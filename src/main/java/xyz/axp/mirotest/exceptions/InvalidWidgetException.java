package xyz.axp.mirotest.exceptions;

public class InvalidWidgetException extends ServiceException {
    public InvalidWidgetException() {
    }

    public InvalidWidgetException(String message) {
        super(message);
    }

    public InvalidWidgetException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidWidgetException(Throwable cause) {
        super(cause);
    }
}
