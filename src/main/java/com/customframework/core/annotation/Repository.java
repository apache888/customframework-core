package com.customframework.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Component
public @interface Repository {
}
