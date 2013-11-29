package com.litt.core.version;

import com.litt.core.common.Utility;


/** 
 * 
 * 产品版本信息.
 * 
 * <pre><b>描述：</b>
 *    版本变更说明：
 *		1、主版本号
 *			主版本号的变更意味着代码的大量重写，这些重写使得新版本无法向下兼容。在未成为正式版之前，主版本号一律置0
 *		2、子版本号
 *			子版本号的变更意味着功能的增强及改善，同时兼顾的向下兼容性。新的版本可以在不用修改任何代码的情况下进行替换升级
 *		3、修正版本号
 *			修正版本号的变更意味着代码中错误的修复，建议立即升级到最新的修正版本以避免程序上的错误。
 *		4、注意
 *			4.1、版本号的起始值约定为0  
 *			4.2、当只有子版本号变更时（即只新增功能），则修正版本号约定置0，即该版本是非必要升级。 
 *			4.3、当有子版本号和修正版本号同时变更时（即新增功能的同时又修复了原来的一些错误），则修正版本号约定置1，即该版本为必要升级，建议立即升级。 
 *			4.4、当主版本号和子版本号同时变更时（即在不兼容的情况下又新增了很多功能），则子版本号约定置1。
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-2-3
 * @version 1.0
 *
 */
public class Version
{	
	/**
	 * 主版本.
	 */
	private int majorVersion;
	
	/**
	 * 子版本.
	 */
	private int minorVersion;
	
	/**
	 * 修订版.
	 */
	private int revision;

	/**
	 * 解析版本信息.
	 * 
	 * @param versionStr 版本字符串
	 * 
	 * @return Version对象
	 */
	public static Version parseVersion(String versionStr)
	{
		if(Utility.isEmpty(versionStr))
			throw new IllegalArgumentException("版本信息不能为空！");
		String[] versionArray = Utility.splitStringAll(versionStr, ".");
		if(versionArray.length!=3)
			throw new IllegalArgumentException("版本信息格式不正确！");
		
		int majorVersion = Utility.parseInt(versionArray[0]);
		int minorVersion = Utility.parseInt(versionArray[1]);
		int revision = Utility.parseInt(versionArray[2]);
		if(majorVersion<0 || minorVersion<0 || revision<0)
			throw new IllegalArgumentException("版本信息格式不正确！");
		
		return new Version(majorVersion, minorVersion, revision);		
	}	

	public Version(int majorVersion, int minorVersion, int revision)
	{
		super();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.revision = revision;
	}
	
	/**
	 * 获得版本信息字符串.
	 * 
	 * @return the string
	 */
	public String toString()
	{
		return Utility.joinString(new String[]{String.valueOf(majorVersion),String.valueOf(minorVersion),String.valueOf(revision)}, ".");
	}
	
	/**
	 * 当前版本是否早于传入版本.
	 * 
	 * @param version 版本对象
	 * 
	 * @return 是返回true，否返回false
	 */
	public boolean isBefore(Version version)
	{
		if(this.majorVersion>version.getMajorVersion())
			return false;
		else if(this.majorVersion==version.getMajorVersion())
		{
			if(this.minorVersion>version.getMinorVersion())
				return false;
			else if(this.minorVersion==version.getMinorVersion())
			{
				return !(this.revision>=version.getRevision());
			}
			else
				return true;
		}
		else
			return true;		
	}
	
	/**
	 * 当前版本是否晚于传入版本.
	 * 
	 * @param version 版本对象
	 * 
	 * @return 是返回true，否返回false
	 */
	public boolean isAfter(Version version)
	{
		return !this.isBefore(version);
	}

	/**
	 * @return the majorVersion
	 */
	public int getMajorVersion()
	{
		return majorVersion;
	}

	/**
	 * @return the minorVersion
	 */
	public int getMinorVersion()
	{
		return minorVersion;
	}

	/**
	 * @return the revision
	 */
	public int getRevision()
	{
		return revision;
	}

}
