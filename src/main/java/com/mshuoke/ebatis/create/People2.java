package com.mshuoke.ebatis.create;

import java.util.Date;

import com.mshuoke.ebatis.annotation.EnableExcelMaker;
import com.mshuoke.ebatis.annotation.ExcelField;
import com.mshuoke.ebatis.annotation.ExcelTitle;
import com.mshuoke.ebatis.annotation.LineNumber;
import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.annotation.MappingSheetName;


@EnableExcelMaker
@ExcelTitle(value = "数据生成表反馈标题")
public class People2 {
	
	@ExcelField(position = 0, name = "姓名")
	@Mapping(key = "姓名1")
	private String name;
	@ExcelField(position = 1, name = "手机号")
	@Mapping(key = "手机号1", rex="^[0-9]{11}$")
	private String phone;
	@Mapping(key = "点数1")
	private Double point;
	@Mapping(key = "日期1")
	private Date date;
	@ExcelField(position = 2, name = "年龄")
	@Mapping(key = "年龄1")
	private Integer age;
	@LineNumber
	private Integer line;
	@MappingSheetName
	private String type;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object people) {
		
		People2 p = (People2)people;
		if(this.phone == null) {
			return true;
		}
		if(this.phone.equals(p.getPhone())) {
			return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "People2 [name=" + name + ", phone=" + phone + ", point=" + point + ", date=" + date + ", age=" + age
				+ ", line=" + line + ", type=" + type + "]";
	}
	
	
}
