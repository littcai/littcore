package com.litt.core.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public interface IOFileFilter extends FileFilter, FilenameFilter 
{
    public boolean accept(File file);
    
    public boolean accept(File file, String string);
}
