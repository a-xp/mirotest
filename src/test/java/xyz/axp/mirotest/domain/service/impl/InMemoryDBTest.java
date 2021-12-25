package xyz.axp.mirotest.domain.service.impl;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDBTest {

    private Widget widget10x10at0(int id) {
        return new Widget(id, 0, 0, id, 10, 10);
    }

    private Widget widget20x20at5(int id, int z) {
        return new Widget(id, 5, 5, z, 20, 20);
    }

    private void assertWidgetEquals(Widget expected, Widget found) {
        assertNotNull(found);
        assertEquals(expected.getZ(), found.getZ());
        assertEquals(expected.getX(), found.getX());
        assertEquals(expected.getY(), found.getY());
        assertEquals(expected.getWidth(), found.getWidth());
        assertEquals(expected.getHeight(), found.getHeight());
    }

    private InMemoryDB dbWith6Widgets() {
        InMemoryDB db = InMemoryDB.empty();
        db = db.copyWithInsert(widget10x10at0(1));
        db = db.copyWithInsert(widget10x10at0(2));
        db = db.copyWithInsert(widget10x10at0(3));
        db = db.copyWithInsert(widget10x10at0(4));
        db = db.copyWithInsert(widget10x10at0(5));
        db = db.copyWithInsert(widget10x10at0(6));
        return db;
    }

    @Test
    void test_should_find_inserted_widget() {
        InMemoryDB db = dbWith6Widgets();
        Widget w = widget20x20at5(7, 7);

        db = db.copyWithInsert(w);

        assertWidgetEquals(w, db.getWidgets().get(w.getId()));
    }

    @Test
    void test_should_not_find_deleted_widget() {
        InMemoryDB db = dbWith6Widgets();
        int id = 1;

        db = db.copyWithDelete(id);

        assertFalse(db.getWidgets().containsKey(id));
    }

    @Test
    void test_should_find_updated_widget_with_updated_fields() {
        InMemoryDB db = dbWith6Widgets();
        Widget w = widget20x20at5(1, 7);

        db = db.copyWithUpdate(w);

        assertWidgetEquals(w, db.getWidgets().get(w.getId()));
    }

    @Test
    void test_should_push_widgets_with_the_same_z_up() {
        InMemoryDB db = dbWith6Widgets();
        Widget w = widget20x20at5(7, 3);

        db = db.copyWithInsert(w);

        assertEquals(2, db.getWidgets().get(2).getZ());
        assertEquals(4, db.getWidgets().get(3).getZ());
        assertEquals(5, db.getWidgets().get(4).getZ());
        assertEquals(6, db.getWidgets().get(5).getZ());
        assertEquals(7, db.getWidgets().get(6).getZ());
    }

    @Test
    void test_should_return_foreground_bigger_than_largest_z() {
        InMemoryDB db = dbWith6Widgets();
        Widget w = widget20x20at5(7, db.getForeground());

        db = db.copyWithInsert(w);
        assertTrue(w.getZ() < db.getForeground());
    }
}