package com.litt.core.util;

import java.io.File;

import java.io.IOException;

import java.util.List;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.litt.core.common.Utility;
import com.litt.core.exception.BusiException;

/**
 *  
 * <b>标题:</b> XML辅助工具类.
 * <pre><b>描述:</b> 
 *   对XML文件进行读、写操作.该类用DOM4J实现，只作为对XML文件的简单操作，如需对XML作遍历等复杂操作的可直接用DOM4J(XPATH需要jaxen包)
 * </pre>  
 * 
 * <pre><b>修改记录：</b>
 * 	 完全重构该工具类，全静态方法
 * 
 * Date:2013-07-17
 * 	1、去除读写XML时抛出的Exception
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-04-28
 * @version 1.0
 * 
 * 
 * @since 2008-09-17
 * @version 2.0
 * 
 * @since 2013-07-17
 * @version 2.1
 *
 */
public class XmlUtils 
{
	private static final Log logger = LogFactory.getLog(XmlUtils.class);
	
	public static final String FORMAT_PREETY = "FORMAT_PREETY";
	
	/**
	 * 避免生成实例.
	 */
	private XmlUtils(){}
	
	/**
	 * 直接解析XML字符串的构造函数.
	 * 
	 * @param xmlContent
	 *            xml字符串
	 * 
	 * @return Document
	 * 
	 * @throws BusiException
	 *             the busi exception
	 */
	public static Document readXml(String xmlContent)
	{
		if(Utility.isEmpty(xmlContent))
            throw new java.lang.IllegalArgumentException("Content can't be empty."); 
        if (logger.isDebugEnabled())
        {
            logger.debug("XML内容 - " + xmlContent);
        } 
        try {
			Document document = DocumentHelper.parseText(xmlContent); 
			return document;
		} catch (DocumentException e) {
			throw new IllegalArgumentException("Illegal xml content.", e);
		} 
	}
	
	/**
	 * 根据文件解析XML.
	 * 
	 * @param file
	 *            xml文件
	 * 
	 * @return Document
	 * 
	 * @throws BusiException
	 *             the busi exception
	 */
	public static Document readXml(File file)
	{
		if(logger.isDebugEnabled())
		{	
			logger.debug("找到配置文件 - "+file.getName());
		}
		SAXReader saxReader = new SAXReader();
		try{
			Document document = saxReader.read(file); 				
			return document;
		} catch (DocumentException e) {
			throw new IllegalArgumentException("Illegal xml file.", e);
		} 
		
	}
	
	/**
	 * 判断节点类型是否为叶子节点
	 * @param element 节点元素
	 * @return boolean
	 */
	public static boolean isLeaf(Element element)
	{
		return element.elements().size()>0;
	}

	/**
	 * 通过XPATH获取节点,如果该节点下有多个同名节点的话取第一个节点
	 * 当前上下文 ./
	 * 		   文档根     /
	 *         根元素     /*
	 *         递归下降   //
	 * 
	 * @param element  为NULL则默认根节点
	 * @param path XPATH路径
	 * @return Element		
	 */
	public static Element getElementByPath(Element element ,String path)
	{
		List list = element.selectNodes(path);
		if(list.size()<=0)
			return null;
		else
			return(Element)list.get(0);			
	}
	
	/**
	 * 通过XPATH获取节点.
	 * 当前上下文 ./
	 * 		   文档根     /
	 *         根元素     /*
	 *         递归下降   //
	 * 
	 * @param element  为NULL则默认根节点
	 * @param path XPATH路径
	 * @return Element		
	 */	
	public static List getElementListByPath(Element element ,String path)
	{
		return element.selectNodes(path);
	}	

	
	/**
	 * 写XML文件(默认UTF-8编码).
	 * 
	 * @param file
	 *            需要更新的XML文件，如存在则覆盖
	 * @param document
	 *            xml文档对象
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public static void writeXml(File file,Document document) throws IOException
	{
		XmlUtils.writeXml(file, document, null, null);
	}
	
	/**
	 * 写XML文件.
	 * 
	 * @param encoding
	 *            编码方式
	 * @param file
	 *            新文件
	 * @param document
	 *            xml文档对象
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public static void writeXml(File file,Document document,String encoding) throws IOException
	{
		XmlUtils.writeXml(file, document, encoding, null);
	}	
	
	/**
	 * 写XML文件.
	 * 
	 * @param encoding
	 *            编码方式
	 * @param file
	 *            新文件
	 * @param formatType
	 *            格式化类型：pretty/其他
	 * @param document
	 *            xml文档对象
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
	public static void writeXml(File file,Document document,String encoding,String formatType) throws IOException
	{
		OutputFormat format = null;
		if(!Utility.isEmpty(formatType)&&"FORMAT_PREETY".equals(formatType))
			format = OutputFormat.createPrettyPrint();
		else
			format = OutputFormat.createCompactFormat();			
		if(!Utility.isEmpty(encoding))
			format.setEncoding(encoding);
		XMLWriter writer = new XMLWriter(new java.io.FileOutputStream(file),format);
		
		writer.write(document);
		writer.close();
	}

	/**
	 * 将XML转换为字符串(默认UTF-8编码).
	 * @param document 文档对象
	 * @return XML字符串
	 * @throws IOException
	 */
	public static String writeXml(Document document)
	{
		return XmlUtils.writeXml(document, null, null);
	}
	
	/**
	 * 将XML转换为字符串.
	 * @param document 文档对象
	 * @param encoding 编码
	 * @return XML字符串
	 * @throws IOException
	 */
	public static String writeXml(Document document,String encoding)
	{
		return XmlUtils.writeXml(document, encoding, null);
	}	
	
	/**
	 * 将XML转换为字符串.
	 * @param document 文档对象
	 * @param encoding 编码
	 * @param formatType 格式化
	 * @return XML字符串
	 * @throws IOException
	 */
	public static String writeXml(Document document,String encoding,String formatType)
	{	
		OutputFormat format = null;
		if(!Utility.isEmpty(formatType)&&"FORMAT_PREETY".equals(formatType))
			format = OutputFormat.createPrettyPrint();
		else
			format = OutputFormat.createCompactFormat();			
		if(!Utility.isEmpty(encoding))
			format.setEncoding(encoding);
		return document.asXML();		
	}	
	
	/**
	 * 格式化XML文件.
	 * 
	 * @param file
	 *            需格式化的XML文件
	 * @param formatType
	 *            格式化类型
	 * 
	 * @throws IOException
	 *             the IO exception
	 * @throws BusiException
	 *             the busi exception
	 */
	public static void formatXml(File file,String formatType) throws Exception
	{
		XmlUtils.formatXml(file,null, formatType);
	}	

	/**
	 * 格式化XML文件.
	 * 
	 * @param file
	 *            需格式化的XML文件
	 * @param encoding
	 *            编码
	 * @param formatType
	 *            格式化类型
	 * 
	 * @throws IOException
	 *             the IO exception
	 * @throws BusiException
	 *             the busi exception
	 */
	public static void formatXml(File file,String encoding,String formatType) throws IOException
	{
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(file);
		} catch (DocumentException e) {
			throw new IllegalArgumentException("Illegal xml file.", e);
		} 
		XmlUtils.writeXml(file,document, encoding,formatType);				
	}	
	
	
	/**
	 * 根据参数名称转换成XPATH
	 * @param key 参数名
	 * @return String XPath
	 */
	public static String parseXPath(String key)
	{
		StringBuffer ret = new StringBuffer("/*/");
		ret.append(key.replaceAll("\\.","/"));		
		return ret.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		File file = new File("D:\\bipData\\config.xml");
		Document document = XmlUtils.readXml(file);
		Element root = document.getRootElement();
		Element index = root.element("index");
		index.setText("中午");
		XmlUtils.writeXml(file,document);
		//XmlUtils.formatXml(file, XmlUtils.FORMAT_PREETY);		
		
		
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		//factory.setValidating(true);
//		factory.setNamespaceAware(false);
//		SAXParser parser = factory.newSAXParser();
//		
//		XMLReader reader = parser.getXMLReader();
//		reader.parse(new InputSource(new FileInputStream(file)));


	}
	
}
