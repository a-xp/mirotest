package xyz.axp.mirotest.domain.service;

import org.springframework.stereotype.Service;
import xyz.axp.mirotest.domain.entities.Widget;
import xyz.axp.mirotest.exceptions.SnapshotNotFoundException;
import xyz.axp.mirotest.exceptions.WidgetNotFoundException;
import xyz.axp.mirotest.util.Rectangle;

@Service
public class WidgetService {

    private final WidgetRepository repository;

    public WidgetService(WidgetRepository repository) {
        this.repository = repository;
    }

    public Widget getById(int id) {
        return repository.getById(id).orElseThrow(WidgetNotFoundException::new);
    }

    public Widget createNew(Widget widget) {
        return repository.createNew(widget);
    }

    public Widget update(int id, Widget widget) {
        widget.setId(id);
        return repository.update(widget).orElseThrow(WidgetNotFoundException::new);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public WidgetPage findAll(int offset, int limit, Rectangle boundaries) {
        return repository.findAll(offset, limit, boundaries);
    }

    public WidgetPage findAllInSnapshot(int snapshotId, int offset, int limit, Rectangle boundaries) {
        return repository.findAllWithSnapshotId(offset, limit, snapshotId, boundaries)
                .orElseThrow(SnapshotNotFoundException::new);
    }

}
