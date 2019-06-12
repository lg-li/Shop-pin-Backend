package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface PinOrderIndividualMapper extends BaseMapper<PinOrderIndividual> {
    List<PinOrderIndividual> selectByOrderGroupId(Integer groupId);

    List<PinOrderIndividual> getRecentThreeMonthsOrderIndividuals(Integer userId);

    List<PinOrderIndividual> getAllWithProductsByKeyWord(@Param("keyWord") String keyWord,@Param("storeId") Integer storeId);

    Integer getNumberOfOrder(Date fromTime, Date toTime, Integer storeId);

    Integer getNumberOfOrderNotShip(Integer storeId);

    Integer getNumberOfOrderRefund(Integer storeId);

    void updateOrderDeliveryTypeNotExpress(Integer orderIndividualId, String deliveryType, Date deliveryTime);

    void updateOrderDeliveryTypeIsExpress(Integer orderIndividualId, String deliveryType, String deliveryName, Integer deliveryId, Date deliveryTime);

    void updateMerchantRemark(Integer orderIndividualId, String merchantRemark);
}
