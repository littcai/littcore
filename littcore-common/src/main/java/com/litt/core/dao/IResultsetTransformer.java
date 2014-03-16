package com.litt.core.dao;

import java.util.List;

/**
 * Resultset Transformer.
 * 
 * <pre><b>Description：</b>
 * 		  结果集转换 
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-5-8
 * @version 1.0
 */
public interface IResultsetTransformer {
	
		public List transform(List srcList);

}
