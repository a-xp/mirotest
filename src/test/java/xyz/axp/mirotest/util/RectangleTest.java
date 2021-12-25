package xyz.axp.mirotest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleTest {

    private Rectangle rect10x10() {
        return Rectangle.fromXYWH(0, 0, 10, 10);
    }

    private Rectangle rect10x10at5() {
        return Rectangle.fromXYWH(5, 5, 10, 10);
    }

    private Rectangle rect20x20() {
        return Rectangle.fromXYWH(0, 0, 20, 20);
    }

    @Test
    void test_contains_is_true_only_when_fully_contained() {
        Rectangle r10 = rect10x10();
        Rectangle r10at5 = rect10x10at5();
        Rectangle r20 = rect20x20();

        assertTrue(r20.contains(r10));
        assertFalse(r10.contains(r10at5));
        assertFalse(r10.contains(r20));
    }
}