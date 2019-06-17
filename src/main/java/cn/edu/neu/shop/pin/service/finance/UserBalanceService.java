package cn.edu.neu.shop.pin.service.finance;

import cn.edu.neu.shop.pin.exception.InsufficientBalanceException;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserBalanceRecord;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LLG
 * 用户余额操作服务
 */
@Service
public class UserBalanceService extends AbstractService<PinUserBalanceRecord> {

    private final PinUserMapper userMapper;

    public UserBalanceService(PinUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 扣除用户余额（用于抵扣支付订单）
     * @param userId 用户ID
     * @param fromOrderIndividualId 个人订单
     * @param balanceToReduce 减少金额（正数）
     */
    @Transactional
    void reduceUserBalanceFromDiscountOnIndividualOrder(Integer userId, Integer fromOrderIndividualId, BigDecimal balanceToReduce) throws InsufficientBalanceException {
        PinUser userToOperate = userMapper.findById(userId);
        // 减少用户记录的余额
        userToOperate.setBalance(userToOperate.getBalance().subtract(balanceToReduce));
        if(userToOperate.getBalance().compareTo(new BigDecimal(0))<0) {
            throw new InsufficientBalanceException("支付余额不足；用户ID=" + userId + "；差额=" + userToOperate.getBalance());
        }
        // 更新数据库记录
        userMapper.updateByPrimaryKey(userToOperate);
        // 动账操作
        PinUserBalanceRecord newBalanceRecord = getNewBalanceRecord(userId, fromOrderIndividualId);
        // 减少金额需要取反

        newBalanceRecord.setChangedAmount(balanceToReduce.negate());
        newBalanceRecord.setType(PinUserBalanceRecord.TYPE_DISCOUNT_ON_ORDER);
        save(newBalanceRecord);
    }

    /**
     * 拼团返现到用户余额
     * @param userId 用户ID
     * @param fromOrderIndividualId 个人订单
     * @param bonusAmount 返现金额（正数）
     */
    @Transactional
    void returnBonusOnBalanceFromIndividualOrder(Integer userId, Integer fromOrderIndividualId, BigDecimal bonusAmount) {
        PinUser userToOperate = userMapper.findById(userId);
        // 增加用户记录的余额
        userToOperate.setBalance(userToOperate.getBalance().add(bonusAmount));
        // 更新数据库记录
        userMapper.updateByPrimaryKey(userToOperate);
        // 动账操作
        PinUserBalanceRecord newBalanceRecord = getNewBalanceRecord(userId, fromOrderIndividualId);
        // 返现金额类型
        newBalanceRecord.setChangedAmount(bonusAmount);
        newBalanceRecord.setType(PinUserBalanceRecord.TYPE_RETURN_BONUS);
        save(newBalanceRecord);
    }

    private PinUserBalanceRecord getNewBalanceRecord(Integer userId, Integer fromOrderIndividualId) {
        PinUserBalanceRecord newBalanceRecord = new PinUserBalanceRecord();
        newBalanceRecord.setUserId(userId);
        newBalanceRecord.setOrderIndividualId(fromOrderIndividualId);
        newBalanceRecord.setCreateTime(new Date());
        return newBalanceRecord;
    }


}
