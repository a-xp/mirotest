package xyz.axp.mirotest.domain.service.impl;

import org.springframework.stereotype.Repository;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.domain.service.WidgetPage;
import xyz.axp.mirotest.domain.service.WidgetRepository;
import xyz.axp.mirotest.util.Rectangle;

import java.util.*;

@Repository
public class InMemoryWidgetRepository implements WidgetRepository {

    private volatile InMemoryDB index = InMemoryDB.empty();
    private final Map<Integer, InMemoryDB> history = Collections.synchronizedMap(new WeakHashMap<>());

    static WidgetPage makePage(Widget[] result, int offset, int limit, int version) {
        int resSize = Math.min(result.length - offset, limit);
        Widget[] items = resSize > 0 ? Arrays.copyOfRange(result, offset, offset + resSize) : new Widget[0];
        return new WidgetPage(items, offset, limit, result.length, version);
    }

    @Override
    public Optional<Widget> getById(int id) {
        return Optional.ofNullable(index.getWidgets().get(id));
    }

    @Override
    public synchronized Widget createNew(Widget newWidget) {
        InMemoryDB index = this.index;

        InMemoryDB newIndex = index.copyWithInsert(newWidget);
        replaceIndex(newIndex);
        return newIndex.getWidgets().get(newIndex.getLastId());
    }

    @Override
    public synchronized void deleteById(int id) {
        InMemoryDB index = this.index;
        replaceIndex(index.copyWithDelete(id));
    }

    @Override
    public synchronized Optional<Widget> update(Widget widget) {
        InMemoryDB index = this.index;
        if (!index.getWidgets().containsKey(widget.getId())) {
            return Optional.empty();
        }
        InMemoryDB newIndex = index.copyWithUpdate(widget);
        replaceIndex(newIndex);
        return Optional.of(newIndex.getWidgets().get(widget.getId()));
    }

    @Override
    public WidgetPage findAll(int offset, int limit, Rectangle boundaries) {
        return getFilteredPage(this.index, boundaries, offset, limit);
    }

    @Override
    public Optional<WidgetPage> findAllWithSnapshotId(int offset, int limit, int snapshotId, Rectangle boundaries) {
        return Optional.ofNullable(history.get(snapshotId))
                .map(index -> getFilteredPage(index, boundaries, offset, limit));
    }

    @Override
    public int getForeground() {
        return index.getForeground();
    }

    WidgetPage getFilteredPage(InMemoryDB index, Rectangle boundaries, int offset, int limit) {
        if (boundaries == null) {
            return makePage(index.getOrder(), offset, limit, index.getVersion());
        } else {
            List<Widget> result = index.getSpatialIndex().search(boundaries);
            return makePage(result.toArray(Widget[]::new), offset, limit, index.getVersion());
        }
    }

    private void replaceIndex(InMemoryDB newVersion) {
        this.index = newVersion;
        history.put(newVersion.getVersion(), newVersion);
    }

}
