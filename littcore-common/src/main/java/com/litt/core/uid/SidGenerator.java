package com.litt.core.uid;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



/**
 * 6位ID生成器.
 * 
 * <pre><b>描述：</b>
 *  短网址生成算法，有可能重复
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *  2015-05-31 改成短网址生成算法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2015-05-31
 * @version 1.0
 */
public class SidGenerator
{
  static char hexChar[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8' , '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  //要使用生成 URL 的字符
  private static String[] CHAR_64_BITS = new String[] { "a" , "b" , "c" , "d" , "e" , "f" , "g" , "h" ,
    "i" , "j" , "k" , "l" , "m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" ,
    "u" , "v" , "w" , "x" , "y" , "z" , "0" , "1" , "2" , "3" , "4" , "5" ,
    "6" , "7" , "8" , "9" , "A" , "B" , "C" , "D" , "E" , "F" , "G" , "H" ,
    "I" , "J" , "K" , "L" , "M" , "N" , "O" , "P" , "Q" , "R" , "S" , "T" ,
    "U" , "V" , "W" , "X" , "Y" , "Z"
  };
  
  public static String getId(){
    String hex = UUID.randomUUID().toString();   
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < 1; i++) {
      //把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
      String sTempSubString = hex.substring(i * 8, i * 8 + 8);

      //这里需要使用 long 型来转换，因为 Integer .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用 long ，则会越界
      long lHexLong = 0x3FFFFFFF & Long.parseLong (sTempSubString, 16);
      for (int j = 0; j < 6; j++) {
        //把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
        long index = 0x0000003D & lHexLong;
        //把取得的字符相加
        sb.append(CHAR_64_BITS[( int ) index]);
        //每次循环按位右移 5 位
        lHexLong = lHexLong >> 5;
      }
    }
    return sb.toString();
  }
  	
	/**
	 * 禁止外部生成实例类.
	 */
	private SidGenerator(){};
	
	
	public static void main(String args[]) {
	  Map<String, String> map = new HashMap<String, String>();
		for(int i=0;i<10000;i++) {
			String id = SidGenerator.getId();
			map.put(id, id);
		}
		System.out.println(map.size());
	}
	
}
