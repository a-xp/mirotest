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

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return top - bottom;
    }

    public Point getBL() {
        return new Point(left, bottom);
    }

    public Point getTR() {
        return new Point(right, top);
    }

    public boolean contains(Rectangle other) {
        return other.left >= left && other.right <= right && other.top <= top && other.bottom >= bottom;
    }

    public Rectangle intersect(Rectangle other) {
        return new Rectangle(
                Math.max(left, other.left),
                Math.min(right, other.right),
                Math.min(top, other.top),
                Math.max(bottom, other.bottom)
        );
    }

    public void expand(Rectangle other) {
        if (other.left < left) {
            left = other.left;
        }
        if (other.right > right) {
            right = other.right;
        }
        if (other.top > top) {
            top = other.top;
        }
        if (other.bottom < bottom) {
            bottom = other.bottom;
        }
    }

    public boolean isEmpty() {
        return getWidth() == 0 || getHeight() == 0;
    }

}
