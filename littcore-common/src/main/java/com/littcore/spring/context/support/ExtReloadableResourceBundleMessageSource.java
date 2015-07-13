package com.littcore.spring.context.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.littcore.util.StringUtils;


/**
 * ExtReloadableResourceBundleMessageSource.
 * 
 * <pre><b>描述：</b>
 *    支持目录级别的资源文件加载
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2014年1月28日
 * @version 1.0
 */
public class ExtReloadableResourceBundleMessageSource extends
		ReloadableResourceBundleMessageSource {
  
  public static final Logger logger = LoggerFactory.getLogger(ExtReloadableResourceBundleMessageSource.class);
	
	private String[] basepaths = new String[0];	
	
	public void setBasepath(String basepath) {
		setBasepaths(new String[]{basepath});
	}
	
	public void setBasepaths(String... basepaths) {
		this.basepaths = basepaths;
//		List<String> basenameList = new ArrayList<String>();
//	   
//    for(String basepath : basepaths)
//    {      
//      try {
//        Resource[] resources = this.resolver.getResources(basepath);
//        for (Resource resource : resources)
//        {
//          String filename = resource.getFilename();
//          String basename = StringUtils.substringBefore(basepath, "/") + "/" +StringUtils.substringBefore(filename, "_");
//          
//          if(!basenameList.contains(basename))
//          {
//            logger.debug("find message resource:{}", new Object[]{basename});
//            basenameList.add(basename);
//          }
//        }
//      } catch (IOException e) {
//        throw new IllegalArgumentException(e);
//      }
//    }
//    super.setBasenames(basenameList.toArray(new String[0]));
	}

  
  /**
   * @return the basepaths
   */
  public String[] getBasepaths()
  {
    return basepaths;
  }

  /* (non-Javadoc)
   * @see org.springframework.context.support.ReloadableResourceBundleMessageSource#setResourceLoader(org.springframework.core.io.ResourceLoader)
   */
  @Override
  public void setResourceLoader(ResourceLoader resourceLoader)
  {
    super.setResourceLoader(resourceLoader);
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
    List<String> basenameList = new ArrayList<String>();
  
    for(String basepath : basepaths)
    {      
     try {
       Resource[] resources = resolver.getResources(basepath);
       for (Resource resource : resources)
       {
         String filename = resource.getFilename();
         String basename = StringUtils.substringBeforeLast(basepath, "/") + "/" +StringUtils.substringBefore(filename, "_");
       
       if(!basenameList.contains(basename))
       {
         logger.debug("find message resource:{}", new Object[]{basename});
           basenameList.add(basename);
         }
       }
     } catch (IOException e) {
       throw new IllegalArgumentException(e);
     }
    }
    super.setBasenames(basenameList.toArray(new String[0]));
  }


}
