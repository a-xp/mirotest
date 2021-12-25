package xyz.axp.mirotest.domain.service.impl;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.quadtree.Quadtree;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.util.Rectangle;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetSpatialIndex {

    private final Quadtree tree;

    private WidgetSpatialIndex(Quadtree tree) {
        this.tree = tree;
    }

    public static WidgetSpatialIndex empty() {
        return new WidgetSpatialIndex(null);
    }

    public static WidgetSpatialIndex index(Collection<Widget> widgets) {
        Quadtree tree = new Quadtree();
        WidgetSpatialIndex result = new WidgetSpatialIndex(tree);
        widgets.forEach(w -> {
            result.tree.insert(envelopeFromRect(rectFromWidget(w)), w);
        });
        return result;
    }

    private static Rectangle rectFromWidget(Widget widget) {
        return Rectangle.fromXYWH(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }

    private static Envelope envelopeFromRect(Rectangle rectangle) {
        return new Envelope(rectangle.getLeft(), rectangle.getRight(), rectangle.getBottom(), rectangle.getTop());
    }

    public List<Widget> search(Rectangle boundaries) {
        if (tree == null || tree.isEmpty()) {
            return List.of();
        }
        return ((List<Widget>) tree.query(envelopeFromRect(boundaries)))
                .stream()
                .filter(w -> boundaries.contains(rectFromWidget(w)))
                .collect(Collectors.toList());
    }

}
