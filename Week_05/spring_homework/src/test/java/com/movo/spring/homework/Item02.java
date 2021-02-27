package com.movo.spring.homework;

import com.movo.spring.homework.bean.Corporation;
import com.movo.spring.homework.bean.Department;
import com.movo.spring.homework.bean.Staff;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Item02 {
    public static void main(String[] args) {
        // 该示例使用XML装配类，使用annotation装配demoStaff的belongDepart属性
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Department devDepart = (Department)context.getBean("devDepart");
        Department opDepart = (Department)context.getBean("opDepart");
        Corporation corporation = (Corporation)context.getBean("demoCorporation");

        devDepart.print();
        opDepart.print();
        corporation.print();
        // devDepart与demoCorporation里departs中的devDepart为同一个实例
        System.out.println(devDepart == corporation.getDeparts().get(0));

        Staff staff = (Staff)context.getBean("demoStaff");
        staff.print();
        // devDepart与demoStaff中的belongDepart为同一个实例
        System.out.println(devDepart == staff.getBelongDepart());

    }
}
