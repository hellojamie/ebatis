package com.mshuoke.ebatis.test;

import java.util.Date;

import com.mshuoke.ebatis.annotation.Mapping;

public class BigPeople {
	
	@Mapping(key = "姓名")
	private String name;
	@Mapping(key = "手机1")
	private String phone;
	@Mapping(key = "生日")
	private Date birth;
	@Mapping(key = "年龄")
	private Integer age;
	@Mapping(key = "号段省")
	private String sheng;
	@Mapping(key = "号段城市")
	private String city;
	@Mapping(key = "性别")
	private String sex;

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSheng() {
		return sheng;
	}

	public void setSheng(String sheng) {
		this.sheng = sheng;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Override
	public int hashCode() {
		String substring = this.phone.substring(5, 11);
		int parseInt = Integer.parseInt(substring);
		return parseInt;
	}
	
	@Override
	public boolean equals(Object people) {
		
		BigPeople p = (BigPeople)people;
		if(this.phone.equals(p.getPhone())) {
			return true;
		}
		
		return false;
	}
}
