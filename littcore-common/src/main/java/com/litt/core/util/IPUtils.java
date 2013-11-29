package com.litt.core.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.litt.core.common.Utility;


/** 
 * 
 * IP辅助类.
 * 
 * <pre><b>描述：</b>
 *     
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 	2011-02-28: 1、增加IP地址转换字节方法   
 *  2012-08-31: 1、增加通过子网掩码计算有效IP地址数量的方法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-7-7
 * @version 1.0
 *
 */
public class IPUtils
{    
	
	/**
	 * 将标准格式IP转换为数字
	 * @param ip IP地址
	 * @return
	 */
	public static long ip2num(String ip) 
	{ 
		if(!ValidateUtils.isIp(ip))
			throw new java.lang.IllegalArgumentException("IP地址格式不正确！");
	    long ipNum = 0; 
	    String ips[] = ip.split("[.]"); 
	    for (int i = 0; i < ips.length; i++) 
	    { 
	    	int k = Integer.parseInt(ips[i]); 
	        ipNum = ipNum + k * (1L << ((3 - i) * 8)); 
	    } 
	     	   
	    return ipNum; 
	} 
	
	/**
	 * 将数字格式IP转换为标志格式字符串
	 * @param longIp
	 * @return
	 */
	public static String num2ip(long longIp) 
	{ 
	    StringBuffer sb = new StringBuffer(15); 
	    // 直接右移24位 
	    sb.append(String.valueOf((longIp >>> 24))); 
	    sb.append('.'); 
	    // 将高8位置0，然后右移16位 
	    sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16)); 
	    sb.append('.'); 
	    // 将高16位置0，然后右移8位 
	    sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8)); 
	    sb.append('.'); 
	    // 将高24位置0 
	    sb.append(String.valueOf((longIp & 0x000000FF))); 
	    return sb.toString(); 
	} 
	
	/**
	 * 将标准格式子网掩码转换为数字
	 * @param ip IP地址
	 * @return
	 */
	public static int netMask2num(String netMask) 
	{ 
		if(!ValidateUtils.isIp(netMask))
			throw new java.lang.IllegalArgumentException("子网掩码地址格式不正确！");
	    String ips[] = StringUtils.split(netMask, '.'); 
	    String binMask = "";
	    for (int i = 0; i < ips.length; i++) 
	    { 
	    	binMask += Integer.toBinaryString(Integer.parseInt(ips[i])); 
	    } 	   
	    return binMask.lastIndexOf("1")+1; 
	} 
	
	/**
	 * 将数字格式IP转换为标志格式字符串
	 * @param longIp
	 * @return
	 */
	public static String num2NetMask(int netMask) 
	{ 
		int mask = -1 << (32 - netMask);
		int partsNum = 4;
		int bitsOfPart = 8;
		int maskParts[] = new int[partsNum];
		int selector = 0x000000ff;

		for (int i = 0; i < maskParts.length; i++) {
			int pos = maskParts.length - 1 - i;
			maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
		}

		StringBuffer result = new StringBuffer(15);
		result.append(maskParts[0]);
		for (int i = 1; i < maskParts.length; i++) {
			result.append(".").append(maskParts[i]);
		}
		return result.toString();
	}
	
	/**
	 * Gets the subnet address.
	 *
	 * @param ip the ip
	 * @param netMask the net mask
	 * @return the subnet address
	 */
	public static String getSubnetAddress(String ip, String netMask)
	{
		byte[] ipRaw = IPUtils.toIpAddrBytes(ip);
		byte[] maskRaw = IPUtils.toIpAddrBytes(netMask);
		int unsignedByteFilter = 0x000000ff;
		int[] resultRaw = new int[ipRaw.length];
		for (int i = 0; i < resultRaw.length; i++) {
			resultRaw[i] = (ipRaw[i] & maskRaw[i] & unsignedByteFilter);
		}

		// make result string
		StringBuffer result = new StringBuffer(15);
		result.append(resultRaw[0]);
		for (int i = 1; i < resultRaw.length; i++) {
			result.append(".").append(resultRaw[i]);
		}
		result.append("/").append(IPUtils.netMask2num(netMask));
		return result.toString();
	}	
    
    /**
     * 根据子网地址和子网掩码计算可用IP地址.
     * 去掉网络号、广播地址
     * 
     * @param subnetAddress 子网任意IP地址
     * @param subnetMask 子网掩码
     * @return
     */
    public static String[] getSubnetIps(String subnetAddress, String subnetMask)
    {
    	int totalNum = getSubnetIpSize(subnetMask);
    	
    	byte[] ipRaw = IPUtils.toIpAddrBytes(subnetAddress);
		byte[] maskRaw = IPUtils.toIpAddrBytes(subnetMask);
		int unsignedByteFilter = 0x000000ff;
		int[] resultRaw = new int[ipRaw.length];
		long subnetNo = 0;
		for (int i = 0; i < resultRaw.length; i++) {
			resultRaw[i] = (ipRaw[i] & maskRaw[i] & unsignedByteFilter);			
			subnetNo = subnetNo + resultRaw[i] * (1L << ((3 - i) * 8)); 			
		}
		
		String[] ips = new String[totalNum];
		for (int i = 0; i < totalNum; i++) {
			ips[i] = num2ip(subnetNo + 1 + i);
		}
		return ips;
    }

	/**
	 * 获得子网有效IP地址数量.
	 * 去掉网络号、广播地址
	 * 
	 * @param subnetMask 子网掩码
	 * @return
	 */
	public static int getSubnetIpSize(String subnetMask) {
		int numMask = netMask2num(subnetMask);
    	int totalNum = (1 << (32 - numMask)) - 2;
		return totalNum;
	}
	
	/**
	 * 缩写的MAC地址(无冒号)转换为全地址.
	 * @param mac
	 * @return
	 */
	public static String mac2Full(String mac)
	{
		String[] array = Utility.splitStringAll(mac, 2);
		StringBuilder sb = new StringBuilder(20);
		for (String string : array) {
			sb.append(string).append(":");
		}
		return sb.toString();
	}
	
	/**
	 * 检查IP是否在指定的IP段中.
	 * 
	 * @param startIp 起始IP
	 * @param endIp 结束IP
	 * @param ip IP地址
	 * 
	 * @return true, if checks is between
	 */
	public static boolean isBetween(String startIp,String endIp, String ip)
	{
		if(!ValidateUtils.isIp(startIp))
			throw new java.lang.IllegalArgumentException("起始IP地址格式不正确！");
		if(!ValidateUtils.isIp(endIp))
			throw new java.lang.IllegalArgumentException("结束IP地址格式不正确！");
		if(!ValidateUtils.isIp(ip))
			throw new java.lang.IllegalArgumentException("IP地址格式不正确！");
		
		long startIpNum = ip2num(startIp);
		long endIpNum = ip2num(endIp);
		long ipNum = ip2num(ip);
		
		return ipNum>=startIpNum && ipNum<=endIpNum;
	}
	
	/**
	 * 检查IP是否在指定的IP段中.
	 * 
	 * @param startIp 起始IP
	 * @param endIp 结束IP
	 * @param ip IP地址
	 * 
	 * @return true, if checks is between
	 */
	public static boolean isInDomain(String domainIp, String ip)
	{			
		String startIp = domainIp.replaceAll("[*]", "0");
		String endIp = domainIp.replaceAll("[*]", "255");
		
		if(startIp.length() > 0 && startIp.charAt(0) == '0')	//IP不能以0开头
			startIp = startIp.replaceFirst("0", "1");			
		
		if(!ValidateUtils.isIp(startIp))
			throw new java.lang.IllegalArgumentException("起始IP地址格式不正确！");
		if(!ValidateUtils.isIp(endIp))
			throw new java.lang.IllegalArgumentException("结束IP地址格式不正确！");
		if(!ValidateUtils.isIp(ip))
			throw new java.lang.IllegalArgumentException("IP地址格式不正确！");
		
		long startIpNum = ip2num(startIp);
		long endIpNum = ip2num(endIp);
		long ipNum = ip2num(ip);
		
		return ipNum>=startIpNum && ipNum<=endIpNum;
	}	
	
    /**
     * <p>getInetAddress</p>
     *
     * @param ipAddrOctets an array of byte.
     * @return a {@link java.net.InetAddress} object.
     */
    public static InetAddress getInetAddress(byte[] ipAddrOctets) {
        try {
            return InetAddress.getByAddress(ipAddrOctets);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IPAddress "+ipAddrOctets+" with length "+ipAddrOctets.length);
        }        
    }
    
    /**
     * <p>getInetAddress</p>
     *
     * @param dottedNotation a {@link java.lang.String} object.
     * @return a {@link java.net.InetAddress} object.
     */
    public static InetAddress getInetAddress(String dottedNotation) {
        try {
            return InetAddress.getByName(dottedNotation);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IPAddress "+dottedNotation);
        }
    }

    
    /**
     * <p>toIpAddrBytes</p>
     *
     * @param dottedNotation a {@link java.lang.String} object.
     * @return an array of byte.
     */
    public static byte[] toIpAddrBytes(String dottedNotation) {
        return getInetAddress(dottedNotation).getAddress();
    }

    /**
     * <p>toIpAddrString</p>
     *
     * @param addr an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String toIpAddrString(byte[] addr) {
        return getInetAddress(addr).getHostAddress();
    }

    /**
     * Given a list of IP addresses, return the lowest as determined by the
     * numeric representation and not the alphanumeric string.
     *
     * @param addresses a {@link java.util.List} object.
     * @return a {@link java.net.InetAddress} object.
     */
    public static InetAddress getLowestInetAddress(List<InetAddress> addresses) {
        if (addresses == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
    
        InetAddress lowest = null;
        byte[] lowestBytes = new byte[] { 0, 0, 0, 0 };
        ByteArrayComparator comparator = new ByteArrayComparator();
        for (InetAddress temp : addresses) {
            byte[] tempBytes = temp.getAddress();
    
            if (comparator.compare(tempBytes, lowestBytes) < 0) {
                lowestBytes = tempBytes;
                lowest = temp;
            }
        }
    
        return lowest;
    }

    public static boolean isInetAddressInRange(final String addrString, final String beginString, final String endString) {
        final ByteArrayComparator comparator = new ByteArrayComparator();
        final byte[] addr = IPUtils.toIpAddrBytes(addrString);
        final byte[] begin = IPUtils.toIpAddrBytes(beginString);
        if (comparator.compare(addr, begin) > 0) {
            final byte[] end = IPUtils.toIpAddrBytes(endString);
            return comparator.compare(addr, end) <= 0;
        } else if (comparator.compare(addr, begin) == 0) {
            return true;
        } else { 
            return false;
        }
    }

    public static boolean isInetAddressInRange(final byte[] addr, final byte[] begin, final byte[] end) {
        final ByteArrayComparator comparator = new ByteArrayComparator();
        if (comparator.compare(addr, begin) > 0) {
            return comparator.compare(addr, end) <= 0;
        } else if (comparator.compare(addr, begin) == 0) {
            return true;
        } else { 
            return false;
        }
    }
    
    public static InetAddress convertBigIntegerIntoInetAddress(BigInteger i) throws UnknownHostException {
        if (i.compareTo(new BigInteger("0")) < 0) {
            throw new IllegalArgumentException("BigInteger is negative, cannot convert into an IP address: " + i.toString());
        } else {
            // Note: This function will return the two's complement byte array so there will always
            // be a bit of value '0' (indicating positive sign) at the first position of the array
            // and it will be padded to the byte boundry. For example:
            //
            // 255.255.255.255 => 00 FF FF FF FF (5 bytes)
            // 127.0.0.1 => 0F 00 00 01 (4 bytes)
            //
            byte[] bytes = i.toByteArray();

            if (bytes.length == 0) {
                return InetAddress.getByAddress(new byte[] {0, 0, 0, 0});
            } else if (bytes.length <= 4) {
                // This case covers an IPv4 address with the most significant bit of zero (the MSB
                // will be used as the two's complement sign bit)
                byte[] addressBytes = new byte[4];
                int k = 3;
                for (int j = bytes.length - 1; j >= 0; j--, k--) {
                    addressBytes[k] = bytes[j];
                }
                return InetAddress.getByAddress(addressBytes);
            } else if (bytes.length <= 5 && bytes[0] == 0) {
                // This case covers an IPv4 address (4 bytes + two's complement sign bit of zero)
                byte[] addressBytes = new byte[4];
                int k = 3;
                for (int j = bytes.length - 1; j >= 1; j--, k--) {
                    addressBytes[k] = bytes[j];
                }
                return InetAddress.getByAddress(addressBytes);
            } else if (bytes.length <= 16) {
                // This case covers an IPv6 address with the most significant bit of zero (the MSB
                // will be used as the two's complement sign bit)
                byte[] addressBytes = new byte[16];
                int k = 15;
                for (int j = bytes.length - 1; j >= 0; j--, k--) {
                    addressBytes[k] = bytes[j];
                }
                return InetAddress.getByAddress(addressBytes);
            } else if (bytes.length <= 17 && bytes[0] == 0) {
                // This case covers an IPv6 address (16 bytes + two's complement sign bit of zero)
                byte[] addressBytes = new byte[16];
                int k = 15;
                for (int j = bytes.length - 1; j >= 1; j--, k--) {
                    addressBytes[k] = bytes[j];
                }
                return InetAddress.getByAddress(addressBytes);
            } else {
                throw new IllegalArgumentException("BigInteger is too large to convert into an IP address: " + i.toString());
            }
        }
    }

    
}
