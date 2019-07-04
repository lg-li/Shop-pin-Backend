package cn.edu.neu.shop.pin.schedule;

import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductPropertyRefresher {

    private static Logger logger = LoggerFactory.getLogger(ProductPropertyRefresher.class);

    private final PinProductMapper pinProductMapper;

    private final PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    private final PinOrderItemMapper pinOrderItemMapper;

    @Autowired
    public ProductPropertyRefresher(PinProductMapper pinProductMapper, PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper, PinOrderItemMapper pinOrderItemMapper) {
        this.pinProductMapper = pinProductMapper;
        this.pinUserProductVisitRecordMapper = pinUserProductVisitRecordMapper;
        this.pinOrderItemMapper = pinOrderItemMapper;
    }

    @Scheduled(initialDelay = 3600000, fixedRate = 3600000)
    public void updateVisitedCount() {
        logger.info("[定时任务] 刷新访问记录...");
        try {
            List<PinUserProductVisitRecord> lists = pinUserProductVisitRecordMapper.selectAll();
            PinProduct product;
            for (PinUserProductVisitRecord item : lists) {
                product = pinProductMapper.selectByPrimaryKey(item.getProductId());
                product.setVisitCount(product.getVisitCount() + 1);
                pinProductMapper.updateByPrimaryKey(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 3600000, fixedRate = 3600000)
    public void updateSoldCount() {
        logger.info("[定时任务] 刷新购买量记录...");
        try {
            List<PinOrderItem> lists = pinOrderItemMapper.selectAll();
            PinProduct product;
            for (PinOrderItem item : lists) {
                product = pinProductMapper.selectByPrimaryKey(item.getProductId());
                product.setVisitCount(product.getSoldCount() + item.getAmount());
                pinProductMapper.updateByPrimaryKey(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
