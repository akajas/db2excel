

package com.ajs.mapper;

import com.ajs.exception.RowMapperException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @version $Revision$ Jun 3, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class AbstractRowResolve {

	Log log = LogFactory.getLog(AbstractRowResolve.class);

	Class clazz = null;

	/**
	 * 根据列名获取相应的setter getter
	 * 
	 * @param colName
	 * @return Method[2],分别为setter getter
	 */
	protected Method[] getMethods(String colName) {
		Method[] methods = new Method[2];

		StringBuffer getter = new StringBuffer(colName.length() + 3);
		StringBuffer setter = new StringBuffer(colName.length() + 3);

		getter.append("get");
		setter.append("set");

		getter.append(Character.toUpperCase(colName.charAt(0)));
		setter.append(Character.toUpperCase(colName.charAt(0)));

		getter.append(colName.substring(1));
		setter.append(colName.substring(1));

		Method[] allMyMethods = clazz.getMethods();

		int count = 0;
		for (Method method : allMyMethods) {

			if (!Modifier.isPublic(method.getModifiers()))
				continue;

			String methodName="";
			if(method.getName().startsWith("get")){
				methodName=method.getName().toLowerCase();
				char a=Character.toUpperCase(method.getName().charAt(3));
				methodName = "get" + a +methodName.substring(4);
			}

			if(method.getName().startsWith("set")){
				methodName=method.getName().toLowerCase();
				char a=Character.toUpperCase(method.getName().charAt(3));
				methodName = "set" + a + methodName.substring(4);
			}

			if (methodName.equals(setter.toString())) {
				methods[0] = method;
				count++;
			}
			else if (methodName.equals(getter.toString())) {
				methods[1] = method;
				count++;
			}

			if (count == 2)
				break;

		}

		return methods;
	}

	/**
	 * @param dto
	 * @param rs
	 * @throws SQLException
	 */
	protected void invoke(Object dto, ResultSet rs) throws RowMapperException {

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colCount = rsmd.getColumnCount();

			for (int i = 0; i < colCount; ++i) {
				String colName = rsmd.getColumnName(i + 1).toLowerCase();

				Method[] methods = getMethods(colName);

				Method setter = methods[0];
				Method getter = methods[1];

				if (setter == null) {
					if (log.isDebugEnabled()) {
						log.debug(String.format("not setter of property %s", colName));
					}
					continue;
					// throw new RowMapperException("004", String.format("not setter of property %s", colName), null);
				}
				if (getter == null) {
					if (log.isDebugEnabled()) {
						log.debug(String.format("not getter of property %s", colName));
					}
					continue;
					// throw new RowMapperException("004", String.format("not getter of property %s", colName), null);
				}
				String type = getter.getReturnType().getName();

				Object value = null;
				if("short".equalsIgnoreCase(type) || "java.lang.Short".equalsIgnoreCase(type)){
					value = rs.getShort(colName);
					
				}else if("int".equalsIgnoreCase(type) || "java.lang.Integer".equalsIgnoreCase(type)){
					value = rs.getInt(colName);
					
				}else if("long".equalsIgnoreCase(type) || "java.lang.Long".equalsIgnoreCase(type)){
					value = rs.getLong(colName);
					
				}else if("float".equalsIgnoreCase(type) || "java.lang.Float".equalsIgnoreCase(type)){
					value = rs.getFloat(colName);
					
				}else if("double".equalsIgnoreCase(type) || "java.lang.Double".equalsIgnoreCase(type)){
					value = rs.getDouble(colName);
					
				}else if("boolean".equalsIgnoreCase(type) || "java.lang.Boolean".equalsIgnoreCase(type)){
					value = rs.getBoolean(colName);
					
				}else if("char".equalsIgnoreCase(type) || "java.lang.Character".equalsIgnoreCase(type)){
					value = rs.getString(colName).charAt(0);
					
				}else if("java.math.BigDecimal".equalsIgnoreCase(type) || "java.math.BigInteger".equalsIgnoreCase(type)){
					value = rs.getBigDecimal(colName);
					
				}else if("java.lang.String".equalsIgnoreCase(type)){
					value = rs.getString(colName);
					
				}else if("java.sql.Date".equalsIgnoreCase(type)){
					value = rs.getDate(colName);
					
				}else if("java.util.Date".equalsIgnoreCase(type)){
					Timestamp tsmp = rs.getTimestamp(colName);
					
					if(tsmp != null){
						value = new java.util.Date(tsmp.getTime());
					}
					
				}else if("java.sql.Timestamp".equalsIgnoreCase(type)){
					value = rs.getTimestamp(colName);
					
				}else if("java.sql.Time".equalsIgnoreCase(type)){
					value = rs.getTime(colName);
					
				}

				try {
					setter.invoke(dto, value);
				}
				catch (Exception e) {
					throw new RowMapperException(String.format("invoke error[method:%s]", setter.getName()), e);
				}
			}

		}
		catch (SQLException se) {
			throw new RowMapperException(String.format("SQL异常:%s", se.getMessage()), se);
		}

	}
}
