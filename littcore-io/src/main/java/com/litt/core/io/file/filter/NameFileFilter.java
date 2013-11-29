package com.litt.core.io.file.filter;

import java.io.File;
import java.util.List;

public class NameFileFilter extends AbstractFileFilter {
    private String[] names;
    
    public NameFileFilter(String name) {
	if (name == null)
	    throw new IllegalArgumentException("The name must not be null");
	names = new String[] { name };
    }
    
    public NameFileFilter(String[] names) {
	if (names == null)
	    throw new IllegalArgumentException
		      ("The array of names must not be null");
	this.names = names;
    }
    
    public NameFileFilter(List names) {
	if (names == null)
	    throw new IllegalArgumentException
		      ("The list of names must not be null");
	this.names = (String[]) names.toArray(new String[names.size()]);
    }
    
    public boolean accept(File file) {
	String name = file.getName();
	for (int i = 0; i < names.length; i++) {
	    if (name.equals(names[i]))
		return true;
	}
	return false;
    }
    
    public boolean accept(File file, String name) {
	for (int i = 0; i < names.length; i++) {
	    if (name.equals(names[i]))
		return true;
	}
	return false;
    }
}
