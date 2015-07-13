package com.littcore.util;

import com.littcore.util.IPUtils;

import junit.framework.TestCase;

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
 * @since 2011-4-6
 * @version 1.0
 */
public class IPUtilsTest extends TestCase {
	
	public void test_ip2num()
	{
		super.assertEquals(IPUtils.ip2num("192.168.0.1"), 3232235521L);
	}
	
	public void test_num2ip()
	{
		super.assertEquals("192.168.0.1", IPUtils.num2ip(3232235521L));
	}
	
	public void test_netMask2num()
	{
		super.assertEquals(IPUtils.netMask2num("255.255.255.255"), 32);
		super.assertEquals(IPUtils.netMask2num("255.255.255.0"), 24);
		super.assertEquals(IPUtils.netMask2num("255.255.0.0"), 16);
		super.assertEquals(IPUtils.netMask2num("255.0.0.0"), 8);
		super.assertEquals(IPUtils.netMask2num("255.255.192.0"), 18);
	}
	
	public void test_num2NetMask()
	{
		super.assertEquals(IPUtils.num2NetMask(32), "255.255.255.255");
		super.assertEquals(IPUtils.num2NetMask(24), "255.255.255.0");
		super.assertEquals(IPUtils.num2NetMask(18), "255.255.192.0");
	}
	
	public void test_getSubnetAddress()
	{

		super.assertEquals("192.168.1.0/24", IPUtils.getSubnetAddress("192.168.1.1", "255.255.255.0"));
		super.assertEquals("192.168.0.0/16", IPUtils.getSubnetAddress("192.168.1.1", "255.255.0.0"));
		
		super.assertEquals("192.168.1.0/25", IPUtils.getSubnetAddress("192.168.1.1", "255.255.255.128"));
	}
	
	public void test_getSubnetIps()
	{
		String[] ips = IPUtils.getSubnetIps("192.168.1.10", "255.255.255.0");
		assertEquals(254, ips.length);
		assertEquals("192.168.1.1", ips[0]);
		assertEquals("192.168.1.254", ips[253]);
	}
	
	public void test_getSubnetIps_case2()
	{
		String[] ips = IPUtils.getSubnetIps("192.168.1.10", "255.255.0.0");
		assertEquals(65534, ips.length);
		assertEquals("192.168.0.1", ips[0]);
		assertEquals("192.168.255.254", ips[65533]);
	}

}
