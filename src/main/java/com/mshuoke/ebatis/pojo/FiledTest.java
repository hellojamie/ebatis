package com.mshuoke.ebatis.pojo;

import java.util.Date;

import com.mshuoke.ebatis.annotation.LineNumber;
import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.annotation.MappingSheetName;

public class FiledTest {
	@Mapping(key = "日期")
	private Date date;
	
	@Mapping(key = "点数")
	private Double num;
	
	@Mapping(key = "姓名")
	private String name;
	
	@Mapping(key = "手机号", rex = "^[0-9]{11}$")
	private String phone;
	
	@Mapping(key = "年龄")
	private Integer age;
	
	@MappingSheetName
	private String type;
	
	@LineNumber
	private Integer line;
	
	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
