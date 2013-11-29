package com.litt.core.io.file.filter;

import java.io.File;
import java.util.List;

/**
 * 文件名过滤.可匹配文件名开头字符串
 * 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-12-04
 * @version 1.0
 * 
 */
public class PrefixFileFilter extends AbstractFileFilter
{
    private String[] prefixes;
    
    public PrefixFileFilter(String prefix) {
	if (prefix == null)
	    throw new IllegalArgumentException("The prefix must not be null");
	prefixes = new String[] { prefix };
    }
    
    public PrefixFileFilter(String[] prefixes) {
	if (prefixes == null)
	    throw new IllegalArgumentException
		      ("The array of prefixes must not be null");
	this.prefixes = prefixes;
    }
    
    public PrefixFileFilter(List prefixes) {
	if (prefixes == null)
	    throw new IllegalArgumentException
		      ("The list of prefixes must not be null");
	this.prefixes
	    = (String[]) prefixes.toArray(new String[prefixes.size()]);
    }
    
    public boolean accept(File file) {
	String name = file.getName();
	for (int i = 0; i < prefixes.length; i++) {
	    if (name.startsWith(prefixes[i]))
		return true;
	}
	return false;
    }
    
    public boolean accept(File file, String name) {
	for (int i = 0; i < prefixes.length; i++) {
	    if (name.startsWith(prefixes[i]))
		return true;
	}
	return false;
    }
}
