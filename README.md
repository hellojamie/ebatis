# Ebatis

用于解析excel表格内容到 java bean			
目前支持xls、xlsx格式文件	
对于大数据量文件自动使用sax方式解析，防止内存溢出	

>email：hello_jamie@outlook.com

# 目录（记得看注意）:

1. 开始
2. 扩展功能
3. 注意


# 开始

将jar加入到项目中
可以访问百度网盘下载
https://pan.baidu.com/s/155euSMWHTRk6438y9FaBfQ
[点击链接](https://pan.baidu.com/s/155euSMWHTRk6438y9FaBfQ)

```
// Maven导入第三方poi依赖,或者直接把master pull下来发布到本地
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
<dependency> 
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
	    <groupId>xerces</groupId>
	    <artifactId>xercesImpl</artifactId>
	    <version>2.9.1</version>
</dependency>
```

首先你需要创建好你的实体类，假设现在有这样一个excel表格需要解析

姓名 | 手机号  |  生日
------------ | ------------- | -------------
王文娟 | 18888888888 | 1996-01-01
大美丽 | 16666666666 | 1996-01-01

首先你需要一个实体类
有几点要求，必须正确封装，包含get\set方法
属性上包含必要的Mapping注解，key属性填入与表格对应的名称，属性类型根据需要自己定义

```
public class People {
	
	@Mapping(key = "姓名")
	private String name;
	
	@Mapping(key = "手机号")
	private String phone;
	
	@Mapping(key = "生日")
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

```

然后将你的文档以流的方式加载进来，通过以下代码开始解析
```
// Init接受一个InputStream对象，以及一个实体对象
// 调用start开始
// 通过ActionContext获取需要的信息(流信息，实体，是否去重)
Init<People> init = new Init<People>(inputStream, new People(), false);
ActionContext<People> act = init.start();
```
（注意：如果要去重的话请重写实体中的hashCode和equals方法，内部使用set来去重，false表示不去重）
ActionContext中包含了所需要的所有信息，信息格式如下，这里以json的形式展示
```
{
　　"sheets":[
　　　　{
　　　　　　"line":2,
　　　　　　"sheetName":"Sheet1",
　　　　　　"column":3,
　　　　　　"correctLine":2,
　　　　　　"blankLineSize":0,
　　　　　　"errorLineSize":0,
　　　　　　"repeatLineSize":0,
　　　　　　"info":[
　　　　　　　　{
　　　　　　　　　　"date":1331481600000,
　　　　　　　　　　"name":"王文娟",
　　　　　　　　　　"phone":"15035214458"
　　　　　　　　},
　　　　　　　　{
　　　　　　　　　　"date":1331481600000,
　　　　　　　　　　"name":"大美丽",
　　　　　　　　　　"phone":"14555874458"
　　　　　　　　}
　　　　　　],
		"blankLine":[

　　　　　　],
　　　　　　"errorLine":[

　　　　　　],
　　　　　　"repeatLine":[
　　　　　　　　
　　　　　　]
　　　　}
　　],
　　"fileType":"XLSX",
　　"fileSizeByte":9045,
　　"useSax":false,
　　"distinct":true,
　　"result":true,
　　"sheetSize":1
}
```
属性名 | 含义
------------ | -------------
sheets | sheet数组
line | 解析当前sheet一共多少行数据，不算表头
sheetName | sheet的名称
column | 列数
info | 实体对象数组，包含实体的列表，也就是行数据
correctLine | 实际正确解析出的数量（行数）
blankLineSize | 空行数量
errorLineSize | 错误行数量（正则不通过、解析失败等）
repeatLineSize | 重复行数量
blankLine | 空白行的行号-数组
errorLine | 错误行的行号-数组
repeatLine | 重复行的行号-数组
fileType | 文件类型
fileSizeByte | 文件大小（字节）
useSax | 是否使用了sax方式
distinct | 是否去重
result | 最后是否解析成功，如果中间出错则是false
sheetSize | 文件中有几个sheet

使用ActionContext的getXXX方法获取上面的内容

# 扩展功能

@Mapping注解有两个非必选属性
```
@Mapping(key = "手机号", rex = "^[0-9]{11}$", delNull = true)
private String phone;
```

属性名 | 含义 | 是否必填
------------ | ------------- | -------------
key | 填写与excel文件头的映射名称 | 必填
rex | 填写解析内容时使用的正则表达式，如果不符合正则则不赋值 | 非必填
delNull | 如果正则验证失败，该属性为null，是否删除整条信息，默认false不删除 | 非必填

@LineNumber注解，获取当前记录是第几行，不算表头那行
```
@LineNumber
private Integer line;
```

@MappingSheetName注解，将sheet名称作为属性值添加
```
@MappingSheetName
private String type;
```


# 注意
因为工具还不是很完善，需要注意以下几点:

* 实体中请使用包装类，不支持int等类型，请使用Integer
* 如果excel文件有多个sheet，sheet间的表头应一致，不然会出现sheet之间解析的数据不一致
* 列与列之间不能包含表头为空的列，即不能有空列将信息隔开，如果有，以空列前一列为末尾解析
* excel文件请使用第一行表头，其余行信息的标准格式，如果有合并单元格情况，可能会解析失败（可以包含空行和空单元格，会自动过滤，但必须有表头）
* 目前默认10M以内使用poi传统方式解析，超过10M且为xlsx的文件将使用sax方式解析
* 实体类的属性不严格要求与列的数量一致，根据需要添加映射注解即可
* 实体类 的属性和表头的顺序没有严格要求，只要key匹配即可
* 如果需要去重，请重写实体的hashCode和equals方法，内部使用HashSet方式去重，重写时请注意
* 最后是否解析成功请以result属性为准


2018/04/08
