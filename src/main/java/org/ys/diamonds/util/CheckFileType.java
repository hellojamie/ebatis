package org.ys.diamonds.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.ys.diamonds.emnu.FileType;

public class CheckFileType {
	
	public static String getByteStr(byte[] bytes){
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {  
            String temp = Integer.toHexString(bytes[i] & 0xFF);  
            if (temp.length() == 1) {  
                hex.append("0");  
            }
            hex.append(temp.toLowerCase());  
        }  
        return hex.toString();  
	}
	
	/** 
     * 读取文件头 
     */  
    private static String getFileHeader(InputStream inputStream) throws IOException {  
        byte[] b = new byte[28];//这里需要注意的是,每个文件的magic word的长度都不相同,因此需要使用startwith  
        inputStream.read(b, 0, 28);  
        inputStream.close();  
                  
        return getByteStr(b);  
    }  
      
    /** 
     * 判断文件类型 
     */  
    public static FileType getType(InputStream inputStream) throws IOException {  
          
        String fileHead = getFileHeader(inputStream);  
        if (fileHead == null || fileHead.length() == 0) {  
            return null;  
        }  
        fileHead = fileHead.toUpperCase();  
        FileType[] fileTypes = FileType.values();  
        for (FileType type : fileTypes) {  
            if (fileHead.startsWith(type.getValue())) {  
                return type;  
            }  
        }  
        return null;  
    }  
      
	
}
