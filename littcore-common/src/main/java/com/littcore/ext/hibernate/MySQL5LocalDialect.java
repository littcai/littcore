package com.littcore.ext.hibernate;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;


/**
 * .
 * 
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2016年8月9日
 * @version 1.0
 */
public class MySQL5LocalDialect extends MySQL5Dialect {
  
  public MySQL5LocalDialect(){  
    super();    
    //增加对MySQL的convert函数的支持
    registerFunction("convert", new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using ?2)") ); 
  }  
}
