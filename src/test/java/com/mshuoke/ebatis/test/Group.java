package com.mshuoke.ebatis.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Group {
	public static void main(String[] args) {
        List<JavaBean> list = new ArrayList<JavaBean>();
        list.add(new JavaBean("来源A", 100, 1));
        list.add(new JavaBean("来源B", 200, 0));
        list.add(new JavaBean("来源C", 300, 1));
        list.add(new JavaBean("来源B", 6600, 0));
        list.add(new JavaBean("来源A", 99800, 1));
        
        Map<String, List<JavaBean>> counting = list.stream().collect(
        		Collectors.groupingBy(JavaBean::getGroup)
        		);
        
        System.out.println(counting);
        
    }

}

class JavaBean {
    private String group;
    private int money;
    private int time;

    public JavaBean() {
    }

    public JavaBean(String group, int money, int time) {
        this.group = group;
        this.money = money;
        this.time = time;
    }

    public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

	@Override
	public String toString() {
		return "JavaBean [group=" + group + ", money=" + money + ", time=" + time + "]";
	}
    
}
