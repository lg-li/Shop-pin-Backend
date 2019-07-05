package cn.edu.neu.shop.pin.service.finance;

import cn.edu.neu.shop.pin.exception.InsufficientBalanceException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.exception.RepeatPaymentException;
import cn.edu.neu.shop.pin.lock.annotation.LockKeyVariable;
import cn.edu.neu.shop.pin.lock.annotation.MutexLock;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LLG
 * 支付服务
 */
@Service
public class PaymentService {

    private final UserBalanceService userBalanceService;

    private final OrderIndividualService orderIndividualService;

    public PaymentService(UserBalanceService userBalanceService, OrderIndividualService orderIndividualService) {
        this.userBalanceService = userBalanceService;
        this.orderIndividualService = orderIndividualService;
    }

    /**
     * 使用余额支付个人订单
     * 若支付成功将正常返回
     *
     * @param individualOrderId 个人订单ID
     * @param userId            当前操作用户ID
     * @throws PermissionDeniedException    权限不足（操作了他人的个人订单）
     * @throws RecordNotFoundException      记录不存在（个人订单ID无效）
     * @throws InsufficientBalanceException 余额不足（支付失败）
     */
    @Transactional
    @MutexLock(key = PinConstants.LOCK_KEY_ORDER_INDIVIDUAL)
    public void payIndividualOrderByBalance(@LockKeyVariable Integer individualOrderId, Integer userId) throws PermissionDeniedException, RecordNotFoundException, InsufficientBalanceException, RepeatPaymentException {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(individualOrderId);
        if (orderIndividual == null) {
            throw new RecordNotFoundException("指定的个人订单不存在");
        }
        if (!orderIndividual.getUserId().equals(userId)) {
            throw new PermissionDeniedException("非法操作");
        }
        if (orderIndividual.getPaid()) {
            // 已经支付 重复支付错误
            throw new RepeatPaymentException("此订单已经支付");
        }
        userBalanceService.reduceUserBalanceFromDiscountOnIndividualOrder(userId, individualOrderId, orderIndividual.getTotalPrice());
        // 余额扣款成功(不成功会抛出异常)。
        // 在此余额支付了所有款项，实际进帐金额为0元
        updateOrderIndividualPaymentStatusToSuccess(orderIndividual, orderIndividual.getTotalPrice());
    }

    private void updateOrderIndividualPaymentStatusToSuccess(PinOrderIndividual orderIndividual, BigDecimal balancePaidPrice) {
        // 余额扣款成功(不成功会抛出异常)
        orderIndividual.setPaid(true);
        orderIndividual.setPayTime(new Date());
        // 余额支付抵扣的金额
        orderIndividual.setBalancePaidPrice(balancePaidPrice);
        // 总金额-余额支付抵扣金额 = 实际进帐金额
        orderIndividual.setPayPrice(orderIndividual.getTotalPrice().subtract(balancePaidPrice));
        orderIndividual.setPayType(PinOrderIndividual.PAY_TYPE_BALANCE);
        orderIndividualService.update(orderIndividual);
    }
}
