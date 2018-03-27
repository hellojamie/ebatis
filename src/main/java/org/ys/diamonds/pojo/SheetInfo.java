package org.ys.diamonds.pojo;

import java.util.List;
import java.util.Map;

public class SheetInfo<T> {
	
	private Integer line;		// 总数据行数
	
	private String sheetName;	// sheet名称
	
	private Integer column;		// 列数
	
	private Integer CorrectLine;// 成功解析的数据行数
	
	// private List<Map<String,String>> info;	// 行信息
	private List<T> info;
	
	public Integer getCorrectLine() {
		return CorrectLine;
	}

	public void setCorrectLine(Integer correctLine) {
		CorrectLine = correctLine;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public List<T> getInfo() {
		return info;
	}

	public void setInfo(List<T> info) {
		this.info = info;
	}
	
	
}
