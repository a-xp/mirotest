package xyz.axp.mirotest.domain.service.impl;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.domain.service.WidgetPage;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWidgetRepositoryTest {

    private Widget createWidget(int id) {
        return new Widget(id, 0, 0, id, 10, 10);
    }

    @Test
    void makePage() {
        Widget[] result = new Widget[]{createWidget(1), createWidget(2), createWidget(3), createWidget(4)};
        WidgetPage page = InMemoryWidgetRepository.makePage(result, 1, 2, 1);
        assertEquals(new WidgetPage(new Widget[]{result[1], result[2]}, 1, 2, 4, 1), page);
        page = InMemoryWidgetRepository.makePage(result, 2, 5, 1);
        assertEquals(new WidgetPage(new Widget[]{result[2], result[3]}, 2, 5, 4, 1), page);
    }
}