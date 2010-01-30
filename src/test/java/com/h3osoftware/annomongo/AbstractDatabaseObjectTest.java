package com.h3osoftware.annomongo;

import com.h3osoftware.annomongo.annotations.Collection;
import com.h3osoftware.annomongo.annotations.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Clint Hill
 * User: clinthill
 * Date: Jan 22, 2010
 * Time: 7:53:24 PM
 */
public class AbstractDatabaseObjectTest extends TestCase {

    @Collection("concretes")
    private class ConcreteDAO extends AbstractDatabaseObject { }

    @Collection("mixedConcretes")
    public static class MixedConcrete extends AbstractDatabaseObject {
        @Field("enumeration")
        public List Enum;
    }

    private ConcreteDAO cdao;
    private MixedConcrete mdao;

    public void setUp(){
        this.cdao = new ConcreteDAO();
        this.mdao = new MixedConcrete();
        this.mdao.Enum = Arrays.asList("Clint", "Hill");
    }

    public void testId(){
        assertNull(cdao.id);
        cdao.id = "something";
        assertEquals(cdao.id, "something");
    }

    public void testSave() {
        assertTrue(new ConcreteDAO().save());
        assertTrue(this.mdao.save());
    }

    public void testFindEnum(){
        this.mdao.save();
        DBObject query = new BasicDBObject();
        query.put("_id", this.mdao.id);
        MixedConcrete found = (MixedConcrete)Database.Find(MixedConcrete.class, query);
        assertEquals(found.id, this.mdao.id);
        assertEquals(found.Enum.size(), 2);
    }
    
}
