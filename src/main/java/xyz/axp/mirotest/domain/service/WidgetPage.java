package xyz.axp.mirotest.domain.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.axp.mirotest.domain.entities.Widget;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class WidgetPage {
    private Widget[] items;
    private int offset;
    private int limit;
    private int total;
    private Integer snapshotId;
}
