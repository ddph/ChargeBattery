package com.clound.battery.model.mina.kit;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Pattern;

public class HexKit {
	
	private static int SN_MIN = 1, SN = 0, SN_MAX = 999;
	
	/**
     * 指定位数，左补0
     * @param src 源串
     * @param length 目标长度
     */
	public static String fillZeroLeft(String src, int length) {
		StringBuilder sBuilder = new StringBuilder(src);
		for(int i=length-src.length();i>0;i--){
			sBuilder.insert(0, "0");
		}
		return sBuilder.toString();
	}
	
	/**
     * 指定位数，右补0
     * @param src 源串
     * @param length 目标长度
     */
	public static String fillZeroRight(String src, int length) {
		StringBuilder sBuilder = new StringBuilder(src);
		for(int i=length-src.length();i>0;i--){
			sBuilder.append("0");
		}
		return sBuilder.toString();
	}
	
	/**
	 * 将数字转换为N个字节长的16进制字符串<br/>
	 * 示例：26正常转换应该为"001A"，按小端模式转换后，则为"1A00"
	 * 示例：65432转为16进制字符串是"0000FF98"，转换为小端模式则为"98FF0000"
	 * @param num 要转换的数字
	 * @param byteLength 要求的字节长度
	 */
	public static String int2Hex(int num,int byteLength){
		String hexString = fillZeroLeft(Integer.toHexString(num), byteLength*2).toUpperCase();
		//小端转换
		return endianExchange(hexString);
	}
	
	/**
	 * 将数字转换为N个字节长的16进制字符串<br/>
	 * 示例：26正常转换应该为"001A"，按小端模式转换后，则为"1A00"
	 * 示例：65432转为16进制字符串是"0000FF98"，转换为小端模式则为"98FF0000"
	 * @param num 要转换的数字
	 * @param byteLength 要求的字节长度
	 */
	public static String long2Hex(long num,int byteLength){
		String hexString = fillZeroLeft(Long.toHexString(num), byteLength*2).toUpperCase();
		//小端转换
		return endianExchange(hexString);
	}
	
	/**
	 * 将小端模式的16进制字符串，换为对应的数字
	 * 示例：65432转为16进制字符串是0000FF98，转换为小端模式则为98FF0000
	 * @param hexStr 16进制字符串
	 */
	public static int toInt(String hexStr){
		//小端转换后，转成int输出
		return Integer.parseInt(bytes2Hex(endianExchange(hex2Bytes(hexStr))),16);
	}
	public static int toInt(byte[] bytes){
		//小端转换后，转成int输出
		return Integer.parseInt(bytes2Hex(endianExchange(bytes)),16);
	}
	
	/**
	 * 将小端模式的16进制字符串，换为对应的数字
	 * 示例：65432转为16进制字符串是0000FF98，转换为小端模式则为98FF0000
	 * @param hexStr 16进制字符串
	 */
	public static long toLong(String hexStr){
		//小端转换后，转成int输出
		return Long.parseLong(bytes2Hex(endianExchange(hex2Bytes(hexStr))),16);
	}
	public static long toLong(byte[] bytes){
		//小端转换后，转成int输出
		return Long.parseLong(bytes2Hex(endianExchange(bytes)),16);
	}

	/**
	 * 将int数值16进制字符串转为int类型数值
	 * @param hexStr float数值16进制字符串
	 */
	public static int hex2Int(String hexStr){
		return Integer.valueOf(hexStr, 16);
	}
	
	/**
	 * 将16进制字符串转换为double类型数字，用于坐标转换
	 * 示例：27bb6fac2bd74340->39.68102031188601
	 * @param hexStr 16进制字符串
	 */
	public static double hex2Double(String hexStr){
		return Double.longBitsToDouble(Long.valueOf(HexKit.endianExchange(hexStr), 16));
	}
	
	/**
	 * 将double类型数字转换为16进制字符串，用于坐标转换
	 * 示例：39.68102031188601->27bb6fac2bd74340
	 * @param num double类型数字
	 */
	public static String double2Hex(double num){
		return HexKit.endianExchange(Long.toHexString(Double.doubleToRawLongBits(num)));
	}
	
	/** 
	 * 从字节数组到16进制字符串转换<br/>
	 * 示例：new byte[]{0x00,0x1A} -> "001A"
	 */
	public static String bytes2Hex(byte bytes) {
		return bytes2Hex(new byte[]{bytes});
	}
	public static String bytes2Hex(byte[] bytes) {  
	   byte[] buff = new byte[2 * bytes.length];  
	   for (int i = 0; i < bytes.length; i++) {  
	       buff[2 * i] = HEX_STRING_BYTES[(bytes[i] >> 4) & 0x0f];  
	       buff[2 * i + 1] = HEX_STRING_BYTES[bytes[i] & 0x0f];  
	   }  
	   return new String(buff);  
	}  

	/** 
	 * 从16进制字符串到字节数组转换 <br/>
	 * 示例："001A" -> new byte[]{0x00,0x1A}
	 */  
	public static byte[] hex2Bytes(String hexStr) {  
	    byte[] b = new byte[hexStr.length() / 2];  
	    int j = 0;  
	    for (int i = 0; i < b.length; i++) {  
	        char c0 = hexStr.charAt(j++);  
	        char c1 = hexStr.charAt(j++);  
	        b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
	    }  
	    return b;  
	}
	
	/**
	 * 获取16进制字符串累计和，用于生成校验位<br/>
	 * 示例：55aa1A00103131313131313131313233343536373800000001的累计和是456，只保留一个字节长，最后得出56
	 */
	public static String getHexSumForCRC(String hexStr){
		int count = 0, length = hexStr.length()/2;
		for(int i=0;i<length;i++){
			count = count + Integer.parseInt(hexStr.substring(i*2,i*2+2),16);
		}
		String resultHexStr = Integer.toHexString(count);
		if(resultHexStr.length()>2){
			resultHexStr = resultHexStr.substring(resultHexStr.length()-2);
		}
		return resultHexStr;
	}
	
	/**
	 * 获取16进制字符串中每一个字符的累计和，只保留个位上的数字，用于生成校验位<br/>
	 * 示例：AB81442A1200的累计和是10+11+8+1+4+4+2+10+1+2+0+0=53，只保留个位上的数字，最后得出3
	 */
	public static String getHexPerSumForCRC(String hexStr){
		int count = 0, length = hexStr.length();
		for(int i=0;i<length;i++){
			String hexItem = hexStr.substring(i,i+1);
			if(Pattern.compile(HEX_REG, Pattern.DOTALL).matcher(hexItem).matches()){
				count = count + Integer.parseInt(hexStr.substring(i,i+1),16);
			}
		}
		return Integer.toHexString(count%10);
	}
	
	/**
     * 字符串转化为一个字节数组，再将字节数据，转化为16进制字符串<br/>
     * 示例："<H01001020304050617283901>" -> "3C4830313030313032303330343035303631373238333930313E"
     * @param str 源串
     */
	public static String str2Hex(String str) {
		StringBuilder sBuilder = new StringBuilder();
		try {
			byte[] b = str.getBytes("gbk");
			for (int i = 0, max = b.length; i < max; i++) {
				sBuilder.append(Integer.toHexString(b[i] & 0xff));
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("字符串转化为16进制字符串异常！" + e.getMessage());
		}
		return sBuilder.toString().toUpperCase();
	}

	/**
	 * 字符串转化为一个字节数组，再将字节数据，转化为16进制字符串，不足位数时前补0<br/>
     * 示例："<H01001020304050617283901>" -> "3C4830313030313032303330343035303631373238333930313E"
     * @param str 源串
	 * @param byteLength 要求的字节长度
	 */
	public static String str2Hex(String str,int byteLength) {
		String hexString = str2Hex(str);
		return fillZeroLeft(hexString,byteLength*2);
	}

	/**
	 * 把十六进制字符串转换成字符串
	 * @param hexStr 16进制字符串
	 */
	public static String hex2Str(String hexStr) {
		try {
			return new String(hex2Bytes(hexStr), "gbk");
		} catch (UnsupportedEncodingException e) {
			System.out.println("16进制字符串转化为字符串异常！" + e.getMessage());
		}
		return hexStr;
	}

	/**
	 * 将指定的日期对象转换为16进制的日期时间字符串
	 * @param date 要转换的日期对象
	 */
	public static String date2Hex(Date date) {
		//将源日期对象转换为需要的格式字符串
		String srcDate = DateKit.format(date,DateKit.FORMAT3);
		//分别截取年、月、日、时、分、秒各部分进行转换为16进制
		String year = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(0, 4)), 16), 4);
		String month = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(4, 6)), 16), 2);
		String day = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(6, 8)), 16), 2);
		String hour = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(8, 10)), 16), 2);
		String minute = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(10, 12)), 16), 2);
		String seconds = fillZeroLeft(Integer.toString(Integer.valueOf(srcDate.substring(12, 14)), 16), 2);
		//然后拼接在一起返回即可
		return day + month + year + hour + minute + seconds;
	}

	/**
	 * 字节数组内容截取
	 * @param bytes 源数组
	 * @param start 起始位
	 * @param to 结束位
	 */
	public static byte[] sub(byte[] bytes,int start,int to){
		byte[] des = new byte[to-start];
		System.arraycopy(bytes, start, des, 0, to-start);
		return des;
	}

	/**
	 * 将16进制字符串，进行大小端模式转换
	 * 示例：26正常转换应该为"001A"，按小端模式转换后，则为"1A00"
	 * 示例：65432转为16进制字符串是"0000FF98"，转换为小端模式则为"98FF0000"
	 * @param hexString 源16进制字符串
	 */
	private static String endianExchange(String hexString){
		//小端转换
		int total = hexString.length()/2;
		StringBuilder sBuilder = new StringBuilder();
		for(int i=total-1;i>=0;i--){
			sBuilder.append(hexString.substring(2*i,2*i+2));
		}
		return sBuilder.toString().toUpperCase();
	}
	
	/**
	 * 将小端模式的byte数组，进行大小端模式转换
	 * @param littleEndianBytes 小端模式的byte数组
	 */
	private static byte[] endianExchange(byte[] littleEndianBytes){
		int total = littleEndianBytes.length;
		byte[] b = new byte[total];
		for(int i=total-1;i>=0;i--){
			b[total-1-i] = littleEndianBytes[i];
		}
		return b;
	}

	private final static String HEX_REG = "[0|1|2|3|4|5|6|7|8|9|A|a|B|b|C|c|D|d|E|e|F|f]";
	private final static String HEX_STRING = "0123456789ABCDEF";
	private final static byte[] HEX_STRING_BYTES = HEX_STRING.getBytes();

	private static int parse(char c) {  
	    if (c >= 'a')  
	        return (c - 'a' + 10) & 0x0f;  
	    if (c >= 'A')  
	        return (c - 'A' + 10) & 0x0f;  
	    return (c - '0') & 0x0f;  
	}
}
