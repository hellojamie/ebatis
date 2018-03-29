package org.ys.diamonds.pojo;

import java.util.Date;

import org.ys.diamonds.annotation.Mapping;

public class FiledTest {
	
	@Mapping(key = "日期")
	private Date date;
	
	@Mapping(key = "编号")
	private Integer num;
	
	@Mapping(key = "孩子姓名")
	private String name;
	
	@Mapping(key = "电话号码")
	private String phone;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
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
