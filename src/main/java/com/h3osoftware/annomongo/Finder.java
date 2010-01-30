package com.h3osoftware.annomongo;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.joda.time.DateTime;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 23, 2010
 * Time: 11:46:52 AM
 */
public class Finder {

    private Class<?> klass;
    private String field;
    private DBObject greaterThan;
    private DBObject lessThan;
    private Object equalTo;

    public Finder(Class<?> klass){
        this.klass = klass;    
    }

    public Finder where(String field){
        this.field = field;
        return this;
    }

    public Finder isEqualTo(Object o){
        this.equalTo = inferDate(o);
        return this;
    }

    public Finder isGreaterThan(Object o){
        this.greaterThan = new BasicDBObject("$gt", inferDate(o));
        return this;
    }

    public Finder isLessThan(Object o){
        this.lessThan = new BasicDBObject("$lt", inferDate(o));
        return this;
    }

    public Object result(){
        return Database.Find(klass, query());
    }

    DBObject query(){
        if(this.equalTo != null) return new BasicDBObject(this.field, this.equalTo);
        if(this.greaterThan != null) return new BasicDBObject(this.field, this.greaterThan);
        if(this.lessThan != null) return new BasicDBObject(this.field, this.lessThan);
        return new BasicDBObjectBuilder().add(this.field, null).get();
    }

    Object inferDate(Object o){
        if(o instanceof DateTime){
            return ((DateTime)o).toDate();
        }
        return o;
    }
}
