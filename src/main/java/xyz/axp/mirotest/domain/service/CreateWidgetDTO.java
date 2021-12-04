package xyz.axp.mirotest.domain.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class CreateWidgetDTO {
    private int x, y;
    private Integer z;
    @Min(value = 1, message = "Width should be positive")
    private int width;
    @Min(value = 1, message = "Height should be positive")
    private int height;
}
