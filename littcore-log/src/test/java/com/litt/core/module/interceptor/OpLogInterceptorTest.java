
package com.litt.core.module.interceptor;

import org.junit.Test;


public class OpLogInterceptorTest {
	
	@Test
	public void test_destory(){
		OpLogInterceptor inceptor = new OpLogInterceptor();
		inceptor.init();
		inceptor.destroy();
		
	}

}
