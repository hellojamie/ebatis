package com.mshuoke.ebatis.test.pojo;

import com.mshuoke.ebatis.annotation.EnableExcelMaker;
import com.mshuoke.ebatis.annotation.ExcelField;
import com.mshuoke.ebatis.annotation.ExcelTitle;

@EnableExcelMaker
@ExcelTitle(value = "CSJ 数据反馈表")
public class ResultPojo {
	
	@ExcelField(position = 0, name = "类型", width = 6, merge = true)
	private String title;
	
	@ExcelField(position = 1, name = "字段", width = 6, merge = true)
	private String ziduan;
	
	@ExcelField(position = 2, name = "数据导入日期", width = 6, merge = true)
	private String dates;
	
	@ExcelField(position = 3, name = "类别", width = 6, merge = true)
	private String type;
	
	@ExcelField(position = 4, name = "城市", width = 6)
	private String city;
	
	@ExcelField(position = 5, name = "入库量", width = 6)
	private Integer num;
	
	@ExcelField(position = 6, name = "CSJ数据量分类别汇总", width = 6, merge = true)
	private Integer all1;
	
	@ExcelField(position = 7, name = "当日去重数据量", width = 6)
	private Integer num2;
	
	@ExcelField(position = 8, name = "当日去重数据量汇总", width = 6, merge = true)
	private Integer all2;
	
	@ExcelField(position = 9, name = "一周历史去重数据量", width = 6)
	private Integer num3;
	
	@ExcelField(position = 10, name = "一周历史去重数据量汇总", width = 6, merge = true)
	private Integer all3;
	
	@ExcelField(position = 11, name = "一月历史去重数据量", width = 6)
	private Integer num4;
	
	@ExcelField(position = 12, name = "一月历史去重数据量汇总", width = 6, merge = true)
	private Integer all4;

	
	
	public ResultPojo() {
		super();
	}
	
	public ResultPojo(String title, String ziduan, String dates, String type, String city, Integer num, Integer all1,
			Integer num2, Integer all2, Integer num3, Integer all3, Integer num4, Integer all4) {
		super();
		this.title = title;
		this.ziduan = ziduan;
		this.dates = dates;
		this.type = type;
		this.city = city;
		this.num = num;
		this.all1 = all1;
		this.num2 = num2;
		this.all2 = all2;
		this.num3 = num3;
		this.all3 = all3;
		this.num4 = num4;
		this.all4 = all4;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZiduan() {
		return ziduan;
	}

	public void setZiduan(String ziduan) {
		this.ziduan = ziduan;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getAll1() {
		return all1;
	}

	public void setAll1(Integer all1) {
		this.all1 = all1;
	}

	public Integer getNum2() {
		return num2;
	}

	public void setNum2(Integer num2) {
		this.num2 = num2;
	}

	public Integer getAll2() {
		return all2;
	}

	public void setAll2(Integer all2) {
		this.all2 = all2;
	}

	public Integer getNum3() {
		return num3;
	}

	public void setNum3(Integer num3) {
		this.num3 = num3;
	}

	public Integer getAll3() {
		return all3;
	}

	public void setAll3(Integer all3) {
		this.all3 = all3;
	}

	public Integer getNum4() {
		return num4;
	}

	public void setNum4(Integer num4) {
		this.num4 = num4;
	}

	public Integer getAll4() {
		return all4;
	}

	public void setAll4(Integer all4) {
		this.all4 = all4;
	}
	
	
}
