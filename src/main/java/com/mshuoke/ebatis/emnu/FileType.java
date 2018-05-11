package com.mshuoke.ebatis.emnu;

public enum FileType {
	XLS("D0CF11E0"),XLSX("504B0304");
	
	private String value = "";  
    
    private FileType(String value) {  
        this.value = value;  
    }  
  
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    } 
}
