package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.CheckInFailedException;
import cn.edu.neu.shop.pin.mapper.PinSettingsConstantMapper;
import cn.edu.neu.shop.pin.mapper.PinUserCreditRecordMapper;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinSettingsConstant;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserCreditRecord;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserCreditRecordService {

    @Autowired
    private PinUserCreditRecordMapper pinUserCreditRecordMapper;

    @Autowired
    private PinUserMapper pinUserMapper;

    @Autowired
    private PinSettingsConstantMapper pinSettingsConstantMapper;

    /**
     * 按照需求规约中格式，获取用户签到记录
     * @param userId
     * @return
     */
    public JSONObject getUserCreditData(Integer userId) {
        JSONObject data = new JSONObject();
        PinUser user = pinUserMapper.selectByPrimaryKey(userId);
        Integer credit = user.getCredit();
        data.put("credit", credit);
        Integer checkInDays = pinUserCreditRecordMapper.getCheckInDaysNum(userId);
        data.put("checkInDays", checkInDays);
        PinUserCreditRecord p = new PinUserCreditRecord();
        p.setUserId(userId);
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.select(p);
        data.put("creditHistory", list);
        return data;
    }

    /**
     * 每日签到
     * @param userId
     * @throws Exception
     */
    public void dailyCheckIn(Integer userId) throws Exception {
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getCheckInDaysInfo(userId);
        Integer credit = Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey(new String("check_in_credit"))
                .getConstantValue());
        Integer increment = Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey(new String("check_in_credit_increment"))
                .getConstantValue());
        Integer limit = Integer.parseInt(pinSettingsConstantMapper
                .selectByPrimaryKey(new String("check_in_credit_limit"))
                .getConstantValue());
        PinUserCreditRecord record;
        if(list.size() == 0) { // 暂无签到记录
            record = new PinUserCreditRecord(userId, credit, 0, new Date(), 1);
            pinUserMapper.updateUserCredit(userId, credit);
            pinUserCreditRecordMapper.insert(record);
        }
        else {
            PinUserCreditRecord p = list.get(0);
            Date date = p.getCreateTime();
            if(isSameDay(date, new Date())) { // 今日已签到
                throw new CheckInFailedException("签到失败！请勿重复签到！");
            }
            else {
                Date yesterday = this.getYesterday(new Date());
                Integer note = isSameDay(date, yesterday) ? p.getNote() : 1;
                Integer creditVal = (credit+note*increment>limit) ? limit : credit+note*increment;
                record = new PinUserCreditRecord(userId, creditVal, 0, new Date(), note);
                pinUserCreditRecordMapper.insert(record);
            }
        }
    }

    /**
     * 判断时间是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    private boolean isNow(Date date) {
        Date now = new Date(); // 当前时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String nowDay = sf.format(now); // 获取今天的日期
        String day = sf.format(date); // 对比的时间
        return day.equals(nowDay);
    }

    /**
     * 给定今天日期，判断昨天日期
     * @param today
     * @return
     */
    public Date getYesterday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    /**
     * 判断两个Date类型的日期是否相同
     * @param date1
     * @param date2
     * @return
     */
    public Boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String day1 = sf.format(date1), day2 = sf.format(date2);
        return day1.equals(day2);
    }
}
