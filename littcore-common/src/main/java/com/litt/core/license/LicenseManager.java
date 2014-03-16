package com.litt.core.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.litt.core.common.Utility;
import com.litt.core.format.FormatDateTime;
import com.litt.core.security.DecryptFailedException;
import com.litt.core.security.DigitalSignatureTool;
import com.litt.core.security.ISecurity;
import com.litt.core.security.algorithm.Algorithm;
import com.litt.core.security.algorithm.DESTool;
import com.litt.core.util.XmlUtils;
import com.litt.core.version.Version;


/**
 * 
 * <b>标题：</b> 证书校验类.
 * <pre><b>描述：</b>
 *    校验当前系统运行证书(基于标准证书认证架构)
 *    
 *    《防君子，不防小人！》
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2012-06-13
 *    				1、允许加载过期证书，以支持系统启动。
 *    2010-02-02 
 *    				1、增加读取license文件的缓存，不重复读取文件
 *    				2、增加刷新license文件的方法，用于重新读取文件
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-12, 2010-02-02
 * @version 1.0, 1.1
 * 
 */
public final class LicenseManager 
{
	public static final LicenseManager licenseManager = new LicenseManager();
	
	/**
	 * license实例对象.
	 */
	private License license;
	
	/** 公钥. */
	private PublicKey pubKey;
	
	/**
	 * 禁止生成实例.
	 */
	private LicenseManager(){}
	
	/**
	 * Gets the single instance of LicenseManager.
	 * 
	 * @return single instance of LicenseManager
	 */
	public static final LicenseManager getInstance()
	{		
		return licenseManager;
	}
	
	/**
	 * 设置预先加载的证书实例(不存在或未初始化时抛异常).
	 * 
	 * @param license the license
	 * 
	 * @return License 对象
	 * 
	 */
	public static final void setDefaultLicense(License license)
	{
		getInstance().license = license;
	}
	
	/**
	 * 读取预先加载的证书实例(不存在或未初始化时抛异常).
	 * 
	 * @return License 对象
	 * 
	 * @throws LicenseException 证书不存在
	 */
	public static final License getDefaultLicense() throws LicenseException
	{
		if(getInstance().license!=null)
			return getInstance().license;
		else
			throw new LicenseException();		
	}	
	
	/**
	 * 读取预先加载的密钥实例(不存在或未初始化时抛异常).
	 * 
	 * @return License 对象
	 * 
	 * @throws LicenseException 密钥不存在
	 */
	public static final PublicKey getDefaultKey() throws LicenseException
	{
		if(getInstance().pubKey!=null)
			return getInstance().pubKey;
		else
			throw new LicenseException();		
	}	
	
	/**
	 * 读取license.
	 * 
	 * @param licenseFile 证书文件
	 * 
	 * @return License 对象
	 * 
	 * @throws LicenseException 证书无效异常
	 */
	public static final License getLicense(File licenseFile) throws LicenseException
	{	
		try
		{
			if(getInstance().license!=null)
				return getInstance().license;
			else
				return loadLicense(licenseFile);
		}
		catch(Exception e)
		{			
			throw new LicenseException(e);
		}
	}
	
	/**
	 * 读取license.
	 * 
	 * @param licenseFilePath 证书文件路径
	 * 
	 * @return License 对象
	 * 
	 * @throws LicenseException 证书无效异常
	 */
	public static final License getLicense(String licenseFilePath) throws LicenseException
	{
		try
		{
			if(getInstance().license!=null)
				return getInstance().license;
			else
				return loadLicense(licenseFilePath);
		}
		catch(Exception e)
		{			
			throw new LicenseException(e);
		}
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKey 公钥
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static void validateLicense() throws LicenseException
	{
		LicenseManager instance = LicenseManager.getInstance();
		validateLicense(instance.license, instance.pubKey);
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKeyFile 公钥文件
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static void validateLicense(License license, String pubKeyFilePath) throws LicenseException
	{
		File pubKeyFile = new File(pubKeyFilePath);
		validateLicense(license, pubKeyFile);
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKeyFile 公钥文件
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static void validateLicense(File licenseFile, File pubKeyFile) throws LicenseException
	{
		License license = getLicense(licenseFile);
		validateLicense(license, pubKeyFile);
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKey 公钥
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static void validateLicense(License license, PublicKey pubKey) throws LicenseException
	{
		try 
		{	
			DigitalSignatureTool tool = new DigitalSignatureTool(pubKey);
			validateLicense(license, tool);			
		}
		catch (FileNotFoundException e) 
		{
			throw new LicenseException(e);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new LicenseException(e);
		}
		catch (DecryptFailedException e) 
		{			
			throw new LicenseException(e);
		}
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKeyFile 公钥文件
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static void validateLicense(License license, File pubKeyFile) throws LicenseException
	{		
		try 
		{	
			DigitalSignatureTool tools = new DigitalSignatureTool(pubKeyFile);
			validateLicense(license, tools);			
		}
		catch (FileNotFoundException e) 
		{
			throw new LicenseException(e);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new LicenseException(e);
		}
		catch (DecryptFailedException e) 
		{			
			throw new LicenseException(e);
		}		
	}
	
	/**
	 * 校验license.
	 * 
	 * @param license 证书对象
	 * @param pubKeyFile 公钥文件
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	private static void validateLicense(License license, DigitalSignatureTool tools) throws LicenseException
	{
		if(license==null)
			throw new LicenseException("License is not exist!");
		license.validate();			
		try 
		{				
			String signedData = license.getSignature();
			tools.validateSign(license.toString(), signedData);					
		}		
		catch (DecryptFailedException e) 
		{			
			throw new LicenseException(e);
		}
		//根据证书类型进行数据校验		
//允许加载过期证书		
//		if(license.isExpired())
//		{
//			throw new LicenseException("证书已过期！");			
//		}		
	}	
	
	/**
	 * 校验license.
	 * 
	 * @param licenseFilePath 证书文件路径
	 * @param pubKeyFilePath 公钥文件路径
	 * 
	 * @throws LicenseException 未通过认证则引发此异常
	 */
	public static final void validateLicense(String licenseFilePath, String pubKeyFilePath) throws LicenseException
	{
		//Note, the software license expressely forbids tampering with this check. 
		License license = getLicense(licenseFilePath);
		validateLicense(license, pubKeyFilePath);
	}
	
	/**
	 * 读取证书.
	 * 
	 * @param licenseFilePath 证书文件路径
	 * 
	 * @return 证书对象
	 * 
	 * @throws FileNotFoundException 文件未找到异常
	 * @throws DocumentException 文档解析异常
	 */
	private static License loadLicense(String licenseFilePath) throws FileNotFoundException,DocumentException
	{
		File licenseFile = new File(licenseFilePath);
		return loadLicense(licenseFile);
	}
	
	/**
	 * 读取证书.
	 * 
	 * @param licenseFile 证书文件
	 * 
	 * @return 证书对象
	 * 
	 * @throws FileNotFoundException 文件未找到异常
	 * @throws DocumentException 文档解析异常
	 */
	public static License loadLicense(File licenseFile) throws DocumentException
	{		
		SAXReader saxReader = new SAXReader();   					
		Document document = saxReader.read(licenseFile);            
		return loadLicense(document);
	}
	
	public static License loadLicenseFromContent(String xmlContent) throws LicenseException
	{							
		try {
			Document document = XmlUtils.readXml(xmlContent);
			return loadLicense(document);
		} catch (Exception e) {
			throw new LicenseException(e);
		}
	}

	/**
	 * @param document
	 * @return
	 */
	private static License loadLicense(Document document) {
		Element rootElement = document.getRootElement(); // 将根节点植入rootElement		
		License	license = new License();
		license.setLicenseId(rootElement.element("licenseId").getTextTrim());
		license.setLicenseType(rootElement.element("licenseType").getTextTrim());
		license.setProductName(rootElement.element("productName").getTextTrim());
		license.setCompanyName(rootElement.element("companyName").getTextTrim());
		license.setCustomerName(rootElement.element("customerName").getTextTrim());
		license.setVersion(Version.parseVersion(rootElement.element("version").getTextTrim()));
		license.setCreateDate(Utility.parseDate(rootElement.element("createDate").getTextTrim()));
		license.setExpiredDate(Utility.parseDate(rootElement.element("expiredDate").getTextTrim()));
		license.setSignature(rootElement.element("signature").getTextTrim());
		return license;
	}
	
	/**
	 * 根据License信息解码证书内容
	 * 
	 * @param licenseId
	 * @param content
	 * @return
	 * @throws LicenseException
	 */
	public static String decryptLicense(String licenseId, String content) throws LicenseException
	{
		//用DES算法进行解密
		try {
			ISecurity security = new DESTool(licenseId, Algorithm.BLOWFISH);			
			return security.decrypt(content);
		} catch (NoSuchAlgorithmException e) {
			throw new LicenseException(e);
		} catch (DecryptFailedException e) {
			throw new LicenseException(e);
		}		
	}
	
	/**
	 * 将证书信息写入文件.
	 * 
	 * @param license the license
	 * @param targetFile 目标文件
	 */
	public static void writeLicense(License license, File targetFile) throws IOException
	{
		Document document = DocumentHelper.createDocument();
		document.addElement("license");
		Element root = document.getRootElement();
		
		//设置属性
		root.addElement("licenseId").setText(license.getLicenseId());
		root.addElement("licenseType").setText(license.getLicenseType());
		root.addElement("productName").setText(license.getProductName());
		root.addElement("companyName").setText(license.getCompanyName());
		root.addElement("customerName").setText(license.getCustomerName());
		root.addElement("version").setText(license.getVersion().toString());
		root.addElement("createDate").setText(FormatDateTime.formatDate(license.getCreateDate()));
		root.addElement("expiredDate").setText(FormatDateTime.formatDate(license.getExpiredDate()));
		root.addElement("signature").setText(license.getSignature());
		XmlUtils.writeXml(targetFile, document);
	}
	
	/**
	 * 重新加载证书.
	 * 
	 * @param licenseFilePath 证书文件路径
	 * @param pubKeyFilePath 公钥文件路径

	 * @throws LicenseException 证书异常
	 */
	public static void reload(String licenseFilePath, String pubKeyFilePath) throws LicenseException
	{
		try
		{
			License license = loadLicense(licenseFilePath);
			//先校验新证书的有效性
			validateLicense(license, pubKeyFilePath);
			//如果已存在旧证书，还要比较新旧证书的区别 		
			getInstance().license = license;	//更新到内存
			getInstance().pubKey = DigitalSignatureTool.readPubKey(new File(pubKeyFilePath));
		}
		catch (FileNotFoundException e)
		{
			throw new LicenseException("加载证书异常！", e);
		}
		catch (DocumentException e)
		{
			throw new LicenseException("加载证书异常！", e);
		}
		catch (DecryptFailedException e)
		{
			throw new LicenseException("加载证书异常！", e);
		}		
	}
	
	public static void main(String[] args) throws Exception
	{
		LicenseManager.validateLicense("D:\\license.xml", "D:\\teamwork.dat");
	}

}
