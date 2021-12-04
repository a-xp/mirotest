package xyz.axp.mirotest.util;

import java.util.regex.Pattern;

public class StringUtils {

    private static final String EXCEPTION = "Exception";
    private static final Pattern CAPS = Pattern.compile("(?<=[a-z])([A-Z])");

    public static String formatErrorCode(String cls) {
        if (cls.endsWith(EXCEPTION)) {
            cls = cls.substring(0, cls.length() - EXCEPTION.length());
        }
        return CAPS.matcher(cls).replaceAll("_$1").toUpperCase();
    }

}
