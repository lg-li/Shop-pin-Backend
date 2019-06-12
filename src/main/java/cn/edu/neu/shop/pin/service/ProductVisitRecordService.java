package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class ProductVisitRecordService {

    private final PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    private final ProductService productService;

    public ProductVisitRecordService(PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper, ProductService productService) {
        this.pinUserProductVisitRecordMapper = pinUserProductVisitRecordMapper;
        this.productService = productService;
    }

    /**
     * 获取某一店铺的访问记录
     * @param storeId 店铺ID
     * @return 访问记录
     */
    public Integer[] getVisitRecords(Integer storeId) {
        Integer[] record = new Integer[7];
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

    /**
     * 添加一条商品浏览记录
     * @param userId 用户ID
     * @param productId 商品ID
     * @param visitTime 访问时间
     * @param visitIp 访问IP
     */
    @Transactional
    public void createVisitRecord(Integer userId, Integer productId, Date visitTime, String visitIp) {
        // 在浏览记录表中插入一条新记录
        PinUserProductVisitRecord productVisitRecord = new PinUserProductVisitRecord();
        productVisitRecord.setUserId(userId);
        productVisitRecord.setProductId(productId);
        productVisitRecord.setVisitTime(visitTime);
        productVisitRecord.setVisitIp(visitIp);
        pinUserProductVisitRecordMapper.insert(productVisitRecord);
        // 更新商品信息，使其浏览量+1 TODO: 为保证一致性，需要改进改变访问量的写法
        PinProduct product = productService.findById(productId);
        product.setVisitCount(product.getVisitCount()+1);
        productService.update(product);
    }

    /**
     * 根据给定日期和给定偏移天数，获取目标日期
     * @param today 给定日期
     * @param delta 偏移天数
     * @return 目标日期
     */
    private java.util.Date getDateByOffset(java.util.Date today, Integer delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delta);
        return calendar.getTime();
    }
}
