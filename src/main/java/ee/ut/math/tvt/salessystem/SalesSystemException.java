package ee.ut.math.tvt.salessystem;

/**
 * Base class for sales system exceptions
 * This is
 * an
 * example
 * file
 */
public class SalesSystemException extends RuntimeException {

    public SalesSystemException() {
        super();
    }

    public SalesSystemException(String message) {
        super(message);
    }

    public SalesSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
