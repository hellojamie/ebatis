package cc.ebatis.test.pojo;

import java.util.Date;

import cc.ebatis.annotation.Mapping;

public class RealPojo {
	
	@Mapping(key = "编号")
	private Integer number;
	
	@Mapping(key = "电话号码",length=8)
	private String phone;
	
	@Mapping(key = "日期")
	private Date date;
	
	@Mapping(key = "孩子姓名")
	private String name;
	
	@Mapping(key = "成人姓名")
	private String parentName;
	
	@Mapping(key = "父母性别")
	private String parentSex;
	
	@Mapping(key = "孩子性别")
	private String sex;
	
	@Mapping(key = "孩子年龄")
	private Integer age;
	
	@Mapping(key = "孩子生日")
	private String kidBirth;
	
	@Mapping(key = "学校")
	private String school;
	
	@Mapping(key = "城市")
	private String city;
	
	@Mapping(key = "省")
	private String sheng;
	
	@Mapping(key = "批次")
	private Integer batch;
	
	@Mapping(key = "信息来源")
	private String source;
	
	@Mapping(key = "点击量")
	private Integer hits;

	public RealPojo() {}
	
	public RealPojo(Integer number, String phone, Date date, String name, String parentName, String parentSex,
			String sex, Integer age, String kidBirth, String school, String city, String sheng, Integer batch,
			String source, Integer hits) {
		super();
		this.number = number;
		this.phone = phone;
		this.date = date;
		this.name = name;
		this.parentName = parentName;
		this.parentSex = parentSex;
		this.sex = sex;
		this.age = age;
		this.kidBirth = kidBirth;
		this.school = school;
		this.city = city;
		this.sheng = sheng;
		this.batch = batch;
		this.source = source;
		this.hits = hits;
	}



	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentSex() {
		return parentSex;
	}

	public void setParentSex(String parentSex) {
		this.parentSex = parentSex;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getKidBirth() {
		return kidBirth;
	}

	public void setKidBirth(String kidBirth) {
		this.kidBirth = kidBirth;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSheng() {
		return sheng;
	}

	public void setSheng(String sheng) {
		this.sheng = sheng;
	}

	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}
	
	@Override
	public int hashCode() {
		int hashCode = (this.phone + this.name).hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object str) {
		return true;
	}

	@Override
	public String toString() {
		return "{\"number\":" + number + ", \"phone\":" + phone + ", \"date\":" + date + ", \"name\":" + name
				+ ", \"parentName\":" + parentName + ", \"parentSex\":" + parentSex + ", \"sex\":" + sex + ", \"age\":"
				+ age + ", \"kidBirth\":" + kidBirth + ", \"school\":" + school + ", \"city\":" + city + ", \"sheng\":"
				+ sheng + ", \"batch\":" + batch + ", \"source\":" + source + ", \"hits\":" + hits + "\"} ";
	}
	
}

