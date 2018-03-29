package org.ys.diamonds.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.ys.diamonds.emnu.FileType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ActionContext<T> {
	
	@JsonIgnore
	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();;
	
	// 文件流
	@JsonIgnore
	private InputStream inputStream;
	
	// 表格内数据
	private List<SheetInfo<T>> info = new ArrayList<SheetInfo<T>>();
	
	// 文件类型
	private FileType fileType;
	
	// 有效sheet数量
	private Integer SheetSize;
	
	// 文件大小
	private Integer fileSizeByte;
	
	@JsonIgnore
	private T objects;
	
	// 是否完成链式操作，默认false
	private boolean result = false;

	public boolean isResult() {
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

	public List<SheetInfo<T>> getInfo() {
		return info;
	}

	public void setInfo(List<SheetInfo<T>> info) {
		this.info = info;
	}

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	
	
}
