package com.mshuoke.ebatis.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 转换工具
 * @author Administrator
 *
 */
public class ConvertUtil {
	
	/**
	 * 将字符串首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCase(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	}
	
	/**
	 * 将表头的内容根据map进行替换
	 * @param replaceHead 要替换的内容
	 * @param listHeader 被替换的表头
	 * @param sheetFlag 替换第几个sheet的表头
	 */
	public static void replaceHead(List<Map<String, String>> replaceHead, List<String> listHeader, int sheetFlag) {
		Map<String, String> map = replaceHead.get(sheetFlag);
		if(map == null) {
			// 如果为null不替换
			return;
		}
		Set<String> keySet = map.keySet();
		for(int x = 0; x < listHeader.size(); x ++) {
			for(String y:keySet) {
				if(listHeader.get(x).equals(y)) {
					listHeader.set(x, map.get(y));
				}
			}
		}
	}
	
}
