package xyz.axp.mirotest.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.axp.mirotest.domain.entities.Widget;

@Getter
@AllArgsConstructor
public class WidgetPage {
    private Widget[] items;
    private int offset;
    private int limit;
    private int total;
    private Integer snapshotId;
}
