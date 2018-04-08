package com.mshuoke.ebatis.test;

import com.mshuoke.ebatis.annotation.Mapping;

public class Kid {
	
	@Mapping(key = "请填写您的手机号码")
	private String phone;
	
	private String name;
	@Mapping(key = "请填写您的宝贝姓名/年龄")
	private String info;
	
	private String city;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	private String age;
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
