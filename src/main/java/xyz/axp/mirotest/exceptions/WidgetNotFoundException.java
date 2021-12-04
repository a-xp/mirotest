package xyz.axp.mirotest.exceptions;

public class WidgetNotFoundException extends ServiceException {

    public WidgetNotFoundException() {
    }

    public WidgetNotFoundException(String message) {
        super(message);
    }

    public WidgetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WidgetNotFoundException(Throwable cause) {
        super(cause);
    }
}
