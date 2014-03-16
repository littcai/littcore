package com.litt.core.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;


/** 
 * 
 * Option生成标签.
 * 
 * <pre><b>描述：</b>
 *    用于生成单个的Option，需与OptionsCollectionTag配合使用
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-9-24
 * @version 1.0
 *
 */
public class OptionTag extends BodyTagSupport
{
	
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -3487939925200301310L;
	/**
	 * 属性名称.
	 */
	private String property;
	
	private boolean selected;
	

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
	public int doEndTag() throws JspException
	{
		OptionsCollectionTag parentTag = (OptionsCollectionTag)super.getParent();	
		if(parentTag == null)
			throw new JspException("未指定父OptionsCollection标签！");
		
		String value = parentTag.getValue();
		BodyContent bodyContent = this.getBodyContent();
		String content = bodyContent.getString();		
		JspWriter out = pageContext.getOut();     
		
		StringBuffer sb = new StringBuffer(200);	

        this.addOption(sb, content, this.property, value);
        try
		{
			out.println(sb.toString());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   

        return EVAL_BODY_INCLUDE;
	}


	protected void addOption(StringBuffer sb, String label, String property, String value) 
	{
	    sb.append("<option value=\"");
	    sb.append(property);	        
	    sb.append('"');
	        
	    if (selected || property.equals(value)) {
	        sb.append(" selected=\"selected\"");
	    }
	    
        sb.append('>');
        sb.append(label);	       
        sb.append("</option>\r\n");
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
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
