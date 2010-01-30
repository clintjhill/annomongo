package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Field;
import com.h3osoftware.annomongo.annotations.Id;
import org.joda.time.DateTime;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 26, 2010
 * Time: 7:29:06 PM
 */
public abstract class AbstractDatabaseObject {

    @Id @Field
    public String id;

    @Field("created_at")
	public DateTime createdAt;
    
    @Field("updated_at")
    public DateTime updatedAt;
    
	public boolean save() {
        this.updatedAt = new DateTime();
        if(this.createdAt == null) this.createdAt = new DateTime();
        return Database.Save(this);
    }
}
