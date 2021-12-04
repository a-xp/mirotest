package xyz.axp.mirotest.rest.dto;

import lombok.Getter;
import lombok.Setter;
import xyz.axp.mirotest.util.Rectangle;

import javax.validation.constraints.Min;
import java.util.Optional;

@Getter
@Setter
public class SearchSpec {

    private Integer snapshotId;
    @Min(1)
    private int limit = 10;
    @Min(0)
    private int offset = 0;

    private Integer x, y;
    @Min(1)
    private Integer w;
    @Min(1)
    private Integer h;

    public Optional<Rectangle> getBoundaries() {
        return x != null && y != null && w != null && h != null ?
                Optional.of(Rectangle.fromXYWH(x, y, w, h)) : Optional.empty();
    }

}
