package com.littcore.dao.dataset;

/**
 * 数据集常量类.
 * 
 * <pre><b>描述：</b>
 *    统一管理数据集组件用到的相关常量信息
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-7-4
 * @version 1.0
 */
public class DatasetConstants {
	
	private DatasetConstants(){}
	
	/**
	 * 数据类型.
	 * 
	 * <pre><b>描述：</b>
	 *    枚举所有常见的基础数据类型
	 * </pre>
	 * 
	 * <pre><b>修改记录：</b>
	 *    
	 * </pre>
	 * 
	 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
	 * @since 2013-7-4
	 * @version 1.0
	 */
	public static enum DataType {		
		STRING,BOOLEAN,INTEGER,LONG,FLOAT,DOUBLE,DATE,DECIMAL,CHARACTER,BYTE_ARRAY,CUSTOM;
		
		/**
		 * 类型是否是字符串.
		 *
		 * @param descr 描述
		 * @return true, if is string
		 */
		public static boolean isString(String descr)
		{
			return "java.lang.String".equals(descr) || "String".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Boolean.
		 *
		 * @param descr 描述
		 * @return true, if is boolean
		 */
		public static boolean isBoolean(String descr)
		{
			return "java.lang.Boolean".equals(descr) || "Boolean".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Integer.
		 *
		 * @param descr 描述
		 * @return true, if is string
		 */
		public static boolean isInteger(String descr)
		{
			return "java.lang.Integer".equals(descr) || "int".equals(descr) || "Integer".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Long.
		 *
		 * @param descr 描述
		 * @return true, if is Long
		 */
		public static boolean isLong(String descr)
		{
			return "java.lang.Long".equals(descr) || "Long".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Float.
		 *
		 * @param descr 描述
		 * @return true, if is Float
		 */
		public static boolean isFloat(String descr)
		{
			return "java.lang.Float".equals(descr) || "Float".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Double.
		 *
		 * @param descr 描述
		 * @return true, if is Double
		 */
		public static boolean isDouble(String descr)
		{
			return "java.lang.Double".equals(descr) || "Double".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Date.
		 *
		 * @param descr 描述
		 * @return true, if is Date
		 */
		public static boolean isDate(String descr)
		{
			return "java.util.Date".equals(descr) || "Date".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是BigDecimal.
		 *
		 * @param descr 描述
		 * @return true, if is BigDecimal
		 */
		public static boolean isDecimal(String descr)
		{
			return "java.math.BigDecimal".equals(descr) || "BigDecimal".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是Object.
		 *
		 * @param descr 描述
		 * @return true, if is Object
		 */
		public static boolean isObject(String descr)
		{
			return "java.lang.Object".equals(descr) || "Object".equalsIgnoreCase(descr);
		}
		
		/**
		 * 类型是否是byte[].
		 *
		 * @param descr 描述
		 * @return true, if is byte[]
		 */
		public static boolean isBytes(String descr)
		{
			return "byte[]".equals(descr);
		}
		
	}

}
