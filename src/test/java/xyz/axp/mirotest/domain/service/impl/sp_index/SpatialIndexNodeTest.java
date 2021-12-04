package xyz.axp.mirotest.domain.service.impl.sp_index;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpatialIndexNodeTest {

    private Widget testWidget(int id) {
        return new Widget(id, 0, 0, 0, 0, 0);
    }

    @Test
    void searchSector() {
        SpatialIndexNode node = SpatialIndexNode.empty();
        Widget w1 = testWidget(1);
        Widget w2 = testWidget(2);
        Widget w3 = testWidget(3);

        node.addEntry(0, new int[]{0, 1, 2}, w1);
        node.addEntry(0, new int[]{0, 1, 3}, w2);
        node.addEntry(0, new int[]{0, 0, 1}, w3);

        List<Widget> result = node.searchSector(0, new int[]{0});
        assertEquals(Set.of(w1, w2, w3), new HashSet<>(result));
        result = node.searchSector(0, new int[]{0, 1});
        assertEquals(Set.of(w1, w2), new HashSet<>(result));
        result = node.searchSector(0, new int[]{0, 1, 2});
        assertEquals(Set.of(w1), new HashSet<>(result));
    }
}