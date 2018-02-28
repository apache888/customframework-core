package com.customframework.core;

import java.util.Set;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */
public interface ApplicationContext {

    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);

    Set<String> getBeanNames();

    void init() throws IllegalAccessException, ClassNotFoundException, InstantiationException;
}
