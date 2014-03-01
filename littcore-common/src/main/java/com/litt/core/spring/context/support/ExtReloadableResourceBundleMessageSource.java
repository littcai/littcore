package com.litt.core.spring.context.support;

import java.io.File;
import java.io.IOException;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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
	
	private String[] basepaths = new String[0];
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	public void setBasepaths(String basepath) {
		setBasepaths(new String[]{basepath});
	}

	public void setBasepaths(String[] basepaths) {
		this.basepaths = basepaths;
		for(String basepath : basepaths)
		{
			Resource resource = resourceLoader.getResource(basepath);
			try {
				File pahtFile = resource.getFile();
				if(!pahtFile.exists() || !pahtFile.isDirectory())
				{
					throw new IllegalArgumentException("wrong basepath:"+basepath);
				}
				
				
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	
	

}
