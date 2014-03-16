package com.litt.core.util;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

/**
 * .
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

}
