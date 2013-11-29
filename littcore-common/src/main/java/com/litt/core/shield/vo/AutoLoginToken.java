package com.litt.core.shield.vo;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.joda.time.DateTime;

import com.litt.core.common.Utility;
import com.litt.core.format.FormatDateTime;
import com.litt.core.security.DecryptFailedException;
import com.litt.core.security.EncryptFailedException;
import com.litt.core.security.ISecurity;
import com.litt.core.security.SecurityFactory;
import com.litt.core.uid.UUID;
import com.litt.core.util.Assert;
import com.litt.core.util.StringUtils;

/**
 * 自动登录令牌.
 * 
 * <pre><b>描述：</b>
 *    记录自动登录参数信息。
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-9-29
 * @version 1.0
 */
public class AutoLoginToken implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id = UUID.randomUUID().toString();
	
	private String autoLoginIp;
	
	private String loginId;
	
	private String encryptedPassword;
	
	private Date createDatetime;
	
	private long expiredTime = 14 * 24 * 60 * 60 * 1000L;	//默认超时二周

	/**
	 * @param autoLoginIp
	 * @param loginId
	 * @param encryptedPassword
	 */
	public AutoLoginToken(String autoLoginIp, String loginId,
			String encryptedPassword) {
		this.autoLoginIp = autoLoginIp;
		this.loginId = loginId;
		this.encryptedPassword = encryptedPassword;
		this.createDatetime = new Date();
	}	

	/**
	 * @param autoLoginIp
	 * @param loginId
	 * @param encryptedPassword
	 * @param expiredTime
	 */
	public AutoLoginToken(String autoLoginIp, String loginId,
			String encryptedPassword, long expiredTime) {
		this.autoLoginIp = autoLoginIp;
		this.loginId = loginId;
		this.encryptedPassword = encryptedPassword;
		this.createDatetime = new Date();
		this.expiredTime = expiredTime;
	}



	/**
	 * @param autoLoginIp
	 * @param loginId
	 * @param encryptedPassword
	 * @param createDatetime
	 */
	public AutoLoginToken(String autoLoginIp, String loginId,
			String encryptedPassword, Date createDatetime) {
		this.autoLoginIp = autoLoginIp;
		this.loginId = loginId;
		this.encryptedPassword = encryptedPassword;
		this.createDatetime = createDatetime;
	}

	/**
	 * @param autoLoginIp
	 * @param loginId
	 * @param encryptedPassword
	 * @param createDatetime
	 * @param expiredTime
	 */
	public AutoLoginToken(String autoLoginIp, String loginId,
			String encryptedPassword, Date createDatetime, long expiredTime) {
		this.autoLoginIp = autoLoginIp;
		this.loginId = loginId;
		this.encryptedPassword = encryptedPassword;
		this.createDatetime = createDatetime;
		this.expiredTime = expiredTime;
	}
	
	
	/**
	 * 从字符串解析成对象.
	 *
	 * @param token 令牌字符串
	 * @return AutoLoginToken
	 */
	public static AutoLoginToken fromString(String token)
	{
		Assert.notEmpty(token, "Invalid token.");	
		//解密
		try {
			ISecurity security = SecurityFactory.genDES();
			token = security.decrypt(token);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Invalid token.", e);
		} catch (DecryptFailedException e) {
			throw new IllegalArgumentException("Invalid token.", e);
		}		
		
		String[] array = StringUtils.split(token, ';');
		if(array.length!=6)
		{
			throw new IllegalArgumentException("Invalid token.");
		}
		for (int i = 0; i < array.length; i++) {
			Assert.notEmpty(array[i], "Invalid token.");				
		}
		
		String id = array[0];
		String autoLoginIp = array[1];
		String loginId = array[2];
		String encryptedPassword = array[3];
		Date createDatetime = new DateTime(Utility.parseLong(array[4])).toDate();
		long expiredTime = Utility.parseLong(array[5]);
		
		AutoLoginToken ret = new AutoLoginToken(autoLoginIp, loginId, encryptedPassword, createDatetime, expiredTime);
		ret.setId(id);
		
		return ret;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = String.format("%s;%s;%s;%s;%s;%s",
						id, autoLoginIp, loginId, encryptedPassword,
						createDatetime.getTime(), expiredTime);		
		try {
			//使用DES+BASE64加密
			ISecurity security = SecurityFactory.genDES();
			ret = security.encrypt(ret);
			return ret;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Encrypt token failed.", e);
		} catch (EncryptFailedException e) {
			throw new RuntimeException("Encrypt token failed.", e);
		}
		
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the autoLoginIp
	 */
	public String getAutoLoginIp() {
		return autoLoginIp;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @return the encryptedPassword
	 */
	public String getEncryptedPassword() {
		return encryptedPassword;
	}


	/**
	 * @return the createDatetime
	 */
	public Date getCreateDatetime() {
		return createDatetime;
	}


	/**
	 * @return the expiredTime
	 */
	public long getExpiredTime() {
		return expiredTime;
	}

}
