package com.littcore.log;

import java.util.Date;

/** 
 * 
 * 业务日志对象.
 * 
 * <pre><b>描述：</b>
 *    描述业务日志需要记录的相关信息 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-1-12
 * @version 1.0
 *
 */
public class OpLogVo
{		
	/** 模块编号. */
	private String moduleCode;
	
	/** 业务编号. */
	private String funcCode;
	
	/** 业务类型. */
	private Integer funcType = FuncType.QUERY;

	/** 操作员ID. */
	private Long opId;

	/** 操作员名称. */
	private String opName;

	/** 操作员IP. */
	private String opIp;

	/** 操作日期时间. */
	private Date opDatetime;

	/** 操作内容. */
	private String content;
	
	/** 状态.
	 *  0 - 失败
	 *  1 - 成功
	 */
	private boolean status;

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the opDatetime
	 */
	public Date getOpDatetime()
	{
		return opDatetime;
	}

	/**
	 * @param opDatetime the opDatetime to set
	 */
	public void setOpDatetime(Date opDatetime)
	{
		this.opDatetime = opDatetime;
	}

	/**
	 * @return the opId
	 */
	public Long getOpId()
	{
		return opId;
	}

	/**
	 * @param opId the opId to set
	 */
	public void setOpId(Long opId)
	{
		this.opId = opId;
	}

	/**
	 * @return the opIp
	 */
	public String getOpIp()
	{
		return opIp;
	}

	/**
	 * @param opIp the opIp to set
	 */
	public void setOpIp(String opIp)
	{
		this.opIp = opIp;
	}

	/**
	 * @return the opName
	 */
	public String getOpName()
	{
		return opName;
	}

	/**
	 * @param opName the opName to set
	 */
	public void setOpName(String opName)
	{
		this.opName = opName;
	}

	/**
	 * @return the busiType
	 */
	public Integer getFuncType()
	{
		return funcType;
	}

	/**
	 * @param busiType the busiType to set
	 */
	public void setFuncType(Integer busiType)
	{
		this.funcType = busiType;
	}

	/**
	 * @return the funcCode
	 */
	public String getModuleCode()
	{
		return moduleCode;
	}

	/**
	 * @param funcCode the funcCode to set
	 */
	public void setModuleCode(String funcCode)
	{
		this.moduleCode = funcCode;
	}

	/**
	 * @return the status
	 */
	public boolean getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status)
	{
		this.status = status;
	}

	/**
	 * @return the funcCode
	 */
	public String getFuncCode()
	{
		return funcCode;
	}

	/**
	 * @param funcCode the funcCode to set
	 */
	public void setFuncCode(String funcCode)
	{
		this.funcCode = funcCode;
	}
}
