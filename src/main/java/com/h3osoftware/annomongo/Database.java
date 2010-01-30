package com.h3osoftware.annomongo;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 26, 2010
 * Time: 7:29:06 PM
 */
public class Database {
	
	private static DB database;
    private static final Reader reader;
    private static final Writer writer;
    private static final Inference inference;

    static {
        reader = new Reader();
        writer = new Writer();
        inference = new Inference();
    }

	public synchronized static void configure(Config config){
		try {
            Mongo mongo = new Mongo(config.host, config.port);
			database = mongo.getDB(config.database);
            if(config.username != null && config.password != null) {
                boolean loggedIn = database.authenticate(config.username, config.password.toCharArray());
                if(!loggedIn){
                    throw new MongoException("Failed to authenticate to " + config.database + " with username " + config.username);
                }
            }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

    private synchronized static DB instance(){
        if(database == null) configure(Config.defaults());
        return database;
    }

    public static Object Find(Class<?> klass, DBObject query) {
        if(query.keySet().size() <= 0) return null;
        query = checkForObjectId(query);
        DBCollection coll = instance().getCollection(inference.collectionFromClass(klass));
        DBObject result = coll.findOne(query);
        Object resultObj = null;
        if(result != null) {
            try {
                resultObj = writer.writeValues(klass.newInstance(), result);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultObj;
    }
	
	public static boolean Save(Object o){
		if (o == null) return false;
		BasicDBObject dbObj = reader.readValues(o);
        String collection = inference.collectionFromClass(o.getClass());
        if (collection == null) return false;
		DBCollection coll = instance().getCollection(collection);
		coll.save(dbObj);
        if(o instanceof AbstractDatabaseObject){
		    ((AbstractDatabaseObject)o).id = dbObj.get("_id").toString();
        }
        return dbObj.get("_id") != null;
	}

    private static DBObject checkForObjectId(DBObject query){
        for(String key : query.keySet()){
            if(key.equals("_id")){
                String id = query.get(key).toString();
                query.put("_id", new ObjectId(id));
            }
        }
        return query;
    }
    
    public static class Config{
		
		final String database, host, username, password;
		final int port;
		
		public Config(String host, int port, String database, String username, String password){
			this.database = database;
			this.host = host;
			this.port = port;
            this.username = username;
            this.password = password;
		}
		
		public static Config defaults(){
			return new Config("localhost",27017,"h3osoftware", null, null);
		}
    }
}
