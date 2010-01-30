package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Field;
import com.h3osoftware.annomongo.annotations.Id;
import com.h3osoftware.annomongo.annotations.Put;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import junit.framework.TestCase;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 28, 2010
 * Time: 9:15:29 PM
 */
public class WriterTest extends TestCase {

    private class WriterFixture {

        private String title;
        private String title2;

        @Field
        public String name;

        @Field("name2")
        public String notherName;

        @Put("title")
        public void setTitle(String title){
            this.title = title;    
        }

        @Put
        public void setTitle2(String title){
            this.title2 = title;
        }

        @Id
        @Field
        public String idValue;
    }

    private Writer writer;
    private DBObject result;

    public void setUp(){
        this.writer = new Writer();
        this.result = new BasicDBObject();
        this.result.put("name", "Clint");
        this.result.put("name2", "Hill");
        this.result.put("_id", "4b625d1573b2844003dc6340");
        this.result.put("title", "Mr.");
        this.result.put("Title2", "Boss Man");
    }

    public void testWriteValues(){
        WriterFixture fixture = (WriterFixture) this.writer.writeValues(new WriterFixture(), this.result);
        assertEquals(fixture.name, "Clint");
        assertEquals(fixture.notherName, "Hill");
        assertEquals(fixture.title, "Mr.");
        assertEquals(fixture.title2, "Boss Man");
        assertEquals(fixture.idValue, "4b625d1573b2844003dc6340");
    }
}
