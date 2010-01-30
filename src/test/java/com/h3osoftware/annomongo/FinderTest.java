package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import junit.framework.TestCase;
import org.joda.time.DateTime;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 29, 2010
 * Time: 9:27:29 PM
 */
public class FinderTest extends TestCase {

    static class FinderFixture{
        @Field
        public String name;
        @Field
        public String title;
        @Field
        public DateTime now;
        @Field
        public int count;
        @Field
        public double percentage;
    }

    public void testSimpleEqualsQuery(){
        Finder query = new Finder(FinderFixture.class);
        DBObject result = query.where("name").isEqualTo("Clint").query();
        assertTrue(result.containsField("name"));
        assertEquals(result.get("name"), "Clint");
    }

    public void testSimpleGreaterThanQuery(){
        Finder query = new Finder(FinderFixture.class);
        DateTime now = new DateTime();
        DBObject result = query.where("now").isGreaterThan(now).query();
        assertTrue(result.containsField("now"));
        assertEquals(result.get("now"), new BasicDBObject("$gt",now.toDate()));
    }

    public void testSimpleLessThanQuery(){
        Finder query = new Finder(FinderFixture.class);
        int thirty = 30;
        DBObject result = query.where("count").isLessThan(thirty).query();
        assertTrue(result.containsField("count"));
        assertEquals(result.get("count"), new BasicDBObject("$lt", thirty));
    }

    public void testSaveAndFind(){
        FinderFixture fixture = new FinderFixture();
        fixture.count = 40;
        fixture.percentage = 99.999;
        fixture.name = "Clint";
        fixture.now = new DateTime().toInstant().minus(300).toDateTime();
        fixture.title = "Mr.";
        Database.Save(fixture);
        Finder query = new Finder(fixture.getClass());
        DateTime now = new DateTime();
        FinderFixture result = (FinderFixture)query.where("now").isLessThan(now).result();
        assertNotNull(result);
        assertEquals(result.count, fixture.count);
        assertEquals(result.percentage, fixture.percentage);
        assertEquals(result.name, fixture.name);
        assertEquals(result.title, fixture.title);
        assertTrue(result.now.isBefore(now));
    }
}
