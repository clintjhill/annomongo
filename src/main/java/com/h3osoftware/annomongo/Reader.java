package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Put;
import com.mongodb.BasicDBObject;
import com.mongodb.ObjectId;
import org.joda.time.DateTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 26, 2010
 * Time: 9:02:28 AM
 */
class Reader {

    private final Inference inference = new Inference();

    public Reader() { }

    public BasicDBObject readValues(Object o){
		Map<String, Object> values = new HashMap<String, Object>();
        readFields(o, values);
        readMethods(o, values);
        BasicDBObject dbObj = new BasicDBObject();
		for(String key : values.keySet()){ dbObj.put(key, values.get(key)); }
        return dbObj;
	}

    private void readFields(Object o, Map<String, Object> values) {
        for(java.lang.reflect.Field f : o.getClass().getFields()){
            String key = inference.keyFromField(f);
            if(key == null) continue;
            Object value = null;
            try {
                f.setAccessible(true);
                value = f.get(o);
                if(value instanceof DateTime) value = ((DateTime)value).toDate();
                if(key.equals("_id")){
                    value = (value != null) ? new ObjectId(value.toString()) : ObjectId.get();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            values.put(key, value);
        }
    }

    private void readMethods(Object o, Map<String, Object> values) {
        for(Method m : o.getClass().getMethods()){
            if(m.isAnnotationPresent(Put.class)) continue;
            String key = inference.keyFromMethod(m);
            if(key == null) continue;
            Object value = null;
            try {
                m.setAccessible(true);
                value = m.invoke(o);
                if(value instanceof DateTime) value = ((DateTime)value).toDate();
                if(key.equals("_id")){
                    value = (value != null) ? new ObjectId(value.toString()) : ObjectId.get();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            values.put(key, value);
        }
    }
}
