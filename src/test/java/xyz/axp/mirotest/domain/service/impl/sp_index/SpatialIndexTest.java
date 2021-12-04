package xyz.axp.mirotest.domain.service.impl.sp_index;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.util.Rectangle;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpatialIndexTest {

    @Test
    void testIndex() {
        List<Widget> widgets = List.of(
                new Widget(1, 5, 5, 1, 5, 5),
                new Widget(2, -10, 5, 2, 5, 5),
                new Widget(3, -10, -10, 3, 5, 5),
                new Widget(4, 5, -10, 4, 5, 5)
        );
        SpatialIndex index = SpatialIndex.index(widgets, 3, 1);

        List<Widget> searchResult = index.search(Rectangle.fromXYWH(0, 0, 10, 10));
        assertEquals(List.of(widgets.get(0)), searchResult);
        searchResult = index.search(Rectangle.fromXYWH(-20, -20, 100, 100));
        assertEquals(new HashSet<>(widgets), new HashSet<>(searchResult));
        searchResult = index.search(Rectangle.fromXYWH(-20, -20, 20, 20));
        assertEquals(List.of(widgets.get(2)), searchResult);
        searchResult = index.search(Rectangle.fromXYWH(0, -20, 10, 20));
        assertEquals(List.of(widgets.get(3)), searchResult);
    }

}