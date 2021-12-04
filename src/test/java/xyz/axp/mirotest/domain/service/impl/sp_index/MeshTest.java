package xyz.axp.mirotest.domain.service.impl.sp_index;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.util.Point;
import xyz.axp.mirotest.util.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

class MeshTest {

    Mesh get3LvlMesh() {
        return new Mesh(1, 16, 16, 3, Rectangle.fromXYWH(-64,-64, 128, 128));
    }

    Mesh get1LvlMesh() {
        return new Mesh(1, 64, 64, 1, Rectangle.fromXYWH(-64,-64, 128, 128));
    }

    @Test
    void getSmallestSector() {
        Mesh mesh = get1LvlMesh();
        MeshSector sector = mesh.getSmallestSector(Rectangle.fromXYWH(10, 10 , 10, 10));
        assertArrayEquals(new int[]{3}, sector.getPath());
        sector = mesh.getSmallestSector(Rectangle.fromXYWH(-20, -20 , 10, 10));
        assertArrayEquals(new int[]{0}, sector.getPath());
        sector = mesh.getSmallestSector(Rectangle.fromXYWH(-10, -10 , 20, 20));
        assertArrayEquals(new int[]{}, sector.getPath());
    }

    /**
     *  12 = 1st cell:
     *
     *  00001111
     *  0011
     *  01
     *
     *  112 = 8th cell:
     *
     *  00001111
     *      0011
     *        01
     */
    @Test
    void encode1D() {
        int[] path = Mesh.encode1D(12, 1,16, 3);
        assertArrayEquals(new int[]{0, 0, 0}, path);
        path = Mesh.encode1D(112, 1,16, 3);
        assertArrayEquals(new int[]{1, 1, 1}, path);
    }

    /**
     * (7,7):
     * 1st split: Q1 = 3
     * 2nd split: Q3 = 0
     * 3rd split: Q3 = 0
     *
     * (-32, 1):
     * 1st split: Q4 = 2
     * 2nd split: Q2 = 1
     * 3rd split: Q3 = 0
     */
    @Test
    void encode2D() {
        Mesh mesh = get3LvlMesh();
        int[] path = mesh.encode2D(new Point(7, 7));
        assertArrayEquals(new int[]{3, 0, 0}, path);
        path = mesh.encode2D(new Point(-32, 1));
        assertArrayEquals(new int[]{2, 1 , 0}, path);
    }

    @Test
    void create() {
        Mesh mesh = Mesh.create(1, 2, Rectangle.fromXYWH(-128, -64, 200, 100));
        assertEquals(Rectangle.fromXYWH(-156, -78, 256, 128), mesh.boundaries);
        assertEquals(1, mesh.factor);
        assertEquals(64, mesh.hSectorSize);
        assertEquals(32, mesh.vSectorSize);
    }

    @Test
    void findMeshSize() {
        Mesh.Dimension dim = Mesh.findMeshSize(2, 20);
        assertEquals(64, dim.size);
        dim = Mesh.findMeshSize(3, 100);
        assertEquals(512, dim.size);
    }
}