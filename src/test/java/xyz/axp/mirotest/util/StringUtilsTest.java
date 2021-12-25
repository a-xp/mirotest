package xyz.axp.mirotest.util;

import org.junit.jupiter.api.Test;
import xyz.axp.mirotest.exceptions.InvalidWidgetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void test_formatted_code_is_correct() {
        String code = StringUtils.formatErrorCode(InvalidWidgetException.class.getSimpleName());

        assertEquals("INVALID_WIDGET", code);
    }
}