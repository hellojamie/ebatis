package com.mshuoke.ebatis.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mshuoke.ebatis.emnu.FileType;

public class ActionContext<T> {
	@JsonIgnore
	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();;
	
	// 文件流
	@JsonIgnore
	private InputStream inputStream;
	
	// 表格内数据
	private List<SheetInfo<T>> sheets = new ArrayList<SheetInfo<T>>();
	
	// 文件类型
	private FileType fileType;
	
	// sheet数量
	private Integer SheetSize;
	
	// 文件大小
	private Integer fileSizeByte;
	
	private T objects;
	
	private boolean useSax = false;
	
	// 是否去除重复，默认否
	private boolean distinct = false;
	
	// 是否完成链式操作，默认false
	private boolean result = false;

	public boolean getDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean getUseSax() {
		return useSax;
	}

	public void setUseSax(boolean useSax) {
		this.useSax = useSax;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public T getObjects() {
		return objects;
	}

	public void setObjects(T objects) {
		this.objects = objects;
	}

	public Integer getFileSizeByte() {
		return fileSizeByte;
	}

	public void setFileSizeByte(Integer fileSizeByte) {
		this.fileSizeByte = fileSizeByte;
	}

	public Integer getSheetSize() {
		return SheetSize;
	}

	public void setSheetSize(Integer sheetSize) {
		SheetSize = sheetSize;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		
		byte[] buffer;
		int len;
		try {
			buffer = new byte[inputStream.available()];
			while ((len = inputStream.read(buffer)) > -1 ) {    
				byteArrayOutputStream.write(buffer, 0, len);    
	        }
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	public List<SheetInfo<T>> getSheets() {
		return sheets;
	}

	public void setSheets(List<SheetInfo<T>> sheets) {
		this.sheets = sheets;
	}
	
	public void addSheets(SheetInfo<T> sheets) {
		if(this.sheets != null) {
			this.sheets.add(sheets);
		}
		
	}

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	
	
}
