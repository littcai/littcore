package com.littcore.web.util;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.littcore.exception.CheckedBusiException;


/**
 * .
 * 
 * <pre><b>描述：</b>
 *    
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
@RunWith(Parameterized.class)
public class XssUtilsTest {
  
  private String input;
  
  private String expected;
  
  @Parameters
  public static Collection prepareData()
  {
      // 测试数据
      Object[][] objects = { 
          { "<div><a href='document.write(document.cookie);'>link</a></div>", "<div><a>link</a></div>" }
          , { "<script>alert('123');</script>", ""}
          , { "<video src='video.mp4'></video>", "<video src='video.mp4'></video>"}
          , { "<span>&nbsp;name</span>", "<span>&nbsp;name</span>"}
          , { "<span style=\"color:#fff\">&nbsp;name</span>", "<span style=\"color:#fff\">&nbsp;name</span>"}
      };
      return Arrays.asList(objects);// 将数组转换成集合返回
  }

  public XssUtilsTest(String input, String expected)
  {
    this.input = input;
    this.expected = expected;
  }
  
  @Test
  public void test_getCleanHtml()
  {
    try
    {
      Assert.assertEquals(this.expected, XssUtils.getCleanHtml(this.input));
    } catch (CheckedBusiException e)
    {
      Assert.fail(e.getMessage());
    }

  }

}
