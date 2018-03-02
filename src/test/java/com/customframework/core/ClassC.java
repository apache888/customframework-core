package com.customframework.core;

import com.customframework.core.annotation.Repository;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */
@Repository
public interface ClassC<T> {

    T findByName(String name);

    T findByNameAndAge(String name, Integer age);

    T findByNameOrLastName(String name, String lastName);



}
