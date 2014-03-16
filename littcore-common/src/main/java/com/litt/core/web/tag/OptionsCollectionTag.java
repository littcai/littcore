package com.litt.core.web.tag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;

import com.litt.core.lang.IteratorAdapter;

/**
 * 
 * 
 * Options集合处理标签.
 * 
 * <pre><b>描述：</b>
 *    用于在创建Options时对集合对象进行迭代操作
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
public class OptionsCollectionTag extends BodyTagSupport implements IterationTag
{	
	
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 循环体的变量.
	 */
	private String var;
	
	/**
	 * 集合对象.
	 */
	private Object collection;	
	
	private String value;

	private Iterator iter;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException
	{
		iter = getIterator(this.collection); 
		if(iter==null || !iter.hasNext())
			return SKIP_BODY;
		else
		{
			Object bean = iter.next();
        	pageContext.setAttribute(var, bean);
			return EVAL_BODY_INCLUDE;
		}
			
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
	public int doAfterBody() throws JspException
	{        
        if(iter.hasNext())
        {
        	Object bean = iter.next();
        	pageContext.setAttribute(var, bean);
        	return EVAL_BODY_AGAIN;
        }        
		return SKIP_BODY;
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException
	{

		pageContext.removeAttribute(var);
		return super.doEndTag();
	}
	

	
    /**
     * Return an iterator for the options collection.
     *
     * @param collection Collection to be iterated over
     * @throws JspException if an error occurs
     */
    protected Iterator getIterator(Object collection)
        throws JspException {
    	if(collection==null)
    		return null;    	
        if (collection.getClass().isArray()) {
            collection = Arrays.asList((Object[]) collection);
        }

        if (collection instanceof Collection) {
            return (((Collection) collection).iterator());
        } else if (collection instanceof Iterator) {
            return ((Iterator) collection);
        } else if (collection instanceof Map) {
            return (((Map) collection).entrySet().iterator());
        } else if (collection instanceof Enumeration) {
            return new IteratorAdapter((Enumeration) collection);
        } else {
            throw new JspException("collection不是一个合法的集合对象！");
        }
    }

	/**
	 * @return the collection
	 */
	public Object getCollection()
	{
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Object collection)
	{
		this.collection = collection;
	}

	/**
	 * @return the var
	 */
	public String getVar()
	{
		return var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var)
	{
		this.var = var;
	}


	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

}
