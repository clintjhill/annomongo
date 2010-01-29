package com.h3osoftware.annomongo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 25, 2010
 * Time: 8:55:35 PM
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Put {
	String value() default "";
}
