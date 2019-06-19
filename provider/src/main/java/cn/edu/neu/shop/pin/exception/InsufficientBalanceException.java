package cn.edu.neu.shop.pin.exception;

/**
 * @author LLG
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
