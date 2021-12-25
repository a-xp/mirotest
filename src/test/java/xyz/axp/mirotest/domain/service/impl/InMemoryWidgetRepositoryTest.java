package xyz.axp.mirotest.domain.service.impl;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.domain.service.WidgetPage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryWidgetRepositoryTest {

    private Widget widget10x10at0(int id) {
        return new Widget(id, 0, 0, id, 10, 10);
    }

    @Test
    void test_should_apply_offset_correctly() {
        Widget[] result = new Widget[]{widget10x10at0(1), widget10x10at0(2), widget10x10at0(3), widget10x10at0(4)};
        int offset = 2;
        int limit = 10;
        int version = 1;

        WidgetPage page = InMemoryWidgetRepository.makePage(result, offset, limit, version);

        assertArrayEquals(new Widget[]{result[2], result[3]}, page.getItems());
        assertEquals(offset, page.getOffset());
        assertEquals(limit, page.getLimit());
        assertEquals(version, page.getSnapshotId());
    }

    @Test
    void test_should_return_page_with_size_lower_than_limit() {
        Widget[] result = new Widget[]{widget10x10at0(1), widget10x10at0(2), widget10x10at0(3), widget10x10at0(4), widget10x10at0(5)};

        WidgetPage page = InMemoryWidgetRepository.makePage(result, 0, 2, 1);

        assertEquals(2, page.getItems().length);
    }
}