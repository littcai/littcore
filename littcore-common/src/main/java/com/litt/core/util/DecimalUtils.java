package com.litt.core.util;

import java.math.BigDecimal;

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
 * @since 2012-7-18
 * @version 1.0
 */
public abstract class DecimalUtils {
	
	private static final int DEFAULT_SCALE = 2;	//默认保留的小数位,百分比则为百分比的小数位数
	
	/**
	 * 精度调整(默认两位小数，四舍五入).
	 * @param value
	 * @param scale
	 * @return
	 */
	public static float round(float value)
	{
		return round(value, DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 精度调整(四舍五入).
	 * @param value
	 * @param scale
	 * @return
	 */
	public static float round(float value, int scale)
	{
		return round(value, scale, BigDecimal.ROUND_HALF_UP);
	}	
	
	/**
	 * 精度调整.
	 * @param value
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public static float round(float value, int scale, int roundingMode)
	{
		BigDecimal decimal = new BigDecimal(value);
		decimal = decimal.setScale(scale, roundingMode);
		return decimal.floatValue();
	}
	
	/**
	 * 精度调整(默认两位小数，四舍五入).
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double round(double value)
	{
		return round(value, DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 精度调整(四舍五入).
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double round(double value, int scale)
	{
		return round(value, scale, BigDecimal.ROUND_HALF_UP);
	}	
	
	/**
	 * 精度调整.
	 * @param value
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public static double round(double value, int scale, int roundingMode)
	{
		BigDecimal decimal = new BigDecimal(value);
		decimal = decimal.setScale(scale, roundingMode);
		return decimal.doubleValue();
	}

}
