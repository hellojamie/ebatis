package com.mshuoke.ebatis.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mshuoke.ebatis.annotation.LineNumber;
import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.annotation.MappingSheetName;

/**
 * 反射对象工具
 * @author Administrator
 *
 * @param <T>
 */
public class ReflexObject<T> {
	/**
	 * 将cell的信息反射进java bean
	 * @param class1 反射对象
	 * @param headStr 头信息
	 * @param analysisRow 行内容
	 * @param sheetName sheet名称
	 * @param lineNum 第几行
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getReflexObject(Class<?> class1, List<String> headStr, List<String> analysisRow, String sheetName, int lineNum){
		/*Object objects = act.getObjects();
		Class<?> class1 = objects.getClass();*/
		
		/*
		 * 改版代码开始================================================
		 */
		
		Object object = null;
		
		// 反射获取所有字段，遍历字段取得它们的注解参数，以map的形式保存下来复用
		try {
			
			Constructor<?> constructor = class1.getConstructor();
			object = constructor.newInstance();
			
			// 获取实体属性列表
			Field[] fields = class1.getDeclaredFields();
			Class<Mapping> mapping = Mapping.class;
			Class<MappingSheetName> mappingSheetName = MappingSheetName.class;
			Class<LineNumber> lineNumber = LineNumber.class;
			
			for(Field x : fields) {
				// 获取当前属性的注解数组，并查看是否有数量
				Annotation[] annotations = x.getAnnotations();
				if(annotations.length == 0) {
					continue;
				}
				Mapping m = x.getAnnotation(mapping);
				MappingSheetName msn = x.getAnnotation(mappingSheetName);
				LineNumber ln = x.getAnnotation(lineNumber);
				
				if(m != null) {
					// 字段映射操作
					boolean mappingOperation = this.mappingOperation(class1, object, x, m, headStr, analysisRow);
					
					// 获取注解是否删除null属性
					// 如果这次映射失败，属性值为null，并且注解标识需要删除该字段为null的信息，则删除
					if(m.delNull() && !mappingOperation) {
						return null;
					}
					
				}
				
				if(msn != null) {
					// sheet名映射操作
					this.sheetNameOperation(class1, object, x, sheetName);		
				}
				
				if(ln != null) {
					// 行数映射操作
					this.lineNumberOperation(class1, object, x, lineNum);
				}

			}
		
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return (T)object;
		
		/*
		 * 改版代码结束================================================
		 */
		
	}
	
	/*改版测试方法如下*/
	
	/**
	 * 字段映射操作
	 * @param object 反射对象
	 * @param headStr 表头信息
	 * @param analysisRow 行内容信息
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private boolean mappingOperation(Class<?> class1, Object object, Field field, Mapping mapping, List<String> headStr, List<String> analysisRow) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		// 获取字段名称
		String fieldName = field.getName();
		// 获取注解映射属性
		String title = mapping.key();
		// 获取注解正则属性
		String rex = mapping.rex();
		
		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(ConvertUtil.upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		for(int y=0; y<headStr.size(); y++) {
			// 当前头标
			String thisHead = headStr.get(y);
			if(title.equals(thisHead) && !thisHead.equals("") && thisHead != null){
				
				String string = analysisRow.get(y);
				
				// 判断正则是否为空，为空则不处理
				if(!rex.equals("")) {
					
					Pattern compile = Pattern.compile(rex);
					Matcher matcher = compile.matcher(string);
					if(!matcher.matches()) {
						// 如果匹配失败设为null
						return false;
					}
				}
				
				this.screening(class1, object, methodName, fieldType, string);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * sheet名称映射操作
	 * @param class1
	 * @param object
	 * @param x
	 * @param m
	 * @param sheetName
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void sheetNameOperation(Class<?> class1, Object object, Field field, String sheetName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// 获取字段名称
		String fieldName = field.getName();

		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(ConvertUtil.upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		this.screening(class1, object, methodName, fieldType, sheetName);
	}
	
	/**
	 * 映射行数到实体
	 * @param class1
	 * @param object
	 * @param field
	 * @param lineNum
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void lineNumberOperation(Class<?> class1, Object object, Field field, int lineNum) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// 获取字段名称
		String fieldName = field.getName();

		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(ConvertUtil.upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		this.screening(class1, object, methodName, fieldType, String.valueOf(lineNum));
	}
	
	/**
	 * 根据类型筛选赋值
	 * @param object 反射对象
	 * @Param methodName 方法名称
	 * @param fieldType 属性类型
	 * @param fieldValue 属性值
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void screening(Class<?> class1, Object object, String methodName, String fieldType, String fieldValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Double parseToDouble = null;
		
		switch(fieldType){
		case "class java.util.Date":
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			Date parse = null;
			try{
				parse = format.parse(fieldValue);
			}catch(ParseException e){
				// 如果异常，比如字段为空或null，设置为null
				parse = null;
			}
			class1.getMethod(methodName, Date.class).invoke(object, parse);
			break;
		case "class java.lang.Integer":
			Integer parseInt = null;
			
			parseToDouble = parseToDouble(fieldValue);
			if(parseToDouble != null) {
				double doubleVal = parseToDouble;
				parseInt = (int)doubleVal;
			}
			try {
				parseInt = Integer.parseInt(fieldValue);
			}catch(NumberFormatException e) {}
			
			class1.getMethod(methodName, Integer.class).invoke(object, parseInt);
			break;
		case "class java.lang.String":
			if(fieldValue.equals("")) {
				break;
			}
			class1.getMethod(methodName, String.class).invoke(object, fieldValue);
			break;
		case "class java.lang.Long":
			Long parseLong = null;
			
			parseToDouble = parseToDouble(fieldValue);
			if(parseToDouble != null) {
				double doubleVal = parseToDouble;
				parseLong = (long)doubleVal;
			}
			
			try{
				parseLong = Long.parseLong(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Long.class).invoke(object, parseLong);
			break;
		case "class java.lang.Double":
			Double parseDouble = null;
			try{
				parseDouble = Double.parseDouble(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Double.class).invoke(object, parseDouble);
			break;
		case "class java.lang.Short":
			Short parseShort = null;
			
			parseToDouble = parseToDouble(fieldValue);
			if(parseToDouble != null) {
				double doubleVal = parseToDouble;
				parseShort = (short)doubleVal;
			}
			
			try{
				parseShort = Short.parseShort(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Short.class).invoke(object, parseShort);
			break;
		case "class java.lang.Boolean":
			Boolean parseBoolean = null;
			try{
				parseBoolean = Boolean.parseBoolean(fieldValue);
			}catch(Exception e){}
			class1.getMethod(methodName, Boolean.class).invoke(object, parseBoolean);
			break;
		}
	}
	
	/**
	 * 转为Double类型
	 * @return
	 */
	public Double parseToDouble(String fieldValue) {
		Double parseDouble = null;
		try{
			if(fieldValue != null && fieldValue.contains(".")) {
				parseDouble = Double.parseDouble(fieldValue);
			}
			
		}catch(NumberFormatException e){}
		
		return parseDouble;
	}
}
