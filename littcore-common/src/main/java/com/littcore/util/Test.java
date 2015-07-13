package com.littcore.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Test {
	
	static Configuration templateConfig = new Configuration(); 
	
	static StringTemplateLoader templateLoader = new StringTemplateLoader();
	
	static CompiledTemplate compiled = TemplateCompiler.compileTemplate("I'am ${c}, ${d} 1+2");
	
	static{
		templateLoader.putTemplate("abc", "I'am ${c}, ${d} 1+2");
		templateConfig.setTemplateLoader(templateLoader);  
		templateConfig.setDefaultEncoding("UTF-8"); 
		templateConfig.setClassicCompatible(true);	//允许NULL值
	}
	
	public static String format()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("c", "a");
		map.put("d", "b");
		return TemplateRuntime.eval("I'am ${c}, ${d} 1+2", map).toString();
	}
	
	public static String format1()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("c", "a");
		map.put("d", "b");
		return TemplateRuntime.execute(compiled, map).toString();
	}
	
	public static String format2()
	{		
		return StringUtils.format("I'am {}, {} 1+2", new Object[]{"a", "b"});
	}
	
	public static String format3()throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("c", "a");
		map.put("d", "b");

		Template template = templateConfig.getTemplate("abc");
		StringWriter writer = new StringWriter();
		template.process(map, writer);
		return writer.toString();
	}
	
	public static String format4() {
		StringBuilder strb = new StringBuilder();
		int pos = -1;
		int start=0;
		
		String[] args = new String[]{"a", "b"};
		String pattern = "I'am {}, {} 1+2";
		for (int i=0; i<args.length; ++i) {
			pos = pattern.indexOf("{}",start);
			if (pos>0) {
				strb.append(pattern.substring(start, pos));
				start = pos+2;
				strb.append(args[i]);
			} else {
				break;
			}
		}
		strb.append(pattern.substring(start));
		return strb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		System.out.println(format());
		
		long a = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Test.format();
		}
		System.out.println(System.currentTimeMillis()-a);
		
		a = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Test.format1();
		}
		System.out.println(System.currentTimeMillis()-a);
		
		a = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Test.format2();
		}
		System.out.println(System.currentTimeMillis()-a);
		
		a = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Test.format3();
		}
		System.out.println(System.currentTimeMillis()-a);
		
		a = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Test.format4();
		}
		System.out.println(System.currentTimeMillis()-a);
	}

}
