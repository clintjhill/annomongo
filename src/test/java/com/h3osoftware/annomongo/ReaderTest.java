package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Field;
import com.h3osoftware.annomongo.annotations.Get;
import com.h3osoftware.annomongo.annotations.Id;
import com.mongodb.BasicDBObject;
import junit.framework.TestCase;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 27, 2010
 * Time: 8:54:35 AM
 */
public class ReaderTest extends TestCase {

    private class ReaderFixture {
        @Field
        public String name;

        @Field("name2")
        public String notherName;

        @Get("title")
        public String getTitle() { return "Mr."; }

        @Get
        public String getTitle2() { return "Boss Man"; }

        @Id
        @Field
        public String idValue;
    }

    private ReaderFixture fixture;
    private Reader reader;
    
    public void setUp(){
        this.reader = new Reader();
        this.fixture = new ReaderFixture();
        this.fixture.idValue = "4b625d1573b2844003dc6340";
        this.fixture.name = "Clint";
        this.fixture.notherName = "Hill";
    }

    public void testReadValues(){
         BasicDBObject result = this.reader.readValues(this.fixture);
        assertEquals(result.get("_id"), "4b625d1573b2844003dc6340");
        assertEquals(result.get("title"), "Mr.");
        assertEquals(result.get("Title2"), "Boss Man");
        assertEquals(result.get("name"), "Clint");
        assertEquals(result.get("name2"), "Hill");
    }
}
