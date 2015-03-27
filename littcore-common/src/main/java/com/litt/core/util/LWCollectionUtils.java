package com.litt.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 *.
 * 
 *<pre>
 * <b>Description:</b>
 * 
 *</pre>
 * 
 *<pre>
 * <b>Changelog:</b>
 * 
 *</pre>
 * 
 * @description Some static utility method
 *@author:luwei
 *@since:Jan 31, 2012
 */

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils{

	/**
	 *根据对象中的属性名称和属性值，返回list对象中所有符合该属性==value的对象
	 * 
	 * @param <T> Type of list element
	 * @param <M> Type of Source element Class
	 * @param list
	 *            传入一个list对象
	 * @param propertyName
	 *            对象中的一个参数
	 * @param value
	 *            参数对应的值
	 */
	public static <T, M> List<T> getListObjectByProperty(List<T> list, String propertyName, M value) {
		List<T> returnList = new ArrayList<T>();
		if(list==null)
			return returnList;
		for (T t : list) {
			M property;
			try {
				property = (M) PropertyUtils.getProperty(t, propertyName);
				if (property.equals(value))
					returnList.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return returnList;

	}

	public static <T, M> List<M> getListValueByProperty(List<T> list, String propertyName) {
		List<M> returnList = new ArrayList<M>();
		if(list==null)
			return returnList;
		for (T t : list) {
		    if(t==null){
                continue;
            }
			M property;
			try {
				property = (M) PropertyUtils.getProperty(t, propertyName);
				returnList.add(property);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return returnList;
	}

	public static <T> List<T> geListAdded(List<T> newList, List<T> oldList) {
		List<T> addedList = new ArrayList<T>();
		for (T element : newList) {
			if (!oldList.contains(element)) {
				addedList.add(element);
			}
		}
		return addedList;
	}

	public static boolean compareHasUnionSet(List<String> listA, List<String> listZ) {
		for (String a : listA) {
			for (String z : listZ) {
				if (a.equals(z)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean compareEqual(List<String> listA, List<String> listZ) {
		if (listA.size() != listZ.size())
			return false;
		int i = 0;
		for (String a : listA) {
			for (String z : listZ) {
				if (a.equals(z)) {
					i = i + 1;
					continue;
				}
			}
		}
		if (i == listA.size()) {
			return true;
		} else {
			return false;
		}
	}

	public static <T, M> List<M> getOtherList(Map<T, List<M>> map, T key) {
		List<M> otherList = new ArrayList<M>();
		for (Map.Entry<T, List<M>> entry : map.entrySet()) {
			if (!entry.getKey().equals(key)) {
				otherList.addAll(entry.getValue());
			}
		}

		return otherList;
	}



	public static <T> void addToList(T t,List<T> list){
        if(t!=null){
            list.add(t);
        }
    }
	
	

	
	public static Long[] convertStrToLongArray(String[] strArray){
		Long[] longArray=new Long[strArray.length];
		for(int i=0;i<strArray.length;i++){
			longArray[i]=Long.valueOf(strArray[i]);
		}
		return longArray;
	}
	
	
	public static <T> List<T> convertArrayToList(T[] array){
		List<T> list=new ArrayList<T>();
		Collections.addAll(list, array);
		return list;
	}
	
	public static <T> List<T> oneToList(T data){
		List<T> list=new ArrayList<T>();
		list.add(data);
		return list;
	}
	
	public static <T> Boolean  isEmptyArrayList(List<T> list){
		if(list==null||list.size()==0){
			return true;
		}
		return false;
	}

}
