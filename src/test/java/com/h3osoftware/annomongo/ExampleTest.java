package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Collection;
import com.h3osoftware.annomongo.annotations.Field;
import com.h3osoftware.annomongo.annotations.Id;
import junit.framework.TestCase;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Feb 5, 2010
 * Time: 10:07:14 PM
 */
public class ExampleTest extends TestCase {

    @Collection("examples")
    public static class Example extends AbstractDatabaseObject{
        @Id
        public String id;

        @Field("name")
        public String name;
    }

    public void testSaveExample(){
        Example ex = new Example();
        ex.name = "John Doe";
        ex.save();
    }

    public void testFindExample(){
        Finder query = new Finder(Example.class);
        Example result = query.where("name").isEqualTo("John Doe").result();
        assertNotNull(result);
        assertNotNull(result.id);
        assertEquals(result.name, "John Doe");
    }
}
