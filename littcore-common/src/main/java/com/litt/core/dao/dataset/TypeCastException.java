package com.litt.core.dao.dataset;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-8-2
 * @version 1.0
 */
public class TypeCastException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	public TypeCastException(Throwable e)
    {
        super(e);
    }

    public TypeCastException(String msg, Throwable e)
    {
        super(msg, e);
    }

    public TypeCastException(Object value, String dataType)
    {
        super(buildMessage(value, dataType));
    }

    
    public TypeCastException(Object value, String dataType, Throwable e)
    {
        super(buildMessage(value, dataType), e);
    }

    private static String buildMessage(Object value, String dataType) {
    	String valueClass = (value==null ? "null" : value.getClass().getName());
    	String message = "Unable to typecast value <" + value + "> of type <" +
    						valueClass + "> to " + dataType;
		return message;
	}


}
