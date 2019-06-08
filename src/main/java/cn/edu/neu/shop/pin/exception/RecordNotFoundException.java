package cn.edu.neu.shop.pin.exception;

/**
 * @author ydy
 */
public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    public RecordNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
