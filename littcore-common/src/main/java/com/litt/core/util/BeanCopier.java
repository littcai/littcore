package com.litt.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象属性复制.
 * 
 * <pre>
 * <b>Description：</b>
 *    
 * </pre>
 * 
 * <pre>
 * <b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2011-11-21
 * @version 1.0
 */
public class BeanCopier {
	
	public static final Logger logger = LoggerFactory.getLogger(BeanCopier.class);
	
	private static DozerBeanMapper instance = new DozerBeanMapper(); // 单例即可

	private BeanCopier() {
	}
	
	public static <T> T copy(Object srcObject, Class<T> clazz) {
		return instance.map(srcObject, clazz);
	}
	
	public static <T> T copy(Object srcObject, T destObject) {
		instance.map(srcObject, destObject);
		return destObject;
	}
	
	public static <T,M> List<T> copyList(List<M> srcObjectList, Class<T> clazz) {
		List<T> destList = new ArrayList<T>();
		for (M object : srcObjectList) {
		    if (object != null) {
                T dest = copy(object, clazz);
                destList.add(dest);
            }
		}
		return destList;
	}
	
	/**
	 * 获得Bean拷贝时变更的属性及原始属性值.
	 *
	 * @param srcMap the src map
	 * @param target the target
	 * @return the changed fields
	 */
	public static Map<String, Object> getChangedFields(Map<String, Object> properties, Object target)
	{
		if(properties==null || properties.isEmpty())
			return MapUtils.EMPTY_MAP;
		Map<String, Object> retMap = new HashMap<String, Object>();
		// Loop through the property name/value pairs to be set
        Iterator<Entry<String, Object>> entries = properties.entrySet().iterator();
        while (entries.hasNext()) {

            // Identify the property name and value(s) to be assigned
            Map.Entry<String, Object> entry = entries.next();
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }
            
            // Perform the assignment for this property
            try {
				Object oldValue = PropertyUtils.getProperty(target, name);
				if(!ObjectUtils.equals(oldValue, entry.getValue()))
				{
					logger.debug("Target object:{}'s field:{} will change from {} to {}", new Object[]{target.getClass(), name, oldValue, entry.getValue()});
					retMap.put(name, oldValue);
				}
			} catch (IllegalAccessException e) {
				logger.error("Target object:{}'s field:{} can't access", new Object[]{target.getClass(), name}, e);
			} catch (InvocationTargetException e) {
				logger.error("Target object:{}'s field:{} can't access", new Object[]{target.getClass(), name}, e);
			} catch (NoSuchMethodException e) {
				logger.error("Access target object:{}'s field:{} error", new Object[]{target.getClass(), name}, e);
			}
        }
        return retMap;
		
	}

}
