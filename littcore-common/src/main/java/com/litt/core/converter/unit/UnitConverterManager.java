package com.litt.core.converter.unit;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.litt.core.common.Utility;



/**
 * UnitConverterManager.
 * 
 * <pre><b>Descr:</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog:</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Caiyuan</a>
 * @since 2014年9月25日
 * @version 1.0
 */
public class UnitConverterManager {
  
  public Map<String, BigDecimal> converterMap = new HashMap<String, BigDecimal>();
  
  public UnitConverterManager()
  {
    init();
  }
  
  public void init()
  {
    SAXReader saxReader = new SAXReader();
    try
    {
      InputStream input = this.getClass().getResourceAsStream("/UnitConverterConfig.xml");
      Document document = saxReader.read(input); 
      Element root = document.getRootElement();
      List<Element> elementList = root.elements();
      for (Element element : elementList)
      {
        String from = element.attributeValue("from");
        String to = element.attributeValue("to");
        BigDecimal rate = Utility.parseBigDecimal(element.attributeValue("rate"), null);
        converterMap.put(from+"-"+to, rate);
      }
    } catch (DocumentException e)
    {
      throw new RuntimeException("Can't parse UnitConverterConfig.xml");
    }
  }  
  
  public BigDecimal getConverterRate(String from, String to)
  {
    return converterMap.get(from+"-"+to);
  }
  
  private static class SingletonClassInstance { 
    private static final UnitConverterManager instance = new UnitConverterManager(); 
  } 

/**
 * 
 * @return
 */
public static UnitConverterManager getInstance() { 
    return SingletonClassInstance.instance; 
}


}
