package com.mshuoke.ebatis.test.pojo;

import java.util.Date;

import com.mshuoke.ebatis.annotation.LineNumber;
import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.annotation.MappingSheetName;

public class ImportPojo {
	
	@Mapping(key = "编号")
	private Integer id;
	
	@Mapping(key = "电话号码",rex = "[0-9]{10}")
	private String phone;
	
	@Mapping(key = "日期")
	private Date date;
	
	@Mapping(key = "姓名")
	private String name;
	
	@Mapping(key = "点数",length = 1)
	private Double point;
	
	@Mapping(key = "序号")
	private Long number;
	
	@MappingSheetName
	private String sheetName;
	
	@LineNumber
	private Integer lineNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getLineNum() {
		return lineNum;
	}

	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"phone\":\"" + phone + "\", \"date\":\"" + date + "\", \"name\":\"" + name
				+ "\", \"point\":" + point + ", \"number\":" + number + ", \"sheetName\":\"" + sheetName
				+ "\", \"lineNum\":" + lineNum + "} ";
	}


	
	
}
