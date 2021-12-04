package xyz.axp.mirotest.domain.service.impl.sp_index;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.util.Rectangle;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SpatialIndex {

    public static final int DEFAULT_FACTOR = 2;
    public static final int DEFAULT_LEVELS = 4;
    private final Mesh mesh;
    private final SpatialIndexNode topNode;

    public static SpatialIndex index(Collection<Widget> widgets, int levels, int factor) {
        if (widgets.isEmpty()) {
            return empty();
        }
        Rectangle boundaries = fromWidget(widgets.stream().findFirst().get());
        widgets.forEach(w -> boundaries.expand(fromWidget(w)));
        Mesh mesh = Mesh.create(factor, levels, boundaries);
        SpatialIndexNode root = SpatialIndexNode.empty();
        widgets.forEach(w -> {
            int[] path = mesh.getSmallestSector(fromWidget(w)).getPath();
            root.addEntry(0, path, w);
        });
        return new SpatialIndex(mesh, root);
    }

    public static SpatialIndex empty() {
        return new SpatialIndex(null, null);
    }

    private static Rectangle fromWidget(Widget widget) {
        return Rectangle.fromXYWH(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }

    public List<Widget> search(Rectangle boundaries) {
        if (topNode == null) {
            return List.of();
        }
        MeshSector sector = mesh.getSmallestSector(boundaries);
        if (sector.isEmpty()) {
            return List.of();
        }
        return topNode.searchSector(0, sector.getPath()).stream()
                .filter(w -> boundaries.contains(fromWidget(w)))
                .collect(Collectors.toList());
    }

}
