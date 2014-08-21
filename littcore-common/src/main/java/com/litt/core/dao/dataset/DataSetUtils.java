package com.litt.core.dao.dataset;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.MirrorList;
import net.vidageek.mirror.set.dsl.FieldSetter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.dao.dataset.annotation.MapColumn;
import com.litt.core.util.DateUtils;

public abstract class DataSetUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataSetUtils.class);	
	
	/**
	 * 将Map映射为VO对象.
	 * 支持Annotation的转换
	 * 
	 * @param clazz
	 * @param dataMap
	 * @return
	 */
	public static <T, K, V> T mapping(Class<T> clazz, Map<K, V> dataMap)
	{			
		try {
			T instance = (T)clazz.getDeclaredConstructors()[0].newInstance();
			
			ClassController<T> classController = new Mirror().on(clazz);
			MirrorList<Field> fieldList = classController.reflectAll().fields();		
			
			for (Field field : fieldList) {
				String columnName = field.getName();
				if(field.isAnnotationPresent(MapColumn.class))
				{
					MapColumn mapColumn = field.getAnnotation(MapColumn.class);
					columnName = mapColumn.name();
				}	
				if(dataMap.containsKey(columnName))
				{
					V value = dataMap.get(columnName);						
					
					setFieldValue(instance, field, value);
				}
			}	
			return instance;
		} catch (SecurityException e) {
			throw new DataSetMappingException(e);
		} catch (InstantiationException e) {
			throw new DataSetMappingException(e);
		} catch (IllegalAccessException e) {
			throw new DataSetMappingException(e);
		} catch (InvocationTargetException e) {
			throw new DataSetMappingException(e);
		}
	}	
	
	public static <T, K, V> T mapping(T instance, Map<K, V> dataMap)
	{		
		MirrorList<Field> fieldList = new Mirror().on(instance.getClass()).reflectAll().fields();
		
		for (Field field : fieldList) {
			String columnName = field.getName();
			if(field.isAnnotationPresent(MapColumn.class))
			{
				MapColumn mapColumn = field.getAnnotation(MapColumn.class);
				columnName = mapColumn.name();
			}	
			if(dataMap.containsKey(columnName))
			{
				V value = dataMap.get(columnName);						
				setFieldValue(instance, field, value);
			}
		}	
		return instance;		
	}

	/**
	 * @param instance
	 * @param field
	 * @param value
	 */
	public static <V, T> void setFieldValue(T instance, Field field, V value) {
		FieldSetter fieldSetter = new Mirror().on(instance).set().field(field.getName());
		//通过字段的数据类型匹配set方法
		if(field.getType().equals(int.class))
		{ 
			Integer newValue = DataSetUtils.typeCastInteger(value);
			fieldSetter.withValue(newValue);
		}
		else if(field.getType().equals(long.class))
		{
			Long newValue = DataSetUtils.typeCastLong(value);
			fieldSetter.withValue(newValue);
		}
		else if(field.getType().equals(double.class))
		{
			Double newValue = DataSetUtils.typeCastDouble(value);
			fieldSetter.withValue(newValue);
		}
		else if(field.getType().equals(float.class))
		{
			Float newValue = DataSetUtils.typeCastFloat(value);
			fieldSetter.withValue(newValue);
		}
		else if(field.getType().equals(boolean.class))
		{
			Boolean newValue = DataSetUtils.typeCastBoolean(value);
			fieldSetter.withValue(newValue);
		}
		else if(field.getType().equals(Date.class))
		{
			Date newValue = DataSetUtils.typeCastDate(value);
			fieldSetter.withValue(newValue);
		}
		else
		{
			fieldSetter.withValue(value);
		}
	}
	
	public static Boolean typeCastBoolean(Object value) throws TypeCastException
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof Boolean)
        {
            return (Boolean)value;
        }
        if (value instanceof Number)
        {
            Number number = (Number)value;
            if (number.intValue() == 0)
                return Boolean.FALSE;
            else
                return Boolean.TRUE;
        }

        if (value instanceof String)
        {
            String string = (String)value;

            if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false") || string.equalsIgnoreCase("1") || string.equalsIgnoreCase("0"))
            {
                return Boolean.valueOf(string);
            }          
        }
        throw new TypeCastException(value, "Boolean");
    }

	
	public static Integer typeCastInteger(Object value) 
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }
        
        if (value instanceof Number)
        {
            return new Integer(((Number)value).intValue());
        }
        return typeCastInteger(new BigDecimal(value.toString()));       
    }
	
	public static Long typeCastLong(Object value) 
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof Number)
        {
            return new Long(((Number)value).longValue());
        }
        return typeCastLong(new BigDecimal(value.toString()));       
    }
	
	public static Double typeCastDouble(Object value) 
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof Number)
        {
            return new Double(((Number)value).doubleValue());
        }
        return typeCastDouble(new BigDecimal(value.toString()));       
    }
	
	public static Float typeCastFloat(Object value) 
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof Number)
        {
            return new Float(((Number)value).floatValue());
        }
        return typeCastFloat(new BigDecimal(value.toString()));       
    }
	
	public static Number typeCastNumber(Object value) throws TypeCastException
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof BigDecimal)
        {
            return (Number)value;
        }

        if (value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue() ? new BigDecimal((double)1) : new BigDecimal((double)0);
        }

        try
        {
            return new BigDecimal(value.toString());
        }
        catch (java.lang.NumberFormatException e)
        {
            throw new TypeCastException(value, "Number", e);
        }
    }

	
	public static String typeCastString(Object value) throws TypeCastException
    {
        logger.debug("typeCast(value={}) - start", value);
        if (value == null)
        {
            return null;
        }

        if (value instanceof String)
        {
            return (String)value;
        }

        if (value instanceof Boolean)
        {
            return value.toString();
        }
        
        return value.toString();
    }

	public static Date typeCastDate(Object value) throws TypeCastException
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null)
        {
            return null;
        }

        if (value instanceof java.sql.Date)
        {
            return DateUtils.convertSqlDate2Date((java.sql.Date)value);
        }

        if (value instanceof java.util.Date)
        {
            return (java.util.Date)value;
        }

        if (value instanceof Long)
        {
            Long date = (Long)value;
            return new java.util.Date(date.longValue());
        }    
        throw new TypeCastException(value, "Date");
    }

}
