package xyz.axp.mirotest.domain.service.impl;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.util.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WidgetSpatialIndexTest {

    private Widget widget10x10(int id, int x, int y) {
        return new Widget(id, x, y, id, 10, 10);
    }

    @Test
    void test_should_find_objects_in_area() {
        Widget w1 = widget10x10(1, 0, 0);
        Widget w2 = widget10x10(2, 5, 5);
        WidgetSpatialIndex index = WidgetSpatialIndex.index(List.of(w1, w2));

        List<Widget> result = index.search(Rectangle.fromXYWH(0, 0, 20, 20));

        assertEquals(Set.of(w1, w2), new HashSet<>(result));
    }

    @Test
    void test_should_not_find_partially_covered_objects() {
        Widget w1 = widget10x10(1, 0, 0);
        WidgetSpatialIndex index = WidgetSpatialIndex.index(List.of(w1));

        List<Widget> result = index.search(Rectangle.fromXYWH(5, 5, 10, 10));

        assertTrue(result.isEmpty());
    }

}