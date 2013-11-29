package com.litt.core.io.file.filter;

import java.io.File;
import java.util.List;

/**
 * 文件名后缀过滤.可匹配单后缀名或多后缀名
 * 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-12-04
 * @version 2.0
 * 
 */
public class SuffixFileFilter extends AbstractFileFilter
{
    private String[] suffixes;
    
    public SuffixFileFilter(String suffix) {
	if (suffix == null)
	    throw new IllegalArgumentException("The suffix must not be null");
	suffixes = new String[] { suffix };
    }
    
    public SuffixFileFilter(String[] suffixes) {
	if (suffixes == null)
	    throw new IllegalArgumentException
		      ("The array of suffixes must not be null");
	this.suffixes = suffixes;
    }
    
    public SuffixFileFilter(List suffixes) {
	if (suffixes == null)
	    throw new IllegalArgumentException
		      ("The list of suffixes must not be null");
	this.suffixes
	    = (String[]) suffixes.toArray(new String[suffixes.size()]);
    }
    
    public boolean accept(File file) {
	String name = file.getName();
	for (int i = 0; i < suffixes.length; i++) {
	    if (name.endsWith(suffixes[i]))
		return true;
	}
	return false;
    }
    
    public boolean accept(File file, String name) {
	for (int i = 0; i < suffixes.length; i++) {
	    if (name.endsWith(suffixes[i]))
		return true;
	}
	return false;
    }
}
