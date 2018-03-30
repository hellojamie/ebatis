package com.mshuoke.ebatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.pojo.FiledTest;

public class Fan<T> {

	@Mapping(key = "jamie")
	private String name;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Fan<FiledTest> fan = new Fan<FiledTest>();
		
		Class<Fan> fz = Fan.class;
		Field[] fields = fz.getDeclaredFields();
		for(Field x:fields){
			Mapping annotation = x.getAnnotation(Mapping.class);
			System.out.println(annotation.key());
		}
		
		/*System.out.println(System.currentTimeMillis() / 1000);
		Fan<FiledTest> fan = new Fan<FiledTest>();
		FiledTest test = new FiledTest();
		FiledTest test2 = fan.getThis(test.getClass());
		Constructor<?> constructor = this1.getConstructor();
		Object newInstance = constructor.newInstance();
		Method method = this1.getMethod("setNum",Integer.class);
		Method method2 = this1.getMethod("getNum");
		for(int i=0;i<500000;i++){
			method.invoke(newInstance,10);
		}*/
		
	}
	
	
	public T getThis(Class<?> c){
		return (T)c;
	}
}
