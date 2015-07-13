package com.littcore.lang;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * 
 * Iterator迭代适配器.
 * 
 * <pre><b>描述：</b>
 *    用于将Enumeration转换成Iterator进行迭代
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-9
 * @version 1.0
 *
 */
public class IteratorAdapter implements Iterator
{
    private Enumeration e;

    public IteratorAdapter(Enumeration e) {
        this.e = e;
    }

    public boolean hasNext() {
        return e.hasMoreElements();
    }

    public Object next() {
        if (!e.hasMoreElements()) {
            throw new NoSuchElementException(
                "IteratorAdaptor.next() has no more elements");
        }

        return e.nextElement();
    }

    public void remove() {
        throw new UnsupportedOperationException(
            "Method IteratorAdaptor.remove() not implemented");
    }

}
