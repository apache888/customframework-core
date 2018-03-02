package com.customframework.core;

import org.junit.Test;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */

public class MainTest {

    @Test
    public void test() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.customframework.core");
        context.init();
        System.out.println(context.getBeanNames().toString());
        System.out.println();
        context.getBean(ClassA.class).test();
        System.out.println();
        ClassB beanB = (ClassB) context.getBean("com.customframework.core.ClassB");
        beanB.test();

        System.out.println();
        context.getBean(ClassC.class).findByName("Name");
        context.getBean(ClassC.class).findByNameAndAge("Name", 30);
        context.getBean(ClassC.class).findByNameOrLastName("Name", "LastName");


    }

}
