package xyz.axp.mirotest.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Point {

    private int x, y;

    public Point relativeTo(Point point) {
        return new Point(x - point.x, y - point.y);
    }

    public Point exclusiveRange() {
        return new Point(x - 1, y - 1);
    }

}
