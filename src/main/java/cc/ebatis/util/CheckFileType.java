package cc.ebatis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cc.ebatis.emnu.FileType;

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
        
        return getByteStr(b);  
    }  
      
    /** 
     * 判断文件类型 
     */  
    public static FileType getType(File file) throws IOException {  
          
    	InputStream inputStream = new FileInputStream(file);
    	
        String fileHead = getFileHeader(inputStream);  
        
        inputStream.close(); 
        inputStream = null;
        
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
