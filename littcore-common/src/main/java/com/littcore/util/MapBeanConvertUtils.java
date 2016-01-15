package com.littcore.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.littcore.exception.CheckedBusiException;

public class MapBeanConvertUtils { 
   
    /** 
     * 将一个 Map 对象转化为一个 JavaBean 
     * @param type 要转化的类型 
     * @param map 包含属性值的 map 
     * @return 转化出来的 JavaBean 对象 
     * @throws CheckedBusiException
     *         如果获取Bean属性失败，实例化对象失败，调用setter方法失败
     *  
     */ 
    public static Object convertMap(Class type, Map map) throws CheckedBusiException { 
        BeanInfo beanInfo=null;
        Object obj=null;
		try {
			beanInfo = Introspector.getBeanInfo(type);
			obj = type.newInstance();// 创建 JavaBean 对象 
		} catch (Exception e){			
			throw new CheckedBusiException("get instance of bean:{} error",new Object[]{type},e);
		}
        // 给 JavaBean 对象的属性赋值 
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 

            if (map.containsKey(propertyName)) { 
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。 
                Object value = map.get(propertyName); 

                Object[] args = new Object[1]; 
                args[0] = value; 

                try {
					descriptor.getWriteMethod().invoke(obj, args);
				} catch (Exception e) {
					throw new CheckedBusiException("invoke method:{} value:{} error",new Object[]{propertyName,value},e);
				} 
            } 
        } 
        return obj; 
    } 
    
    /** 
     * 将一个 数据库查询结果Map对象象转化为一个 JavaBean，map字段忽略下划线 
     * @param type 要转化的类型 
     * @param map 包含属性值的 map 
     * @return 转化出来的 JavaBean 对象 
     * @throws CheckedBusiException
     *         如果获取Bean属性失败，实例化对象失败，调用setter方法失败
     */ 
    public static Object convertMapIngoreUnderline(Class type, Map<String,Object> map) 
            throws CheckedBusiException{ 
    	BeanInfo beanInfo=null;
    	Object obj=null;
		try {
			beanInfo = Introspector.getBeanInfo(type); // 获取类属性
			obj = type.newInstance(); // 创建 JavaBean 对象
		} catch (Exception e) {
			throw new CheckedBusiException("get instance of bean:{} error", new Object[] { type }, e);
		}
 
        // 给 JavaBean 对象的属性赋值 
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 
 
            for(Entry<String,Object> entry:map.entrySet()){
            	String key = (String) entry.getKey();
				String columnName = StringUtils.replace(key, "_", "");
				if(propertyName.equalsIgnoreCase(columnName)){
					Object value=entry.getValue();
					  Object[] args = new Object[1]; 
		                args[0] = value; 
		                try{
		                descriptor.getWriteMethod().invoke(obj, args); 
		                } catch (Exception e) {
							throw new CheckedBusiException("invoke bean:{}  method:{} value:{} error",new Object[]{type,propertyName,value},e);
						} 
				}
            }
        } 
        return obj; 
    }

    /** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws checkedBusiException 分析属性失败，获取getter方法失败。。
   
     */ 
    public static Map convertBean(Object bean) 
    	throws CheckedBusiException{  
        Class type = bean.getClass(); 
        Map returnMap = new HashMap(); 
        BeanInfo beanInfo=null;
        try{
        beanInfo = Introspector.getBeanInfo(type); 
        } catch (Exception e) {
			throw new CheckedBusiException("get instance of bean:{} error", new Object[] { type }, e);
		}
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 
            if (!propertyName.equals("class")) { 
                Method readMethod = descriptor.getReadMethod(); 
                Object result=null;
                try{
                result = readMethod.invoke(bean, new Object[0]); 
                } catch (Exception e) {
					throw new CheckedBusiException("invoke method:{} bean:{} error",new Object[]{propertyName,bean.toString()},e);
				} 
                if (result != null) { 
                    returnMap.put(propertyName, result); 
                } else { 
                    returnMap.put(propertyName, ""); 
                } 
            } 
        } 
        return returnMap; 
    } 
}