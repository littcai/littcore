package com.littcore.po;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.littcore.util.BeanCopier;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-2-2
 * @version 1.0
 */
public class BasePo implements Serializable {	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * To vo.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 * @return the t
	 */
	public <T> T toVo(Class<T> clazz)
	{
		return BeanCopier.copy(this, clazz);
	}
	
	/**
	 * To vo.
	 *
	 * @param <T> the generic type
	 * @param instance the instance
	 * @return the t
	 */
	public <T> T toVo(T instance)
	{
		return BeanCopier.copy(this, instance);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	

}
