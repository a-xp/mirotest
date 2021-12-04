package xyz.axp.mirotest.domain.service.impl.sp_index;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import xyz.axp.mirotest.util.Point;
import xyz.axp.mirotest.util.Rectangle;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Mesh {

    int factor;
    int hSectorSize;
    int vSectorSize;
    int numLvl;
    Rectangle boundaries;

    static int[] encode1D(int val, int factor, int baseSize, int numLvl) {
        Assert.isTrue(val >= 0, "Invalid usage: value should be positive");
        int[] result = new int[numLvl];
        val = val / baseSize;
        int lvlWidth = 1 << factor;
        for (int i = numLvl - 1; i >= 0; i--) {
            result[i] = val % lvlWidth;
            val >>= factor;
        }
        return result;
    }

    public static Mesh create(int factor, int maxLvl, Rectangle boundaries) {
        Assert.isTrue(factor > 0, "Invalid usage: factor should be positive");
        Assert.isTrue(maxLvl > 0, "Invalid usage: numLvl should be positive");
        Mesh mesh = new Mesh();
        Point bl = boundaries.getBL();
        Dimension dw = findMeshSize(factor, boundaries.getWidth());
        Dimension dh = findMeshSize(factor, boundaries.getHeight());
        int hOffset = (dw.size - boundaries.getWidth()) / 2;
        int vOffset = (dh.size - boundaries.getHeight()) / 2;
        mesh.boundaries = Rectangle.fromXYWH(bl.getX() - hOffset, bl.getY() - vOffset, dw.size, dh.size);
        mesh.factor = factor;
        mesh.numLvl = IntStream.of(maxLvl, dw.depth, dh.depth).min().getAsInt();
        mesh.hSectorSize = dw.size / (factor << mesh.numLvl);
        mesh.vSectorSize = dh.size / (factor << mesh.numLvl);
        return mesh;
    }

    static Dimension findMeshSize(int factor, int regionSize) {
        int meshSize = 1;
        int depth = 0;
        while (meshSize < regionSize) {
            meshSize <<= factor;
            depth++;
        }
        return new Dimension(meshSize, depth);
    }

    public MeshSector getSmallestSector(Rectangle rectangle) {
        rectangle = rectangle.intersect(boundaries);
        if (rectangle.isEmpty()) {
            return new MeshSector(null, true);
        }
        int[] bl = encode2D(rectangle.getBL());
        int[] tr = encode2D(rectangle.getTR().exclusiveRange());
        int[] path = IntStream.range(0, numLvl).takeWhile(i -> bl[i] == tr[i])
                .map(i -> bl[i]).toArray();
        return new MeshSector(path, false);
    }

    int[] encode2D(Point point) {
        Point rel = point.relativeTo(boundaries.getBL());
        int[] h = encode1D(rel.getX(), factor, hSectorSize, numLvl);
        int[] v = encode1D(rel.getY(), factor, vSectorSize, numLvl);
        int[] result = new int[numLvl];
        int line = 1 << factor;
        for (int i = 0; i < numLvl; i++) {
            result[i] = v[i] * line + h[i];
        }
        return result;
    }

    @AllArgsConstructor
    static class Dimension {
        int size;
        int depth;
    }

}
