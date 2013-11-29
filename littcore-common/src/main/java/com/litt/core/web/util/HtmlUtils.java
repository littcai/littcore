package com.litt.core.web.util;


import com.litt.core.util.StringUtils;
import com.litt.core.util.ValidateUtils;

/** 
 * 
 * HTML辅助工具类.
 * 
 * <pre><b>描述：</b>
 * 处理以下字符类型转义：
 *  1、HTML 特殊字符； 
 *	2、JavaScript 特殊字符； 
 *	3、SQL 特殊字符；
 *
 * HTML 特殊字符转义	
	HTML 中 <，>，& 等字符有特殊含义，它们是 HTML 语言的保留字，因此不能直接使用。使用这些个字符时，应使用它们的转义序列：	
	& ：&amp; 
	" ：&quot; 
	< ：&lt; 
	> ：&gt; 

* JavaScript 特殊字符转义
	JavaScript 中也有一些需要特殊处理的字符，如果直接将它们嵌入 JavaScript 代码中，JavaScript 程序结构将会遭受破坏，甚至被嵌入一些恶意的程序。下面列出了需要转义的特殊 JavaScript 字符：
	
	' ：\' 
	" ：\" 
	\ ：\\ 
	走纸换页： \f 
	换行：\n 
	换栏符：\t 
	回车：\r 
	回退符：\b 

 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-12-05
 * @version 1.0
 *
 */
public abstract class HtmlUtils extends org.springframework.web.util.HtmlUtils
{		
	
	/**
	 * 脚本清理.
	 * 1、所有<script>标签将转换为文本输入.
	 * 2、超链href支持标签
	 * 
	 * @param content 原HTML字符串
	 * @return 受支持的HTML标签可输出，脚本标签不输出
	 */
	public static String escapeScript(String content)
	{
		if(ValidateUtils.isEmpty(content))
			return "";		
		else
		{         
            content = StringUtils.replace(content, "<script", "&ltscript" );
            content = StringUtils.replace(content, "</script", "&lt/script");
            content = StringUtils.replace(content, "href *= *[\\s\\S]*script *:", "");	//过滤所有<a>标签中的href中的脚本
            content = StringUtils.replace(content, " on[\\s\\S]*=", "");					//过滤所有on事件
            content = StringUtils.replace(content, "<iframe[\\s\\S]+</iframe *>", "");	//过滤所有<iframe>标签
            content = StringUtils.replace(content, "<frameset[\\s\\S]+</frameset *>", "");	//过滤所有<frameset>标签
            content = StringUtils.replace(content, "<marquee", "&ltmarquee");
            content = StringUtils.replace(content, "</marquee", "&lt/marquee");  
            return content; 			
			//return string.replaceAll("<script[^>]*>([\\s\\S](?!<script))*?</script>", "");	//FIXME 该表达式不支持大小写
		}		
	}	
	
	public static void main(String[] args) {
        String specialStr = "<div id=\"testDiv\">test1;test2</div>";
        String str1 = HtmlUtils.htmlEscape(specialStr); //①转换为HTML转义字符表示
        System.out.println(str1);
        System.out.println(HtmlUtils.htmlEscape(str1));
       
        String str2 = HtmlUtils.htmlEscapeDecimal(specialStr); //②转换为数据转义表示
        System.out.println(str2);
       
        String str3 = HtmlUtils.htmlEscapeHex(specialStr); //③转换为十六进制数据转义表示
        System.out.println(str3);
       
        //④下面对转义后字符串进行反向操作
        System.out.println(HtmlUtils.htmlUnescape(str1));
        System.out.println(HtmlUtils.htmlUnescape(str2));
        System.out.println(HtmlUtils.htmlUnescape(str3));
    }

}
