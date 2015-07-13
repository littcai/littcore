package com.littcore.pojo;

import java.io.Serializable;
import java.util.Date;

import com.littcore.version.Version;

/**
 * 
 * <b>标题：</b> 基本系统信息对象.
 * <pre><b>描述：</b>
 *    存储应用系统基本信息
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2010-02-02 
 *    				1、增加环境变量读取
 *    				2、增加证书文件路径属性
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-10-07, 2010-02-02
 * @version 1.0, 1.1
 */
public class BaseSystemInfoVo implements ISystemInfoVo, Serializable  
{	
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -3622027031624947638L;

	private Long systemId;
	
	private String systemCode;

	private String systemName;

	private String systemVersion;

	private String copyright;

	private String companyName;
	
	private Date lastUpdateDatetime;	
	
	/** 系统数据保存路径. */
	private String homePath;
	
	/**
	 * 证书文件路径.
	 */
	private String licenseFilePath;
	
	/**
	 * 获得版本信息.
	 * 
	 * @return Version
	 */
	public Version getVersion()
	{
		return Version.parseVersion(this.systemVersion);
	}
	
	/**
	 * JDK版本.
	 */
	private String javaVersion = System.getProperty("java.version");
	
	/**
	 * JDK厂商.
	 */
	private String javaVendor = System.getProperty("java.vendor");
	
	/**
	 * JDK主目录.
	 */
	private String javaHome = System.getProperty("java.home");
	
	/**
	 * 操作系统补丁.
	 */
	private String osPatchLevel = System.getProperty("sun.os.patch.level");
	
	/**
	 * 操作系统架构.
	 */
	private String osArch = System.getProperty("os.arch");
	
	/**
	 * 操作系统版本.
	 */
	private String osVersion = System.getProperty("os.version");
	
	/**
	 *操作系统名称.
	 */
	private String osName = System.getProperty("os.name");
	
	/**
	 * 操作系统用户语言.
	 */
	private String osUserLanguage = System.getProperty("user.language");
	
	/**
	 * 操作系统用户名.
	 */
	private String osUserName = System.getProperty("user.name");
	
	/**
	 * 行分隔符.
	 */
	private String lineSeparator = System.getProperty("line.separator");
	
	/**
	 * 文件名分隔符.
	 */
	private String fileSeparator = System.getProperty("file.separator");
	
	/**
	 * 文件编码.
	 */
	private String fileEncoding = System.getProperty("file.encoding");

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * @return the lastUpdateDatetime
	 */
	public Date getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	/**
	 * @param lastUpdateDatetime the lastUpdateDatetime to set
	 */
	public void setLastUpdateDatetime(Date lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	/**
	 * @return the systemCode
	 */
	public String getSystemCode() {
		return systemCode;
	}

	/**
	 * @param systemCode the systemCode to set
	 */
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * @return the systemVersion
	 */
	public String getSystemVersion() {
		return systemVersion;
	}

	/**
	 * @param systemVersion the systemVersion to set
	 */
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	/**
	 * @return the systemId
	 */
	public Long getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return the homePath
	 */
	public String getHomePath()
	{
		return homePath;
	}

	/**
	 * @param homePath the homePath to set
	 */
	public void setHomePath(String homePath)
	{
		this.homePath = homePath;
	}

	/**
	 * @return the fileEncoding
	 */
	public String getFileEncoding()
	{
		return fileEncoding;
	}

	/**
	 * @return the fileSeparator
	 */
	public String getFileSeparator()
	{
		return fileSeparator;
	}

	/**
	 * @return the javaHome
	 */
	public String getJavaHome()
	{
		return javaHome;
	}

	/**
	 * @return the javaVendor
	 */
	public String getJavaVendor()
	{
		return javaVendor;
	}

	/**
	 * @return the javaVersion
	 */
	public String getJavaVersion()
	{
		return javaVersion;
	}

	/**
	 * @return the lineSeparator
	 */
	public String getLineSeparator()
	{
		return lineSeparator;
	}

	/**
	 * @return the osArch
	 */
	public String getOsArch()
	{
		return osArch;
	}

	/**
	 * @return the osName
	 */
	public String getOsName()
	{
		return osName;
	}

	/**
	 * @return the osPatchLevel
	 */
	public String getOsPatchLevel()
	{
		return osPatchLevel;
	}

	/**
	 * @return the osUserLanguage
	 */
	public String getOsUserLanguage()
	{
		return osUserLanguage;
	}

	/**
	 * @return the osUserName
	 */
	public String getOsUserName()
	{
		return osUserName;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion()
	{
		return osVersion;
	}

	/**
	 * @return the licenseFilePath
	 */
	public String getLicenseFilePath()
	{
		return licenseFilePath;
	}

	/**
	 * @param licenseFilePath the licenseFilePath to set
	 */
	public void setLicenseFilePath(String licenseFilePath)
	{
		this.licenseFilePath = licenseFilePath;
	}
}
