package xyz.axp.mirotest.domain.service;

import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.util.Rectangle;

import java.util.Optional;

public interface WidgetRepository {

    Widget createNew(Widget widget, boolean moveToForeground);

    Optional<Widget> getById(int id);

    void deleteById(int id);

    Optional<Widget> update(Widget widget);

    WidgetPage findAll(int offset, int limit, Rectangle boundaries);

    Optional<WidgetPage> findAllWithSnapshotId(int offset, int limit, int snapshotId, Rectangle boundaries);

}
