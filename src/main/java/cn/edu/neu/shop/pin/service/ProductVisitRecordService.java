package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ProductVisitRecordService {

    @Autowired
    private PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    public Integer[] getVisitRecords(Integer storeId) {
        Integer record[] = new Integer[7];
        Date date = new Date();
        date = getDateByOffset(date, 1);
        for(int i = 0; i < 7; i++) {
            Date toDate = date;
            date = getDateByOffset(date, -1);
            Date fromDate = date;
            record[i] = pinUserProductVisitRecordMapper.getNumberOfVisitRecord(fromDate, toDate, storeId);
        }
        return record;
    }

    private java.util.Date getDateByOffset(java.util.Date today, Integer delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delta);
        return calendar.getTime();
    }
}
