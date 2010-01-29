package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Collection;
import com.h3osoftware.annomongo.annotations.Get;
import com.h3osoftware.annomongo.annotations.Put;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class DatabaseTest extends TestCase {

    static class Methodless { }

    @Collection("Keyed")
    static class MethodsKeyedAnnotations extends AbstractDatabaseObject {

        private String name;

        public MethodsKeyedAnnotations() { }

        @Put("Name")
        public void setName(String name) {
            this.name = name;
        }
        @Get("Name")
        public String getName() { return name; }
    }

    public void setUp(){
        Database.configure(Database.Config.defaults());
    }
	
	public void testDatabaseConfigDefault(){
		assertEquals(27017, Database.Config.defaults().port);
		assertEquals("localhost", Database.Config.defaults().host);
		assertEquals("h3osoftware", Database.Config.defaults().database);
	}
	
	public void testSaveFailsOnMethodless(){
		assertFalse(Database.Save(new Methodless()));
	}
	
	public void testSaveFailsOnCollectionless(){
		assertFalse(Database.Save(new Methodless()));
	}
	
	public void testSaveFailsOnNull() {
		assertFalse(Database.Save(null));
	}
	
	public void testSave(){
        MethodsKeyedAnnotations m = new MethodsKeyedAnnotations();
        m.setName("Clint");
		assertTrue(Database.Save(m));
	}

    public void testFind(){
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("Name", "Clint");
        MethodsKeyedAnnotations m = (MethodsKeyedAnnotations)Database.Find(MethodsKeyedAnnotations.class, query);
        assertNotNull(m);
        assertEquals(m.getName(), "Clint");
    }

}
