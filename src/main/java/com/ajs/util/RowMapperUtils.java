/** 
 * @(#)RowMapperUtils.java Jun 13, 2010
 * 
 * Copyright (c) 1995-2010 Wonders Information Co.,Ltd. 
 * 1518 Lianhang Rd,Shanghai 201112.P.R.C.
 * All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Wonders Group.
 * (Social Security Department). You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with Wonders Group. 
 *
 * Distributable under GNU LGPL license by gnu.org
 */

package com.ajs.util;

import com.ajs.exception.RowMapperException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;



public class RowMapperUtils {
	
	static Log log = LogFactory.getLog(RowMapperUtils.class);
	/**
	 * 根据列名获取相应的setter getter
	 * 
	 * @param colName
	 * @return Method[2],分别为setter getter
	 */
	public static Method[] getMethods(Class clazz,String colName) {
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

			if (method.getName().equals(setter.toString())) {
				methods[0] = method;
				count++;
			}
			else if (method.getName().equals(getter.toString())) {
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
	public static void invoke(Object dto, ResultSet rs) throws RowMapperException {
		Class clazz =dto.getClass();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colCount = rsmd.getColumnCount();

			for (int i = 0; i < colCount; ++i) {
				String colName = rsmd.getColumnName(i + 1).toLowerCase();

				Method[] methods = getMethods(clazz,colName);

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
				if ("short".equalsIgnoreCase(type)) {
					value = rs.getShort(colName);
				}
				else if ("int".equalsIgnoreCase(type) || "java.lang.Integer".equalsIgnoreCase(type)) {
					value = rs.getInt(colName);
				}
				else if ("long".equalsIgnoreCase(type)|| "java.lang.Long".equalsIgnoreCase(type)) {
					value = rs.getLong(colName);
				}
				else if ("float".equalsIgnoreCase(type)) {
					value = rs.getFloat(colName);
				}
				else if ("double".equalsIgnoreCase(type) || "java.lang.Double".equalsIgnoreCase(type)) {
					value = rs.getDouble(colName);
				}
				else if ("boolean".equalsIgnoreCase(type)) {
					value = rs.getBoolean(colName);
				}
				else if ("java.lang.String".equalsIgnoreCase(type)) {
					value = rs.getString(colName);
					
				}else if("java.sql.Date".equalsIgnoreCase(type)){
					value =rs.getDate(colName);
				}else if("java.sql.Timestamp".equalsIgnoreCase(type)){
					value =rs.getTimestamp(colName);
				}else if("java.sql.Time".equalsIgnoreCase(type)){
					value =rs.getTime(colName);
					
				//处理java.util.Date
				}else if("java.util.Date".equalsIgnoreCase(type)){
					java.sql.Timestamp date =rs.getTimestamp(colName);
					if(date !=null)
						value =new java.util.Date(date.getTime());
				}else if("java.util.Timestamp".equalsIgnoreCase(type)){
					java.sql.Timestamp date =rs.getTimestamp(colName);
					if(date !=null)
						value =new java.util.Date(date.getTime());
				}else if("java.util.Time".equalsIgnoreCase(type)){
					java.sql.Time date =rs.getTime(colName);
					if(date !=null)
						value =new java.util.Date(date.getTime());
					
				}else if ("java.io.InputStream".equalsIgnoreCase(type)) {
					value = null;
				}
				else if ("char".equalsIgnoreCase(type)) {
					String str = rs.getString(colName);
					value = str.charAt(0);
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
