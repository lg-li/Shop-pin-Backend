package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.CheckInFailedException;
import cn.edu.neu.shop.pin.mapper.PinSettingsConstantMapper;
import cn.edu.neu.shop.pin.mapper.PinUserCreditRecordMapper;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserCreditRecord;
import cn.edu.neu.shop.pin.service.security.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserCreditRecordService {

    private final PinUserCreditRecordMapper pinUserCreditRecordMapper;

    private final PinUserMapper pinUserMapper;

    private final PinSettingsConstantMapper pinSettingsConstantMapper;

    private final UserService userService;

    public UserCreditRecordService(PinUserCreditRecordMapper pinUserCreditRecordMapper, PinUserMapper pinUserMapper, PinSettingsConstantMapper pinSettingsConstantMapper, UserService userService) {
        this.pinUserCreditRecordMapper = pinUserCreditRecordMapper;
        this.pinUserMapper = pinUserMapper;
        this.pinSettingsConstantMapper = pinSettingsConstantMapper;
        this.userService = userService;
    }

    /**
     * 按照需求规约中格式，获取用户签到记录
     *
     * @param userId 用户ID
     * @return 响应JSON
     */
    public JSONObject getUserCreditData(Integer userId) {
        JSONObject data = new JSONObject();
        PinUser user = pinUserMapper.selectByPrimaryKey(userId);
        Integer credit = user.getCredit();
        data.put("credit", credit);
        Integer totalCheckInDays = pinUserCreditRecordMapper.getCheckInDaysNum(userId);
        data.put("totalCheckInDays", totalCheckInDays);
        Integer continuousCheckInDays = getContinuousCheckInDaysNum(userId);
        data.put("continuousCheckInDays", continuousCheckInDays);
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getUserCreditRecordByDateDesc(userId);
        data.put("creditHistory", list);
        return data;
    }

    /**
     * 每日签到
     *
     * @param userId 用户ID
     * @throws Exception 异常
     */
    public void dailyCheckIn(Integer userId) throws Exception {
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getCheckInDaysInfo(userId);
        Integer creditValue = getCreditValueInDatabase();
        Integer incrementValue = getIncrementValueInDatabase();
        Integer limitValue = getLimitValueInDatabase();
        PinUserCreditRecord record;
        PinUser user = pinUserMapper.selectByPrimaryKey(userId);
        if (list.size() == 0) { // 暂无签到记录
            record = new PinUserCreditRecord(userId, creditValue, PinUserCreditRecord.TYPE_FROM_CHECK_IN, new Date(), 1);
            // 插入一条积分变更记录
            pinUserCreditRecordMapper.insert(record);
            // 更新用户积分
            System.out.println("user: " + user.getId() + " origin credit: " + user.getCredit());
            user.setCredit(creditValue + user.getCredit());
            userService.update(user);
            user = pinUserMapper.selectByPrimaryKey(userId);
            System.out.println("after update: " + user.getCredit());
        } else if (hasCheckedIn(userId)) { // 今日已签到
            throw new CheckInFailedException("您已签到，明天再来吧。");
        } else {
            this.getYesterday(new Date());
            Integer note = getContinuousCheckInDaysNum(userId);
            Integer deltaCredit = (creditValue + note * incrementValue > limitValue) ? limitValue : creditValue + note * incrementValue;
            record = new PinUserCreditRecord(userId, deltaCredit, PinUserCreditRecord.TYPE_FROM_CHECK_IN, new Date(), note + 1);
            // 插入一条积分变更记录
            pinUserCreditRecordMapper.insert(record);
            // 更新用户积分
            user.setCredit(deltaCredit + user.getCredit());
            userService.update(user);
        }
    }

    /**
     * 判断今天是否签到了
     *
     * @param userId 用户ID
     * @return 是否已签到
     */
    public Boolean hasCheckedIn(Integer userId) {
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getCheckInDaysInfo(userId);
        if (list.size() == 0) {
            return false;
        } else {
            PinUserCreditRecord p = list.get(0);
            Date date = p.getCreateTime();
            return isTheSameDay(date, new Date());
        }
    }

    /**
     * 获取某一用户的连续签到天数（用到了DP思想）
     *
     * @param userId 用户ID
     * @return 连续签到天数
     */
    private Integer getContinuousCheckInDaysNum(Integer userId) {
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getCheckInDaysInfo(userId);
        if (list.size() == 0) {
            return 0;
        } else {
            PinUserCreditRecord p = list.get(0);
            Date date = p.getCreateTime(), yesterday = this.getYesterday(new Date());
            return (isTheSameDay(date, new Date()) || isTheSameDay(date, yesterday))
                    ? p.getNote() : 1;
        }
    }

    /**
     * 获取Constant表中的check_in_credit值，为了简化代码
     *
     * @return Constant表中的check_in_credit值
     */
    private Integer getCreditValueInDatabase() {
        return Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey("check_in_credit")
                .getConstantValue());
    }

    /**
     * 获取Constant表中的check_in_credit_increment值，为了简化代码
     *
     * @return Constant表中的check_in_credit_increment值
     */
    private Integer getIncrementValueInDatabase() {
        return Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey("check_in_credit_increment")
                .getConstantValue());
    }

    /**
     * 获取Constant表中的check_in_credit_limit值，为了简化代码
     *
     * @return Constant表中的check_in_credit_limit值
     */
    private Integer getLimitValueInDatabase() {
        return Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey("check_in_credit_limit")
                .getConstantValue());
    }

    /**
     * 给定今天日期，判断昨天日期
     *
     * @param today 今天日期
     * @return 昨天日期
     */
    private Date getYesterday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    /**
     * 判断两个Date类型的日期是否相同
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否是同一天
     */
    private Boolean isTheSameDay(Date date1, Date date2) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String day1 = sf.format(date1), day2 = sf.format(date2);
        return day1.equals(day2);
    }
}
