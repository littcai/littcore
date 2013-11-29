package com.litt.core.web.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.beanutils.PropertyUtils;

import com.litt.core.lang.IteratorAdapter;


/** 
 * 
 * TODO.
 * 
 * <pre><b>描述：</b>
 *    TODO 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    TODO
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-9-24
 * @version 1.0
 *
 */
public class OptionsTag extends SimpleTagSupport
{	
	/**
	 * 集合对象.
	 */
	private Object collection;
	
	/**
	 * 属性名称.
	 */
	private String property;
	
	/**
	 * 标签显示属性名称.
	 */
	private String label;
	
	/**
	 * 选项值.
	 */
	private String value;
		
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException, IOException
	{		
		super.doTag();
		JspContext context = this.getJspContext();
		JspWriter out = context.getOut();      
		
		//Acquire the collection containing our options
        Iterator iter = getIterator(this.collection);     
		
        StringBuffer sb = new StringBuffer(200);		
		
		while(iter.hasNext())
		{
			Object bean = iter.next();
            Object beanLabel = null;
            Object beanProperty = null;

            // Get the label for this option
            try {
                beanLabel = PropertyUtils.getProperty(bean, label);

                if (beanLabel == null) {
                    beanLabel = "";
                }
            } catch (IllegalAccessException e) {                 
                throw new JspException(e);
            } catch (InvocationTargetException e) {
            	throw new JspException(e);
            } catch (NoSuchMethodException e) {
            	throw new JspException(e);
            }
			
            //Get the value for this option
            try {
            	beanProperty = PropertyUtils.getProperty(bean, this.property);

                if (beanProperty == null) {
                	beanProperty = "";
                }
            } catch (IllegalAccessException e) {                 
                throw new JspException(e);
            } catch (InvocationTargetException e) {
            	throw new JspException(e);
            } catch (NoSuchMethodException e) {
            	throw new JspException(e);
            }
            
            String stringLabel = beanLabel.toString();
            String stringProperty = beanProperty.toString();
            this.addOption(sb, stringLabel, stringProperty);
			
		}
		out.println(sb.toString());
	}
	
	protected void addOption(StringBuffer sb, String label, String property) 
	{
	    sb.append("<option value=\"");
	    sb.append(property);	        
	    sb.append('"');
	        
	    if (property.equals(value)) {
	        sb.append(" selected=\"selected\"");
	    }
	    
        sb.append('>');
        sb.append(label);	       
        sb.append("</option>\r\n");
	}
	
    /**
     * Return an iterator for the options collection.
     *
     * @param collection Collection to be iterated over
     * @throws JspException if an error occurs
     */
    protected Iterator getIterator(Object collection)
        throws JspException {
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
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return the property
	 */
	public String getProperty()
	{
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property)
	{
		this.property = property;
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
