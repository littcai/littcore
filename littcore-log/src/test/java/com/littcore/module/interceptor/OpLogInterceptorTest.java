
package com.littcore.module.interceptor;

import org.junit.Test;

import com.littcore.module.interceptor.OpLogInterceptor;


public class OpLogInterceptorTest {
	
	@Test
	public void test_destory(){
		OpLogInterceptor inceptor = new OpLogInterceptor();
		inceptor.init();
		inceptor.destroy();
		
	}

}
