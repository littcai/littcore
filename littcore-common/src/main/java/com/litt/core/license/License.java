package com.litt.core.license;

import java.util.Date;

import com.litt.core.common.Utility;
import com.litt.core.format.FormatDateTime;
import com.litt.core.util.DateUtils;
import com.litt.core.version.Version;

/**
 * 
 * 标题：许可证对象.
 * <pre><b>描述：</b>
 *    定义产品授权的相关属性
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-12-12
 * @version 1.0
 */
public class License 
{	
	
	/**
	 * 演示版.
	 * 
	 * 1、固定使用期限30天.
	 * 2、只能使用基本功能.
	 * 3、不能升级和更新License.
	 * 4、主要用于推广.
	 */
	public static final String LICENSE_TYPE_DEMO = "DEMO";
	
	/**
	 * 试用版.
	 * 
	 * 1、标准功能版本.
	 * 2、根据用户需要可设定试用期限、超过期限则提示用户升级证书.
	 * 3、可通过更新license的方式升级为标准版或企业版
	 * 
	 */
	public static final String LICENSE_TYPE_TRIAL = "TRIAL";
	
	/**
	 * 标准版.
	 * 
	 * 1、标准功能版本.
	 * 2、根据用够需要设定使用期限、超过期限则无法继续使用，提示用户延长使用时间.
	 * 3、使用期限内可升级.
	 * 4、可通过更新license的方式升级为企业版
	 */
	public static final String LICENSE_TYPE_STANDARD = "STANDARD";
	
	/**
	 * 企业版.
	 * 
	 * 1、全部功能版本.
	 * 2、根据用够需要设定使用期限、超过期限则无法继续使用，提示用户延长使用时间.
	 * 3、使用期限内可升级.
	 * 4、可通过更新license的方式降级为标准版
	 */
	public static final String LICENSE_TYPE_ENTERPRISE = "ENTERPRISE";
	
	/**
	 * 开发版.
	 * 
	 * 1、全部功能版本.
	 * 2、无使用期限.
	 * 3、可升级.
	 * 4、提供二次开发API.
	 */
	public static final String LICENSE_TYPE_DEVELOPMENT = "DEVELOPMENT";	
	
	/**
	 * 产品唯一许可ID.
	 */
	private String licenseId;
	
	/**
	 * 许可证类型.
	 */
	private String licenseType = LICENSE_TYPE_DEMO;
	
	/**
	 * 产品名称.
	 */
	private String productName;
	
	/**
	 * 公司名称.
	 */
	private String companyName;
	
	/**
	 * 客户名称.
	 */
	private String customerName;
	
	/**
	 * 产品版本.
	 */
	private Version version;
	
	/**
	 * 许可证创建日期.
	 */
	private Date createDate;
	
	/**
	 * 许可证过期日期.
	 */
	private Date expiredDate;
	
	/**
	 * 签名.
	 */
	private String signature;	
	
	/**
	 * 数据校验.
	 *
	 */
	public void validate() throws LicenseException
	{
		if(Utility.isEmpty(this.licenseId)
			|| Utility.isEmpty(this.licenseType)
			|| Utility.isEmpty(this.productName)
			|| Utility.isEmpty(this.companyName)
			|| Utility.isEmpty(this.customerName)
			|| this.version==null
			|| this.createDate==null
			|| this.expiredDate==null
			|| Utility.isEmpty(this.signature)
			)
		throw new LicenseException("证书文件异常！");	
	}
	
	/**
	 * 是否已过期.
	 *
	 */
	public boolean isExpired() throws LicenseException
	{
		validate();
		if(LICENSE_TYPE_DEVELOPMENT.equals(this.licenseType))
			return false;
		if(new Date().after(this.expiredDate))
			return true;
		if(LICENSE_TYPE_DEMO.equals(this.licenseType))
		{
			if(DateUtils.getBetweenDays(this.createDate, new Date())>30)
				return true;
		}
		return false;
	}
	
	/**
	 * 获得剩余有效天数.
	 * 开发版无限制
	 * @return
	 */
	public int getRemainDays()
	{
		if(LICENSE_TYPE_DEVELOPMENT.equals(this.licenseType))
			return 9999;
		return DateUtils.getBetweenDays(new Date(), this.expiredDate);
	}
	
	/** 
	 * 用来生成密钥的字符串
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(licenseId);
		sb.append(licenseType);
		sb.append(productName);
		sb.append(companyName);
		sb.append(customerName);
		sb.append(version);
		sb.append(FormatDateTime.formatDateNum(createDate));
		sb.append(FormatDateTime.formatDateNum(expiredDate));
		return sb.toString();
	}
	
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
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the expiredDate
	 */
	public Date getExpiredDate() {
		return expiredDate;
	}
	/**
	 * @param expiredDate the expiredDate to set
	 */
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	/**
	 * @return the licenseId
	 */
	public String getLicenseId() {
		return licenseId;
	}
	/**
	 * @param licenseId the licenseId to set
	 */
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	/**
	 * @return the licenseType
	 */
	public String getLicenseType() {
		return licenseType;
	}
	/**
	 * @param licenseType the licenseType to set
	 */
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(Version version) {
		this.version = version;
	}


	
}
