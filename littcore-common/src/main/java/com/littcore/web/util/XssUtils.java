package com.littcore.web.util;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.littcore.exception.CheckedBusiException;


/**
 * XSS辅助类.
 * 
 * <pre><b>描述：</b>
 *    用于对HTML进行XSS过滤，方式XSS攻击。用于输入输出均为HTML的情况
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2015年12月1日
 * @version 1.0
 */
public abstract class XssUtils {
  
  private static Policy policy;  
  private static AntiSamy as;
  
  static {
    try
    {
      policy = Policy.getInstance(XssUtils.class.getResourceAsStream("/antisamy-html.xml"));
      as = new AntiSamy();
    } catch (PolicyException e)
    {
      throw new RuntimeException("Initialize XssUtils failed", e);
    }
  }
  
  /**
   * 获得纯净的HTML代码.
   *
   * @param html the html
   * @return the clean html
   * @throws CheckedBusiException the checked busi exception
   */
  public static String getCleanHtml(String html) throws CheckedBusiException
  {
    try
    {
      CleanResults results = as.scan(html, policy);
      return results.getCleanHTML();
    } catch (ScanException e)
    {
      throw new CheckedBusiException(e);
    } catch (PolicyException e)
    {
      throw new CheckedBusiException(e);
    }
  }

}
