package xyz.axp.mirotest.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.axp.mirotest.exceptions.ServiceException;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Widget implements Cloneable {

    private Integer id;
    private int x, y, z;
    @Min(value = 1, message = "Width should be positive")
    private int width;
    @Min(value = 1, message = "Height should be positive")
    private int height;

    public Widget clone() {
        try {
            return (Widget) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ServiceException(e);
        }
    }
}
