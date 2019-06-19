package cn.edu.neu.shop.pin.exception;

/**
 * @author LLG
 * 重复支付异常
 * 在支付一个已经支付过的订单或一个已经加锁的订单时抛出
 */
public class RepeatPaymentException extends Exception {
    public RepeatPaymentException(String message) {
        super(message);
    }
}
