package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserProductRecordService extends AbstractService<PinUserProductVisitRecord> {

    private final PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    private final UserProductInteractionService userProductInteractionService;

    @Autowired
    public UserProductRecordService(PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper, UserProductInteractionService userProductInteractionService) {
        this.pinUserProductVisitRecordMapper = pinUserProductVisitRecordMapper;
        this.userProductInteractionService = userProductInteractionService;
    }

    /**
     * 根据userId获取商品浏览记录
     *
     * @param userId 用户ID
     * @return 用户访问记录List
     */
    public List<PinUserProductVisitRecord> getUserProductVisitRecord(Integer userId) {
        PinUserProductVisitRecord pinUserProductVisitRecord = new PinUserProductVisitRecord();
        pinUserProductVisitRecord.setId(userId);
        return pinUserProductVisitRecordMapper.select(pinUserProductVisitRecord);
    }

    /**
     * 更新访问商品交互度量值
     * @param userId 用户 ID
     * @param productId 商品 ID
     * @param ipAddress IP 地址
     */
    public void recordProductVisit(Integer userId, Integer productId, String ipAddress) {
        PinUserProductVisitRecord visitRecord = new PinUserProductVisitRecord();
        visitRecord.setProductId(productId);
        visitRecord.setUserId(userId);
        visitRecord.setVisitTime(new Date());
        visitRecord.setVisitIp(ipAddress);
        save(visitRecord);
        // 更新交互度量值
        userProductInteractionService.visitProduct(userId, productId);
    }
}
