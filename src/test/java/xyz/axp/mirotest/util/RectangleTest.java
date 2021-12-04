package xyz.axp.mirotest.util;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.util.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    @Test
    void contains() {
        Rectangle r1 = Rectangle.fromXYWH(0, 0, 10, 10);
        assertTrue(r1.contains(Rectangle.fromXYWH(0 , 0 , 5 ,5)));
        assertFalse(r1.contains(Rectangle.fromXYWH(5, 5, 10 ,10)));
    }

    @Test
    void expand() {
        Rectangle r1 = Rectangle.fromXYWH(0, 0, 10, 10);
        r1.expand(Rectangle.fromXYWH(5, 5, 10 ,10));
        assertEquals(Rectangle.fromXYWH(0 ,0 , 15, 15), r1);
    }

    @Test
    void intersect() {
        Rectangle r1 = Rectangle.fromXYWH(0, 0, 10, 10);
        Rectangle r2 = Rectangle.fromXYWH(5, 5, 10 ,10);
        assertEquals(Rectangle.fromXYWH(5, 5, 5 ,5), r1.intersect(r2));
    }
}