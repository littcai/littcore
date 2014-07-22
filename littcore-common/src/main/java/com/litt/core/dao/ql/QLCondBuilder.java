package com.litt.core.dao.ql;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.Utility;
import com.litt.core.util.StringUtils;
import com.litt.core.util.ValidateUtils;

/** 
 * 
 * 动态SQL拼接工具.
 * 
 * <pre><b>描述：</b>
 *    用来根据参数动态生成SQL语句，简化以前需手工编写代码判断参数为NULL或空或空字符串的情况.
 *    该工具参考rapid-xsqlbuilder的原理
 *    
 *    与rapid的不同是采用标准的ANSI注释关键字--作为条件的分隔符，直接通过--分割整个字符串，第一个即为基础语句，后面的都是查询条件。
 *    SELECT * FROM OPERATOR WHERE 1=1
 *    	-- AND LOGIN_ID={loginId}
 *    	-- AND OP_NAME LIKE {opName}
 *    	-- AND STATUS=1
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 	  2013-07-25 1、动态SQL的顺序问题，WHERE\ORDER BY\OTHER仍需要按照原始顺序组装
 * 	  2010-02-26 1、原值占位符[]的功能性变更，使用[]占位符作用与{}相同，生成SQL时都以?替换，唯一的区别是[]占位符所定义的查询条件为必选条件，值为空时将抛出IllegalArgumentException异常。
 * 	  2009-12-24 1、增加动态SQL解析及静态generateQlResult方法，简化SQL的处理
 * 				 2、由于generateQlResult返回结果即为QlResult，简化其命名为generate
 * 				 3、由于IN方式只能通过拼接SQL，故再次开放方括号。大括号与方括号不能同时使用
 * 				 4、增加了对非分页查询条件的支持，定义对象CondParam				
 *               5、BaseDaoImpl中增加了对动态SQL的直接支持，具体看示例代码
 *    2009-04-16 增加对IN的处理，参数以数据方式传入
 *    2009-04-03 1、增加插入静态条件的方法，插入的条件不进行解析
 *    			 2、修改生成规则，原方括号不再支持，需要生成可执行SQL的，可通过设置标志位的方式   
 *    2008-12-11 增加大括号处理模糊匹配的问题简化
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-11-28, 2008-12-11, 2009-04-03, 2009-04-16, 2009-12-24, 2010-02-26
 * @version 1.0, 1.1, 1.2, 1.3, 1.4, 1.5
 *
 */
public class QLCondBuilder implements IQLCondBuilder
{
	private final static Log logger = LogFactory.getLog(QLCondBuilder.class);
	
	public static final Pattern PATTERN_BRACKETS = Pattern.compile("(\\{(.*?)\\})"); 
	
	public static final Pattern PATTERN_BRACKET = Pattern.compile("(\\[(.*?)\\])"); 
	
	/**
	 * 条件分隔符.
	 */
	public static final String COND_SPLIT = "--";	
	
	/** 
	 * 是否调试模式. 
	 * 调试模式直接生成可执行的SQL语句
	 */
	private boolean isDebug = false;
	
	private CondParam condParam;
	
	/** SQL条件. */
	private List<String> sqlCondList = new ArrayList<String>();	
	
	/** 排序条件. */
	private String orderSql = "";
	
	/**
	 * Instantiates a new sql cond builder.
	 */
	public QLCondBuilder(){}
	
	
	/**
	 * 以调试模式生成QL，生成的SQL为可执行QL.
	 * 
	 * @param isDebug 是否调试
	 */
	public QLCondBuilder(boolean isDebug)
	{
		this.isDebug = isDebug;
	}		
	
	/**
	 * 获得参数值.
	 * 
	 * @param paramName 参数名
	 * 
	 * @return the param
	 */
	public Object getParam(String paramName)
	{
		if(this.condParam.hasCond(paramName))
		{
			return this.condParam.getCond(paramName);
		}
		return null;
	}
	
	/**
	 * 增加SQL查询条件.
	 * 
	 * @param condQl the cond sql
	 */
	public void addCond(String condQl)
	{
		sqlCondList.add(condQl);
	}
	
	/**
	 * 增加静态的SQL查询条件
	 * @param condQl
	 */
	public void addStaticCond(String staticCondQl)
	{
		sqlCondList.add(staticCondQl);
	}			
	
	/**
	 * 生成SQL结果对象.
	 * 
	 * @param dynamicQl 动态SQL语句
	 * @param condParam 条件查询对象
	 * @param isDebug 是否调试模式
	 * 
	 * @return qlResult
	 */
	public static QLResult generate(String dynamicQl, CondParam condParam, boolean isDebug)
	{
		QLCondBuilder builder = new QLCondBuilder(isDebug);
		builder.setCondParam(condParam);
		
		//解析动态SQL语句，添加条件语句
		String[] condArray = Utility.splitStringAll(dynamicQl, COND_SPLIT);
		String baseQl = condArray[0];
		for(int i=1;i<condArray.length;i++)
		{
			builder.addCond(condArray[i]);
		}
		//生成结果
		QLResult result = (QLResult)builder.generate();
		result.setBaseQl(baseQl);		
		return result;
	}
	
	/**
	 * 生成SQL结果对象.
	 * 
	 * @param dynamicQl 动态SQL语句
	 * @param condParam 条件查询对象
	 * 
	 * @return qlResult
	 */
	public static QLResult generate(String dynamicQl, CondParam condParam)
	{	
		return generate(dynamicQl, condParam, false);
	}
	
	/**
	 * 生成SQL结果对象.
	 * 
	 * @param dynamicQl 动态SQL语句
	 * @param pageParam 条件查询对象
	 * @param isDebug 是否调试模式
	 * 
	 * @return qlResult
	 */
	public static QLResult generate(String dynamicQl, PageParam pageParam, boolean isDebug)
	{
		QLCondBuilder builder = new QLCondBuilder(isDebug);
		builder.setCondParam(pageParam);
		
		//解析动态SQL语句，添加条件语句
		String[] condArray = Utility.splitStringAll(dynamicQl, COND_SPLIT);
		String baseQl = condArray[0];
		for(int i=1;i<condArray.length;i++)
		{
			builder.addCond(condArray[i]);
		}			
		//生成结果
		QLResult result = (QLResult)builder.generate();
		result.setBaseQl(baseQl);	
		result.setPageIndex(pageParam.getPageIndex());
		result.setPageSize(pageParam.getPageSize());		
		return result;
	}	
	
	/**
	 * 生成SQL结果对象.
	 * 
	 * @param dynamicQl 动态SQL语句
	 * @param pageParam 条件查询对象
	 * 
	 * @return qlResult
	 */
	public static QLResult generate(String dynamicQl, PageParam pageParam)
	{
		return generate(dynamicQl, pageParam, false);
	}
	
	/**
	 * 生成SQL结果对象.
	 * 
	 * @param paramMap 参数映射
	 * 
	 * @return IQLResult
	 */
	public IQLResult generate()
	{
		QLResult qlResult = new QLResult();
		//遍历SQL查询条件，根据有效参数动态拼接SQL语句
		StringBuffer sql = new StringBuffer(200);
		//动态查询条件值列表
		List paramList = new ArrayList();
		for(int i=0;i<sqlCondList.size();i++)
		{
			String condQl = (String)sqlCondList.get(i);
			if(logger.isDebugEnabled())
			{
				logger.debug("处理查询条件："+condQl);
			}			
			if(!StringUtils.startsWithIgnoreCase(condQl, " AND") && !StringUtils.startsWithIgnoreCase(condQl, " OR"))	//判断是否为条件语句，即以AND或OR开头
			{
				sql.append(condQl);
				continue;
			}
			else if(StringUtils.startsWithIgnoreCase(condQl, " ORDER BY"))	//判断是否为排序条件
			{
				this.orderSql = condQl;
				continue;
			}				
			//从查询条件中获取参数变量（正则表达式）
			//先判断是哪种类型变量
			boolean hasPlaceholder = false;
			Matcher matcherBrackets = findBrackets(condQl);		
			if(matcherBrackets.find())	//只要发现一次，但是替换时是全部替换
			{
				hasPlaceholder = true;
				//处理{}型参数
				if(this.isDebug)
					debugMode(matcherBrackets, sql, condQl, false, "\\{","\\}");	
				else
					runMode(matcherBrackets, sql, paramList, condQl, false, "\\{","\\}");
				
			}
			else 
			{
				Matcher matcherBracket = findBracket(condQl);		
				if(matcherBracket.find())	//只要发现一次，但是替换时是全部替换
				{
					hasPlaceholder = true;
					//处理[]型参数
					if(this.isDebug)
						debugMode(matcherBracket, sql, condQl, true, "\\[","\\]");	
					else						
						runMode(matcherBracket, sql, paramList, condQl, true, "\\[","\\]");
						
				}	
			}	
			//如果没有表达式，则直接插入该条件
			if(!hasPlaceholder)
				sql.append(condQl);
		}
		/*
		 * 存在排序时处理排序
		 */
		if(!ValidateUtils.isEmpty(orderSql))	
		{			
//			Pattern pattern = compiler.compile("(\\[(.*?)\\])");
//			PatternMatcher matcherBracket = new Perl5Matcher();
//			while(matcherBracket.contains(orderSql, pattern))
//			{
//				MatchResult result = matcherBracket.getMatch();
//				String sortField = result.group(2);
//				String replaceContent = StringUtils.substringBefore(sortField, "_") + " " + pageParam.getSort(sortField);	

//				orderSql = Util.substitute(matcherBracket, pattern, new Perl5Substitution(replaceContent), orderSql);			
//			}
			String tmpSql = orderSql;
			StringBuilder sbOrder = new StringBuilder(" ORDER BY ");
			Matcher matcherBracket = findBracket(tmpSql);	
			int index = 0;
			boolean hasPlaceholder = false;
			while(matcherBracket.find())	//只要发现一次，但是替换时是全部替换
			{
				hasPlaceholder = true;
				String sortField = tmpSql.substring(matcherBracket.start()+1, matcherBracket.end()-1);			
				if(condParam.hasSort(sortField))
				{
					//排序字段名即为传入参数以横线分隔的前部分
					String replaceContent = StringUtils.substringBefore(sortField, "_") + " " + condParam.getSortOrder(sortField);	
					if(index>0)
						sbOrder.append(", ");
					sbOrder.append(replaceContent);				
					tmpSql = matcherBracket.replaceFirst(replaceContent);	
					index++;
				}
				else {
					tmpSql = matcherBracket.replaceFirst("");	
				}
				matcherBracket = findBracket(tmpSql);	//再次匹配	
			}	
			if(hasPlaceholder)
				qlResult.setOrderQl(sbOrder.toString());
			else {
				qlResult.setOrderQl(tmpSql);
			}
		}
		qlResult.setCondQl(sql.toString());
		qlResult.setParams(paramList.toArray());
		return qlResult;
	}
	
	/**
	 * 通过正则表达式查询
	 * @param content
	 * @param patternStart
	 * @param patternEnd
	 * @return
	 */
	private Matcher findMatcher(String content,String patternStart,String patternEnd)
	{
		String regEx = "("+patternStart+"(.*?)"+patternEnd+")";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(content); 
		return m;
	}
	
	/**
	 * 通过正则表达式查询.
	 * 
	 * @param content the content
	 * 
	 * @return the matcher
	 */
	private static Matcher findBrackets(String content)
	{  
		Matcher m = PATTERN_BRACKETS.matcher(content); 
		return m;
	}
	
	/**
	 * 通过正则表达式查询.
	 * 
	 * @param content the content
	 * 
	 * @return the matcher
	 */
	private static Matcher findBracket(String content)
	{  
		Matcher m = PATTERN_BRACKET.matcher(content); 
		return m;
	}	

	/**
	 * 运行模式，生成?替换.
	 * 
	 * @param sql 基础QL语句
	 * @param paramList the param list
	 * @param condQl the cond sql
	 * @param matcher the matcher
	 * @param isRequired 参数是否必选
	 */
	private void runMode(Matcher matcher, StringBuffer sql, List paramList, String condQl, boolean isRequired, String replaceStart, String replaceEnd)
	{
		String paramName = condQl.substring(matcher.start()+1,matcher.end()-1);
		String paramNameTrue = paramName;	//处理模糊匹配
		if(logger.isDebugEnabled())
		{
			logger.debug("发现动态参数："+paramName);
		}	
		
		//处理模糊匹配
		boolean leftFuzzy = false;
		boolean rightFuzzy = false;
		if(paramName.length() > 0 && paramName.charAt(0) == '%')
		{
			paramNameTrue = paramNameTrue.substring(1);
			leftFuzzy = true;
		}
		if(paramName.endsWith("%"))
		{
			paramNameTrue = paramNameTrue.substring(0,paramNameTrue.length()-1);
			rightFuzzy = true;
		}		
		
		if(!this.condParam.hasCond(paramNameTrue))
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("参数不存在！");
			}
			if(isRequired)
				throw new IllegalArgumentException("必选参数未设置，参数名称："+paramNameTrue);
			return;
		}
		Object value = this.condParam.getCond(paramNameTrue);
		String replaceContent = replaceStart+paramName+replaceEnd;
		if(value==null)
		{
			if(isRequired)
				throw new IllegalArgumentException("必选参数值不能为空，参数名称："+paramNameTrue);
		}
		else if(value instanceof String)	//String型
		{
			if(!ValidateUtils.isBlank((String)value))
			{
				//处理模糊匹配
				if(leftFuzzy)	//左模糊
				{
					value = "%"+value;					
				}
				if(rightFuzzy)		//右模糊
				{
					value = value + "%";
				}	
				
				//condQl = matcher.replaceAll("?");
				condQl = condQl.replaceAll(replaceContent, "?");
				sql.append(condQl);
				paramList.add(value);
			}					
		}
		else if(value instanceof Number)	//Number型
		{		
			//condQl = matcher.replaceAll("?");
			condQl = condQl.replaceAll(replaceContent, "?");
			sql.append(condQl);
			paramList.add(value);			
		}
		else if(value instanceof Date)	//Date型
		{		
			condQl = matcher.replaceAll("?");
			//condQl = condQl.replaceAll(replaceContent, "?");
			sql.append(condQl);
			paramList.add(value);
		}
		else if(value.getClass().isArray())	//数组类型，通常用于IN语句
		{
			int length = Array.getLength(value);
			if(length>0)
			{
				StringBuffer symbol = new StringBuffer();
				for(int i=0;i<length;i++)
				{
					Object param = Array.get(value, i);
					symbol.append('?');
					if(i<(length-1))						
						symbol.append(',');
					paramList.add(param);
				}
				condQl = matcher.replaceAll(symbol.toString());
				//condQl = condQl.replaceAll(replaceContent, symbol.toString());
				sql.append(condQl);
			}
		}
		else
		{
			//condQl = matcher.replaceAll("?");
			condQl = condQl.replaceAll(replaceContent, "?");
			sql.append(condQl);
			paramList.add(value);			
		}		
	}
	
	/**
	 * 调试模式，直接生成QL.
	 * 
	 * @param sql the sql
	 * @param condQl the cond sql
	 * @param matcher the matcher
	 * @param isRequired 是否必选参数
	 * @param replaceStart the replace start
	 * @param replaceEnd the replace end
	 */
	private void debugMode(Matcher matcher, StringBuffer sql, String condQl, boolean isRequired, String replaceStart, String replaceEnd)
	{
		String paramName = condQl.substring(matcher.start()+1,matcher.end()-1);
		String paramNameTrue = paramName;	//处理模糊匹配
		if(logger.isDebugEnabled())
		{
			logger.debug("发现动态参数："+paramName);
		}
		//处理模糊匹配
		boolean leftFuzzy = false;
		boolean rightFuzzy = false;
		if(paramName.length() > 0 && paramName.charAt(0) == '%')
		{
			paramNameTrue = paramNameTrue.substring(1);
			leftFuzzy = true;
		}
		if(paramName.endsWith("%"))
		{
			paramNameTrue = paramNameTrue.substring(0,paramNameTrue.length()-1);
			rightFuzzy = true;
		}	
		
		
		if(!this.condParam.hasCond(paramNameTrue))
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("参数不存在！");
			}
			if(isRequired)
				throw new IllegalArgumentException("必选参数未设置，参数名称："+paramNameTrue);
			return;
		}
		Object value = this.condParam.getCond(paramNameTrue);
		String replaceContent = replaceStart+paramName+replaceEnd;
		if(value==null)
		{
			if(isRequired)
				throw new IllegalArgumentException("必选参数值不能为空，参数名称："+paramNameTrue);
		}
		else if(value instanceof String)	//String型
		{
			if(!ValidateUtils.isBlank((String)value))
			{
				//处理模糊匹配
				if(leftFuzzy)	//左模糊
				{
					value = "%"+value;					
				}
				if(rightFuzzy)		//右模糊
				{
					value = value + "%";
				}	
				condQl = matcher.replaceAll("'"+StringEscapeUtils.escapeSql(value.toString())+"'");		//防止SQL注入					
			}								
		}
		else if(value instanceof Number)	//Number型
		{
			if(value!=null)
			{
				condQl = matcher.replaceAll( value.toString());								
			}	
		}
		else if(value instanceof Date)	//Date型
		{			
			throw new IllegalArgumentException("值替换方式尚不支持Date类型，参数名称："+paramNameTrue);
		}
		else if(value.getClass().isArray())	//数组类型，通常用于IN语句
		{
			int length = Array.getLength(value);
			if(length>0)
			{
				StringBuffer symbol = new StringBuffer();
				for(int i=0;i<length;i++)
				{
					Object paramValue = Array.get(value, i);
					if(paramValue instanceof Number)						
						symbol.append(paramValue.toString());
					else if(paramValue instanceof String)
					{
						symbol.append('\'');
						symbol.append(StringEscapeUtils.escapeSql(paramValue.toString()));
						symbol.append('\'');
					}
					else
					{
						throw new IllegalArgumentException(StringUtils.assembleString("值替换方式尚不支持{}类型，参数名称：{}", new String[]{paramValue.getClass().getName(), paramNameTrue}));
					}
					if(i<(length-1))						
						symbol.append(',');					
				}
				condQl = matcher.replaceAll(symbol.toString());				
			}
		}
		sql.append(condQl);
	}


	/**
	 * @return the orderSql
	 */
	public String getOrderSql() {
		return orderSql;
	}


	/**
	 * @param orderSql the orderSql to set
	 */
	public void setOrderSql(String orderSql) {
		this.orderSql = orderSql;
	}


	/**
	 * @param condParam the condParam to set
	 */
	public void setCondParam(CondParam condParam) {
		this.condParam = condParam;
	}
}
