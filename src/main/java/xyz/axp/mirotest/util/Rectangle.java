package xyz.axp.mirotest.util;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Rectangle {

    int left, right, top, bottom;

    public static Rectangle fromXYWH(int x, int y, int w, int h) {
        return new Rectangle(x, x + w, y + h, y);
    }

    public boolean contains(Rectangle other) {
        return other.left >= left && other.right <= right && other.top <= top && other.bottom >= bottom;
    }

}
