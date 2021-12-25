package xyz.axp.mirotest.domain.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.axp.mirotest.domain.entities.Widget;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
class InMemoryDB {

    private final static Comparator<Widget> BY_Z = Comparator.comparing(Widget::getZ);
    private Map<Integer, Widget> widgets;
    private Widget[] order;
    private WidgetSpatialIndex spatialIndex;
    private int lastId;
    private int version;

    private static void copyWithReorder(Widget[] src, Widget[] dest, int oldPos, int newPos) {
        int size = src.length;
        if (oldPos == newPos) {
            System.arraycopy(src, 0, dest, 0, size);
        } else {
            if (oldPos > newPos) {
                System.arraycopy(src, 0, dest, 0, newPos);
                System.arraycopy(src, newPos, dest, newPos + 1, oldPos - newPos);
                System.arraycopy(src, oldPos + 1, dest, oldPos + 1, size - oldPos - 1);
            } else {
                System.arraycopy(src, 0, dest, 0, oldPos);
                System.arraycopy(src, oldPos + 1, dest, oldPos, newPos - oldPos);
                System.arraycopy(src, newPos + 1, dest, newPos + 1, size - newPos - 1);
            }
        }
    }

    private static InMemoryDB create(Map<Integer, Widget> widgets, Widget[] order, int lastId, int version) {
        WidgetSpatialIndex spatialIndex = WidgetSpatialIndex.index(Arrays.asList(order));
        return new InMemoryDB(widgets, order, spatialIndex, lastId, version);
    }

    public static InMemoryDB empty() {
        return new InMemoryDB(Map.of(), new Widget[0], WidgetSpatialIndex.empty(), 0, 1);
    }

    public InMemoryDB copyWithDelete(int id) {
        if (!this.widgets.containsKey(id)) {
            return this;
        }
        int version = this.version + 1;
        int size = this.order.length - 1;
        Map<Integer, Widget> widgets = new HashMap<>(this.widgets);
        int pos = Arrays.binarySearch(order, widgets.get(id), Comparator.comparing(Widget::getZ));
        widgets.remove(id);
        Widget[] order = new Widget[size];
        System.arraycopy(this.order, 0, order, 0, pos);
        System.arraycopy(this.order, pos + 1, order, pos, size - pos);
        return create(widgets, order, this.lastId, version);
    }

    public InMemoryDB copyWithInsert(Widget widget) {
        int version = this.version + 1;
        int lastId = this.lastId + 1;
        int size = order.length + 1;
        widget.setId(lastId);
        Map<Integer, Widget> widgets = new HashMap<>(this.widgets);
        widgets.put(widget.getId(), widget);
        Widget[] order = new Widget[size];
        int insertAt = Arrays.binarySearch(this.order, widget, Comparator.comparing(Widget::getZ));
        if (insertAt < 0) {
            insertAt = -(insertAt + 1);
        }
        System.arraycopy(this.order, 0, order, 0, insertAt);
        order[insertAt] = widget;
        System.arraycopy(this.order, insertAt, order, insertAt + 1, size - 1 - insertAt);
        InMemoryDB result = create(widgets, order, lastId, version);
        result.sortAfterInsert(insertAt);
        return result;
    }

    public InMemoryDB copyWithUpdate(Widget widget) {
        if (!this.widgets.containsKey(widget.getId())) {
            return this;
        }
        int version = this.version + 1;
        int size = order.length;
        Widget old = this.widgets.get(widget.getId());
        int pos = Arrays.binarySearch(this.order, old, BY_Z);
        int newPos = pos;
        Map<Integer, Widget> widgets = new HashMap<>(this.widgets);
        widgets.put(widget.getId(), widget);
        Widget[] order = new Widget[size];
        if (widget.getZ() != old.getZ()) {
            if (widget.getZ() > old.getZ()) {
                if (pos != size - 1) {
                    newPos = ins_pos(Arrays.binarySearch(this.order, pos + 1, size, widget, BY_Z)) - 1;
                }
            } else {
                if (pos != 0) {
                    newPos = ins_pos(Arrays.binarySearch(this.order, 0, pos, widget, BY_Z));
                }
            }
        }
        copyWithReorder(this.order, order, pos, newPos);
        order[newPos] = widget;
        InMemoryDB db = create(widgets, order, this.lastId, version);
        if (widget.getZ() != old.getZ()) {
            db.sortAfterInsert(newPos);
        }
        return db;
    }

    private void sortAfterInsert(int from) {
        int size = order.length;
        for (int i = from; i < size - 1; i++) {
            Widget left = order[i];
            Widget right = order[i + 1];
            if (left.getZ() == right.getZ()) {
                pushWidget(i + 1);
            } else {
                break;
            }
        }
    }

    public int getForeground() {
        if (order.length > 0) {
            return order[order.length - 1].getZ() + 1;
        } else {
            return 0;
        }
    }

    private int ins_pos(int pos) {
        return pos < 0 ? -(pos + 1) : pos;
    }

    private void pushWidget(int pos) {
        Widget copy = order[pos].clone();
        copy.setZ(copy.getZ() + 1);
        order[pos] = copy;
        widgets.put(copy.getId(), copy);
    }
}
