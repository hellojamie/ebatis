package com.mshuoke.ebatis.test;

import java.util.Date;

import com.mshuoke.ebatis.annotation.Mapping;

public class People {
	
	@Mapping(key = "姓名")
	private String name;
	
	@Mapping(key = "手机号", rex = "^[0-9]{11}$")
	private String phone;
	
	private Date birth;

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

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
