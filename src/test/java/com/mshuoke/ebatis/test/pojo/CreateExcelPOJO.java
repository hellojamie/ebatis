package com.mshuoke.ebatis.test.pojo;

import java.util.Date;

import com.mshuoke.ebatis.annotation.EnableExcelMaker;
import com.mshuoke.ebatis.annotation.ExcelField;
import com.mshuoke.ebatis.annotation.ExcelTitle;
import com.mshuoke.ebatis.annotation.Mapping;

@EnableExcelMaker
@ExcelTitle(value = "数据生成表反馈标题")
public class CreateExcelPOJO {

	@ExcelField(position = 0, name = "姓名", width = 6)
	@Mapping(key = "姓名")
	private String name;
	
	@ExcelField(position = 1, name = "手机号", width = 11)
	@Mapping(key = "手机号", rex="^[0-9]{11}$")
	private String phone;

	@ExcelField(position = 3, name = "日期", width = 10)
	@Mapping(key = "日期")
	private Date date;
	
	@ExcelField(position = 2, name = "年龄", width = 6)
	@Mapping(key = "年龄")
	private Integer age;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "CreateExcelPOJO [name=" + name + ", phone=" + phone + ", date=" + date + ", age=" + age + "]";
	}
	
	
}
