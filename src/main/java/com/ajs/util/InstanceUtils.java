
package com.ajs.util;

/**
 * 实例化工具，按照给定的clazz进行实例化

 * @version $Revision$ Jun 3, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class InstanceUtils {
	
	public static <T> T getInstance(Class<T> clazz){
		
		T t =null;
		
		try {
			t =clazz.newInstance();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return t;
		
	}
	public static <T> T getInstance(String className){
		
		T t =null;
		
		try {
			Class clazz =Class.forName(className);
			t =(T)clazz.newInstance();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return t;
		
	}
	
	public static Class getClassFromClassName(String className){

		Class clazz =null;
		try {
			clazz=Class.forName(className);
			 
			
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
		
	}
	
}
