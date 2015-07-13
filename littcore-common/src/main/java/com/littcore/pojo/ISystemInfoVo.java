package com.littcore.pojo;

import java.util.Date;

import com.littcore.version.Version;

public interface ISystemInfoVo
{
	/**
	 * 获得版本信息.
	 * 
	 * @return Version
	 */
	public Version getVersion();
	

	/**
	 * @return the companyName
	 */
	public String getCompanyName();

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName);

	/**
	 * @return the copyright
	 */
	public String getCopyright();

	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright);

	/**
	 * @return the lastUpdateDatetime
	 */
	public Date getLastUpdateDatetime();

	/**
	 * @param lastUpdateDatetime the lastUpdateDatetime to set
	 */
	public void setLastUpdateDatetime(Date lastUpdateDatetime);

	/**
	 * @return the systemCode
	 */
	public String getSystemCode();

	/**
	 * @param systemCode the systemCode to set
	 */
	public void setSystemCode(String systemCode);

	/**
	 * @return the systemName
	 */
	public String getSystemName();

	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName);

	/**
	 * @return the systemVersion
	 */
	public String getSystemVersion();

	/**
	 * @param systemVersion the systemVersion to set
	 */
	public void setSystemVersion(String systemVersion);

	/**
	 * @return the systemId
	 */
	public Long getSystemId();

	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemId(Long systemId);
	

	/**
	 * @return the homePath
	 */
	public String getHomePath();

	/**
	 * @param homePath the homePath to set
	 */
	public void setHomePath(String homePath);

}