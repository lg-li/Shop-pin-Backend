package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinOrderGroupMapper;
import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import cn.edu.neu.shop.pin.websocket.CustomerPrincipal;
import cn.edu.neu.shop.pin.websocket.WebSocketService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author flyhero
 * @Description 获取OrderGroup（当前团单信息）表中的内容
 */
@Service
public class OrderGroupService extends AbstractService<PinOrderGroup> {

    public static final int STATUS_CREATE_ORDER_GROUP_SUCCESS = 0;
    public static final int STATUS_CREATE_ORDER_GROUP_INVALID_ID = -1;
    public static final int STATUS_CREATE_ORDER_GROUP_PERMISSION_DENIED = -2;
    public static final int STATUS_CREATE_ORDER_GROUP_NOT_ALLOWED = -3;

    @Autowired
    private PinOrderIndividualMapper individualMapper;

    @Autowired
    private PinOrderGroupMapper pinOrderGroupMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderIndividualService orderIndividualService;

    @Autowired
    private OrderGroupService orderGroupService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    /**
     * 传入orderGroup 返回拼单的人
     * @param orderGroup orderGroup
     * @return 拼单的人的list
     */
    public List<PinUser> getUsersByOrderGroup(PinOrderGroup orderGroup) {
        List<PinOrderIndividual> individuals = individualMapper.selectByOrderGroupId(orderGroup.getId());
        return orderIndividualService.getUsers(individuals);
    }

    /**
     * @author flyhero
     * 新建一个团单
     * @param orderIndividualId
     */
    public Integer createOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        if(userId != orderIndividual.getUserId()) {
            return STATUS_CREATE_ORDER_GROUP_PERMISSION_DENIED;
        }
        if(storeId != orderIndividual.getStoreId()) {
            return STATUS_CREATE_ORDER_GROUP_INVALID_ID;
        }
        // 团单创建失败的情况：1.orderGroupId不为空 2.isPaid不是未付款 3.status不是0-待发货
        if(orderIndividual.getOrderGroupId() != null
        || orderIndividual.getPaid() != true
        || orderIndividual.getStatus() != 0) {
            return STATUS_CREATE_ORDER_GROUP_NOT_ALLOWED;
        }
        PinOrderGroup orderGroup = new PinOrderGroup();
        orderGroup.setOwnerUserId(userId);
        orderGroup.setStoreId(storeId);
        orderGroup.setCreateTime(new Date());
        // TODO: 李林根好香
        orderGroup.setCloseTime(getOrderGroupCloseTime(storeId));
        orderGroupService.save(orderGroup);
        return STATUS_CREATE_ORDER_GROUP_SUCCESS;
    }

    public void joinOrderGroup(Integer userId) {

    }

    /**
     * Stomp 初始化页面消息
     * @param customerPrincipal 客户principal
     */
    public void sendGroupInitMessageToSingle(CustomerPrincipal customerPrincipal) {
        PinOrderGroup orderGroup = orderGroupService.findById(customerPrincipal.getOrderGroupId());
        PinOrderIndividual orderIndividual = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
        if (orderGroup == null || orderIndividual == null) {
            webSocketService.sendSingleErrorMessage(customerPrincipal,
                    ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, PinConstants.ResponseMessage.INVALID_DATA, null));
            return;
        }
//        String key = GlobalData.generateKey(customerPrincipal.getGroupOrderId(), orderGroup.getAddressId(), orderGroup.getRestaurantId());
        JSONObject returnJSON = (JSONObject) JSONObject.toJSON(orderGroup);
        // 团单结束时间
        returnJSON.put("groupCloseTime", orderGroup.getCloseTime());
        // 个人订单详情
//        // 计算拼团后的实际价格
//        returnJSON.put("myActualPrice", orderIndividual.getActualPrice().subtract(BonusRule.calculateBonus(orderIndividualService.countPeopleInGroup(orderGroup.getId()))));
        // 原始价格
        returnJSON.put("myOriginalPrice", orderIndividual.getTotalPrice());//数据库的 Actual price 是卖的价格
        // 是否已完成支付
        returnJSON.put("isPaid", orderIndividual.getPaid());
        returnJSON.put("orderItems", orderItemService.getOrderItemsByOrderIndividualId(customerPrincipal.getOrderIndividualId()));
        // 团内用户信息
        List<PinUser> users = userService.getUsersByOrderGroupId(customerPrincipal.getOrderGroupId());
        if (users != null) {
            returnJSON.put("users", users);
        } else {
            webSocketService.sendSingleErrorMessage(customerPrincipal,
                    ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null));
            return;
        }
//        returnJSON.put("key", key); // 应该是和锁有关的，后续再加
//        if (newCustomer == null)
//            stompService.sendSingleNotifyMessage(customerPrincipal, "有人退出当前饭团！");
//        else if (!customerPrincipal.getCustomerId().equals(newCustomer.getCustomerId()))
//            stompService.sendSingleNotifyMessage(customerPrincipal, "有人加入当前饭团！");
        webSocketService.sendSingleHelloMessage(customerPrincipal, returnJSON);
    }

    /**
     * @author flyhero
     * 设置拼团的截止时间
     * @param storeId
     * @return
     */
    private Date getOrderGroupCloseTime(Integer storeId) {

        return new Date();
    }

//    private Date getGroupCloseTimeFromNow(Integer restaurantId) {
//        // 获设置拼团时间
//        RestaurantDeliveryBatch recentBatch = restaurantDeliveryBatchService.getRecentDeliveryBatchByRestaurant(restaurantId);
//        long timeSecondsStampOfClosing;
//        if(recentBatch == null) {
//            // 向下取整到整点分钟
//            // 未指定配送批次则10分钟后收团
//            timeSecondsStampOfClosing = ((System.currentTimeMillis() + GROUP_CLOSE_DELAY_MILLISECOND) / 60000);
//            timeSecondsStampOfClosing *= 60000; // 恢复大小到毫秒
//        } else {
//            // 已指定配送批次，选择最近时间收团
//            //Calendar zeroCalendar = Calendar.getInstance();
//            //            zeroCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//            //            zeroCalendar.set(zeroCalendar.get(Calendar.YEAR), zeroCalendar.get(Calendar.MONTH), zeroCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//            //            timeSecondsStampOfClosing = zeroCalendar.getTime().getTime() + recentBatch.getTriggerTime().getTime();
//            long current = System.currentTimeMillis() + 8*1000*3600; // 添加时区offset
//            long zero = current/(1000*3600*24)*(1000*3600*24);
//            timeSecondsStampOfClosing = zero + recentBatch.getTriggerTime().getTime();
//        }
//        return new Date(timeSecondsStampOfClosing);
//    }

}
