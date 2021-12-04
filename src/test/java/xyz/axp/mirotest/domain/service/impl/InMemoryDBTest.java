package xyz.axp.mirotest.domain.service.impl;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryDBTest {

    private Widget getWidget(int id) {
        return new Widget(id, 0,0, id, 10, 10);
    }

    @Test
    void testDb() {
        InMemoryDB db = InMemoryDB.empty();
        db = db.copyWithInsert(getWidget(1));
        db = db.copyWithInsert(getWidget(2));
        db = db.copyWithInsert(getWidget(3));
        db = db.copyWithInsert(getWidget(4));
        db = db.copyWithInsert(getWidget(5));
        db = db.copyWithInsert(getWidget(6));
        assertEquals(1, db.getWidgets().get(1).getZ());
        db = db.copyWithDelete(1);
        assertFalse(db.getWidgets().containsKey(1));
        Widget w = new Widget(2, 10, 10, 3, 10, 10);
        db = db.copyWithUpdate(w);
        assertEquals(3, db.getWidgets().get(2).getZ());
        assertEquals(4, db.getWidgets().get(3).getZ());
        assertEquals(7, db.getWidgets().get(6).getZ());
        w = new Widget(6, 10, 10, 4, 10, 10);
        db = db.copyWithUpdate(w);
        assertEquals(3, db.getWidgets().get(2).getZ());
        assertEquals(5, db.getWidgets().get(3).getZ());
        assertEquals(4, db.getWidgets().get(6).getZ());
    }

    @Test
    void getForeground() {
        InMemoryDB db = InMemoryDB.empty();
        assertEquals(0, db.getForeground());
        db = db.copyWithInsert(getWidget(1));
        assertEquals(2, db.getForeground());
    }
}