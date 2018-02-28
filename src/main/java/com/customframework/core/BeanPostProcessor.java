package com.customframework.core;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);



}
