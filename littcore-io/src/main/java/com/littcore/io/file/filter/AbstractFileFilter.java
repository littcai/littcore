package com.littcore.io.file.filter;

import java.io.File;


public abstract class AbstractFileFilter implements IOFileFilter 
{

	 public boolean accept(File file) {
		return accept(file.getParentFile(), file.getName());
	}

	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}

}
