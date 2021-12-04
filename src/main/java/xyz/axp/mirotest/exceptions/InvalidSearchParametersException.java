package xyz.axp.mirotest.exceptions;

public class InvalidSearchParametersException extends ServiceException {

    public InvalidSearchParametersException() {
    }

    public InvalidSearchParametersException(String message) {
        super(message);
    }

    public InvalidSearchParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSearchParametersException(Throwable cause) {
        super(cause);
    }
}
