package xyz.axp.mirotest.exceptions;

public class SnapshotNotFoundException extends ServiceException {
    public SnapshotNotFoundException() {
    }

    public SnapshotNotFoundException(String message) {
        super(message);
    }

    public SnapshotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnapshotNotFoundException(Throwable cause) {
        super(cause);
    }
}
