package com.ajs.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * General purpose utility methods related to ResultSets
 */
public class ResultSetUtils {

	/**
	 * Returns next record of result set as a Map. The keys of the map are the
	 * column names, as returned by the metadata. The values are the columns as
	 * Objects.
	 * 
	 * @param resultSet The ResultSet to process.
	 * @exception SQLException if an error occurs.
	 */
	public static Map getMap(ResultSet resultSet) throws SQLException {

		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Create hashmap, sized to number of columns
		HashMap row = new HashMap(cols, 1);

		// Transfer record into hashmap
		if (resultSet.next()) {
			for (int i = 1; i <= cols; i++) {
				putEntryIgnoreNull(row, metaData, resultSet, i);
			}
		} // end while

		return ((Map) row);

	} // end getMap

	/**
	 * Return a Collection of Maps, each representing a row from the ResultSet.
	 * The keys of the map are the column names, as returned by the metadata.
	 * The values are the columns as Objects.
	 * 
	 * @param resultSet The ResultSet to process.
	 * @exception SQLException if an error occurs.
	 */
	public static Collection getMaps(ResultSet resultSet) throws SQLException {

		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Use ArrayList to maintain ResultSet sequence
		ArrayList list = new ArrayList();

		// Scroll to each record, make map of row, add to list
		while (resultSet.next()) {
			HashMap row = new HashMap(cols, 1);
			for (int i = 1; i <= cols; i++) {
				putEntryIgnoreNull(row, metaData, resultSet, i);
			}
			list.add(row);
		} // end while

		return ((Collection) list);

	} // end getMaps

	/**
	 * Return a Collection of Maps, each representing a row from the ResultSet.
	 * The keys of the map are the column names, as returned by the metadata.
	 * The values are the columns as Objects.
	 * 
	 * @param resultSet The ResultSet to process.
	 * @exception SQLException if an error occurs.
	 */
	public static Collection getMaps(ResultSet resultSet, int start, int numberOfResults)
			throws SQLException {

		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Use ArrayList to maintain ResultSet sequence
		ArrayList list = new ArrayList();

		int k = 0;
		int l = 0;

		// Scroll to each record, make map of row, add to list
		while (l != numberOfResults && resultSet.next()) {
			if (k >= start) {
				HashMap row = new HashMap(cols, 1);
				for (int i = 1; i <= cols; i++) {
					putEntryIgnoreNull(row, metaData, resultSet, i);
				}
				list.add(row);

				l++;
			}
			else {
				k++;
			}
		} // end while

		return ((Collection) list);

	} // end getMaps

	/**
	 * <p>
	 * Copy property values from the origin Map bean to the destination bean for
	 * all cases where the property names are the same. For each property, a
	 * conversion is attempted as necessary. All combinations of standard
	 * JavaBeans and DynaBeans as origin and destination are supported.
	 * Properties that exist in the origin bean, but do not exist in the
	 * destination bean (or are read-only in the destination bean) are silently
	 * ignored.
	 * </p>
	 * 
	 * @param dest Destination bean whose properties are modified
	 * @param orig Origin bean whose properties are retrieved
	 * @exception IllegalAccessException if the caller does not have access to
	 *                the property accessor method
	 * @exception IllegalArgumentException if the <code>dest</code> or
	 *                <code>orig</code> argument is null
	 * @exception InvocationTargetException if the property accessor method
	 *                throws an exception
	 * @exception NullPointerException if <code>orig</code> or
	 *                <code>dest</code> is <code>null</code>
	 */
	public static void copyProperties(Object dest, Map orig) throws IllegalAccessException,
			InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		// Copy the properties, converting as necessary
		PropertyDescriptor destDescriptors[] = PropertyUtils.getPropertyDescriptors(dest);
		for (int i = 0; i < destDescriptors.length; i++) {

			String name = destDescriptors[i].getName();
			if (name == null || "class".equals(name)) {
				continue;
			}

			if (PropertyUtils.isWriteable(dest, name)) {
				String lowerName = name.toLowerCase();
				Object value = orig.get(lowerName);
				if (value == null) {
					continue;
				}

				BeanUtils.copyProperty(dest, name, value);
			}

		}

	}

	/**
	 * Map JDBC objects to Java equivalents. Used by getBean() and getBeans().
	 * <p>
	 * Some types not supported. Many not work with all drivers.
	 * <p>
	 * Makes binary conversions of BIGINT, DATE, DECIMAL, DOUBLE, FLOAT,
	 * INTEGER, REAL, SMALLINT, TIME, TIMESTAMP, TINYINT. Makes Sting
	 * conversions of CHAR, CLOB, VARCHAR, LONGVARCHAR, BLOB, LONGVARBINARY,
	 * VARBINARY.
	 * <p>
	 * DECIMAL, INTEGER, SMALLINT, TIMESTAMP, CHAR, VARCHAR tested with MySQL
	 * and Poolman. Others not guaranteed.
	 */

	public static void putEntryIgnoreNull(Map properties, ResultSetMetaData metaData,
			ResultSet resultSet, int i) throws SQLException {
		// Modified By Jacky 2005-10-10
		// String columnName = metaData.getColumnName(i).toLowerCase();
		String columnName = metaData.getColumnLabel(i).toLowerCase().replaceAll("_", "");

		switch (metaData.getColumnType(i)) {

		// http://java.sun.com/j2se/1.3.0/docs/api/java/sql/Types.html
		case Types.TINYINT:
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.REAL:
		case Types.NUMERIC:
			String sField = resultSet.getString(i);
			if (null != sField) {
				properties.put(columnName, new java.math.BigDecimal(sField));
			}
			break;
		case Types.DATE:
			java.sql.Date datefield = resultSet.getDate(i);

			if (null != datefield) {
				properties.put(columnName, datefield);
			}
			break;
		case Types.TIME:
			java.sql.Time timiefield = resultSet.getTime(i);
			if (null != timiefield) {
				properties.put(columnName, timiefield);
			}
			break;
		case Types.TIMESTAMP:
			java.sql.Timestamp timestampfiled = resultSet.getTimestamp(i);
			if (null != timestampfiled) {
				properties.put(columnName, timestampfiled);
			}
			break;
		case Types.CHAR:
		case Types.CLOB:
		case Types.VARCHAR: {
			String stemp = resultSet.getString(i);
			if (stemp == null)
				stemp = new String("");
			properties.put(columnName, stemp);
		}
			break;
		case Types.LONGVARCHAR:
			// :FIXME: Handle binaries differently?
		case Types.BLOB:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			String stringfield = resultSet.getString(i);
			if (null != stringfield) {
				properties.put(columnName, stringfield);
			}
			break;

		// Otherwise, pass as *String property to be converted
		default: {
			String stemp = resultSet.getString(i);
			if (null != stemp) {
				properties.put(columnName, stemp);
			}
		}
			break;
		} // end switch

	} // end putEntryIgnoreNull

	/**
	 * Populate target bean with the first record from a ResultSet.
	 * 
	 * @param resultSet The ResultSet whose parameters are to be used to
	 *            populate bean properties
	 * @param target An instance of the bean to populate
	 * @exception SQLException if an exception is thrown while setting property
	 *                values, populating the bean, or accessing the ResultSet
	 */
	public static Object getElement(Class target, ResultSet resultSet) throws SQLException {

		Object bean = null;
		// Check prerequisites
		if ((target == null) || (resultSet == null)) {
			throw new SQLException("getCollection: Null parameter");
		}

		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Create hashmap, sized to number of columns
		HashMap properties = new HashMap(cols, 1);

		// Scroll to next record and pump into hashmap
		for (int i = 1; i <= cols; i++) {
			putEntryIgnoreNull(properties, metaData, resultSet, i);
		}
		try {
			bean = target.newInstance();
			copyProperties(bean, properties);
		}
		catch (Throwable t) {
			throw new SQLException("ResultSetUtils.getElement: " + t.getMessage());
		}

		return bean;
	} // end getElement

	/**
	 * Return a ArrayList of beans populated from a ResultSet.
	 * 
	 * @param resultSet The ResultSet whose parameters are to be used to
	 *            populate bean properties
	 * @param target An instance of the bean to populate
	 * @exception SQLException if an exception is thrown while setting property
	 *                values, populating the bean, or accessing the ResultSet
	 */
	public static Collection getCollection(Class target, ResultSet resultSet) throws SQLException {

		// Check prerequisites
		if ((target == null) || (resultSet == null)) {
			throw new SQLException("getCollection: Null parameter");
		}

		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Create hashmap, sized to number of columns
		HashMap properties = new HashMap(cols, 1);

		// Use ArrayList to maintain ResultSet sequence
		ArrayList list = new ArrayList();

		// Scroll to next record and pump into hashmap
		while (resultSet.next()) {
			for (int i = 1; i <= cols; i++) {
				putEntryIgnoreNull(properties, metaData, resultSet, i);
			}
			try {

				Object bean = target.newInstance();

				copyProperties(bean, properties);

				list.add(bean);

			}
			catch (Throwable t) {

				throw new SQLException("ResultSetUtils.getCollection: " + t.getMessage());
			}

			properties.clear();

		} // end while

		return ((Collection) list);

	} // end getCollection

	/**
	 * Return a ArrayList of beans populated from a ResultSet.
	 * 
	 * @param resultSet The ResultSet whose parameters are to be used to
	 *            populate bean properties
	 * @param target An instance of the bean to populate
	 * @exception SQLException if an exception is thrown while setting property
	 *                values, populating the bean, or accessing the ResultSet
	 */
	public static Collection getCollection(Class target, ResultSet resultSet, int start,
			int numberOfResults) throws SQLException {

		// Check prerequisites
		if ((target == null) || (resultSet == null)) {
			throw new SQLException("getCollection: Null parameter");
		}
		// Acquire resultSet MetaData
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();

		// Create hashmap, sized to number of columns
		HashMap properties = new HashMap(cols, 1);

		// Use ArrayList to maintain ResultSet sequence
		ArrayList list = new ArrayList();

		int k = 0;
		int l = 0;

		// Scroll to next record and pump into hashmap
		while (l != numberOfResults && resultSet.next()) {
			if (k >= start) {
				for (int i = 1; i <= cols; i++) {
					putEntryIgnoreNull(properties, metaData, resultSet, i);
				}
				try {

					Object bean = target.newInstance();
					copyProperties(bean, properties);
					list.add(bean);
				}
				catch (Throwable t) {
					throw new SQLException("ResultSetUtils.getCollection: " + t.getMessage());
				}
				properties.clear();

				l++;
			}
			else {
				k++;
			}
		} // end while
		return ((Collection) list);
	}

}
