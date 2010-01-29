package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.*;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 26, 2010
 * Time: 9:15:25 PM
 */
class Inference {

    private static final String METHOD_NAME = "([a-z][0-9]*)([A-Z])";
        
    public Inference() { }

    public String keyFromField(java.lang.reflect.Field f) {
        if(!f.isAnnotationPresent(Field.class)) return null;
        String key = f.getAnnotation(Field.class).value();
        if(key == null || "".equals(key)) key = f.getName();
        if (f.isAnnotationPresent(Id.class)) key = "_id";
        return key;
    }
    
    public String keyFromMethod(Method m) {
        if(!m.isAnnotationPresent(Id.class) &&
                !m.isAnnotationPresent(Get.class) &&
                !m.isAnnotationPresent(Put.class)) return null;
        if(m.isAnnotationPresent(Id.class)) return "_id";
        String op;
        if (m.isAnnotationPresent(Get.class)) {
            String getter = m.getAnnotation(Get.class).value();
            if(getter != null && !"".equals(getter)) return getter;
            op = "get";
        }
        else if (m.isAnnotationPresent(Put.class)) {
            String putter = m.getAnnotation(Put.class).value();
            if(putter != null && !"".equals(putter)) return putter;
            op = "set";
        }
        else {
            throw new IllegalArgumentException("@Get or @Put annotation not found: " + m);
        }
        String methodName = m.getName().substring(m.getName().indexOf(op) + op.length());
		return Pattern.compile(METHOD_NAME).matcher(methodName).replaceAll("$1_$2");
    }

    public java.lang.reflect.Field fieldFromClass(String key, Class<?> klass){
        for(java.lang.reflect.Field f : klass.getFields()){
            if(f.isAnnotationPresent(Id.class) && key.equals("_id")) return f;
            if(f.isAnnotationPresent(Field.class)){
                String fieldName = f.getAnnotation(Field.class).value();
                if(fieldName.equals(key) || f.getName().equals(key)) return f;
            }
        }
        return null;
    }

    public Method putterFromClass(String key, Class<?> klass) {
        for(Method m : klass.getMethods()){
            if (m.isAnnotationPresent(Id.class) && key.equals("_id")){
                if(m.isAnnotationPresent(Put.class)) return m;
            }
            if (m.isAnnotationPresent(Put.class)) {
                String setterName = m.getName().substring(m.getName().indexOf("set") + "set".length());
                String putKey = m.getAnnotation(Put.class).value();
                if (putKey.equals(key) || setterName.equals(key)) return m;
            }
        }
        return null;
    }

    public Method getterFromClass(String key, Class<?> klass) {
        for(Method m : klass.getMethods()){
            if (m.isAnnotationPresent(Id.class) && key.equals("_id")){
                if(m.isAnnotationPresent(Get.class)) return m;
            }
            if (m.isAnnotationPresent(Get.class)) {
                String getterName = m.getName().substring(m.getName().indexOf("get") + "get".length());
                String getKey = m.getAnnotation(Get.class).value();
                if (getKey.equals(key) || getterName.equals(key)) return m;
            }
        }
        return null;
    }

    public String collectionFromClass(Class<?> c) {
        String collection = null;
		if(c.isAnnotationPresent(Collection.class)){
            collection =  c.getAnnotation(Collection.class).value();
            if (collection == null || "".equals(collection)){
                collection = c.getSimpleName();
            }
        }
		return collection;
	}
}
