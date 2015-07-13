package com.littcore.web.tag.el;

import com.littcore.common.Utility;
import com.littcore.util.ArrayUtils;


/** 
 * 
 * 表单EL.
 * 
 * <pre><b>描述：</b>
 *    处理checkbox、select、radio等一些特殊表单控件
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-12-08
 * @version 1.0
 *
 */
public class FormEL
{	
	
	/**
	 * Contains.
	 *
	 * @param array the array
	 * @param obj the obj
	 * @return true, if successful
	 */
	public static boolean contains(Object[] array, Object obj)
	{
		return ArrayUtils.contains(array, obj);
	}
	
	/**
	 * 处理单选框的选中状态.
	 * 
	 * @param value 值
	 * @param checkedValue 复选框值
	 * 
	 * @return 匹配返回checked="checked"，否则返回空
	 */
	public static String renderChecked(String value,String checkedValue)
	{
		if(value==null||!value.equals(checkedValue))
			return "";
		else
			return "checked=\"checked\"";
	}
	
	/**
	 * 处理复选框的选中状态.
	 * 
	 * @param value 值
	 * @param selectedValue 复选框值
	 * 
	 * @return 匹配返回selected="selected"，否则返回空
	 */
	public static String renderSelected(String value,String selectedValue)
	{
		if(value==null||!value.equals(selectedValue))
			return "";
		else
			return "selected=\"selected\"";
	}
	
	/**
	 * 将是否标志转换为中文表示.
	 * 
	 * @param flag 标志
	 * 
	 * @return 中文表示的是否
	 */
	public static String renderBoolean(Boolean flag)
	{
		if(flag==null)
			return "";
		else if(flag.booleanValue())
			return "是";
		else
			return "否";
	}

	
	/**
	 * 获得字符串长度.
	 * 中文按双字节计算
	 * 
	 * @param content 内容
	 * 
	 * @return 长度
	 */
	public static int getAnsiLength(String content)
	{
		return Utility.ansiLength(content);
	}	
	
}
