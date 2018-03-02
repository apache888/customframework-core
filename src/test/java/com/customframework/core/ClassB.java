package com.customframework.core;

import com.customframework.core.annotation.Autowired;
import com.customframework.core.annotation.Component;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */
@Component
public class ClassB {

    @Autowired
    private ClassA classA;

    public void test(){
        classA.test();
        System.out.println("Trough B!");
    }


}
