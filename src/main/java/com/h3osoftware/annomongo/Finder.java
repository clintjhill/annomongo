package com.h3osoftware.annomongo;

import java.util.Map;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 23, 2010
 * Time: 11:46:52 AM
 */
public class Finder {

    public static Object query(Class<?> klass, Map<String, Object> query){
        return Database.Find(klass,query);
    }
}
