package com.h3osoftware.annomongo;

import com.mongodb.DBObject;
import com.mongodb.ObjectId;
import org.joda.time.DateTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 26, 2010
 * Time: 7:29:06 PM
 */
class Writer {

    private final Inference inference = new Inference();

    public Writer() { }

    public Object writeValues(Object result, DBObject dbResult){
        for(String key : dbResult.keySet()){
            writeField(result, dbResult, key);
            writeMethod(result, dbResult, key);
        }
        return result;
    }

    private void writeField(Object result, DBObject dbResult, String key){
        java.lang.reflect.Field f = inference.fieldFromClass(key, result.getClass());
        if(f == null) return;
        try {
            f.setAccessible(true);
            Object value = dbResult.get(key);
            if(value instanceof Date) value = new DateTime(value);
            if(value instanceof ObjectId) value = value.toString();
            f.set(result, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void writeMethod(Object result, DBObject dbResult, String key) {
        Method m = inference.putterFromClass(key, result.getClass());
        if(m == null) return;
        try {
            m.setAccessible(true);
            Object value = dbResult.get(key);
            if(value instanceof Date) value = new DateTime(value);
            if(value instanceof ObjectId) value = value.toString();
            m.invoke(result, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
