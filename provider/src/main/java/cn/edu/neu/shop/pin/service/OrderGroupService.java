package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.lock.annotation.LockKeyVariable;
import cn.edu.neu.shop.pin.lock.annotation.MutexLock;
import cn.edu.neu.shop.pin.mapper.PinOrderGroupMapper;
import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinStoreGroupCloseBatch;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import cn.edu.neu.shop.pin.websocket.CustomerPrincipal;
import cn.edu.neu.shop.pin.websocket.StompMessageService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author flyhero
 * 获取OrderGroup（当前团单信息）表中的内容
 */
@Service
public class OrderGroupService extends AbstractService<PinOrderGroup> {

    private static Logger logger = LoggerFactory.getLogger(OrderGroupService.class);

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_INVALID_ID = -1;
    public static final int STATUS_PERMISSION_DENIED = -2;
    public static final int STATUS_NOT_ALLOWED = -3;

    //    public static final int STATUS_JOIN_ORDER_GROUP_SUCCESS = -4;
//    public static final int STATUS_JOIN_ORDER_GROUP_INVALID_ID = -5;
//    public static final int STATUS_JOIN_ORDER_GROUP_PERMISSION_DENIED = -6;
//    public static final int STATUS_JOIN_ORDER_GROUP_NOT_ALLOWED = -7;
    public static final int STATUS_JOIN_ORDER_GROUP_IS_ENDED = -8;
    public static final int STATUS_JOIN_ORDER_GROUP_IS_FULL = -9;

    //    public static final int STATUS_QUIT_ORDER_GROUP_SUCCESS = -10;
    public static final int STATUS_QUIT_ORDER_GROUP_FAILED = -11;

    private static final int GROUP_CLOSE_DELAY_MILLISECOND = 7200000;

    private final PinOrderIndividualMapper individualMapper;

    private final PinOrderGroupMapper pinOrderGroupMapper;

    private final StoreService storeService;

    private final OrderItemService orderItemService;

    private final OrderIndividualService orderIndividualService;

    private final StompMessageService stompMessageService;

    private final UserService userService;

    private final StoreCloseBatchService storeCloseBatchService;

    public OrderGroupService(PinOrderIndividualMapper individualMapper, PinOrderGroupMapper pinOrderGroupMapper, StoreService storeService, OrderItemService orderItemService, OrderIndividualService orderIndividualService, StompMessageService stompMessageService, UserService userService, StoreCloseBatchService storeCloseBatchService) {
        this.individualMapper = individualMapper;
        this.pinOrderGroupMapper = pinOrderGroupMapper;
        this.storeService = storeService;
        this.orderItemService = orderItemService;
        this.orderIndividualService = orderIndividualService;
        this.stompMessageService = stompMessageService;
        this.userService = userService;
        this.storeCloseBatchService = storeCloseBatchService;
    }

    /**
     * @return OrderGroup的list
     * @author flyhero
     * 获取某一店铺当前活跃的前十条拼团数据
     */
    public List<PinOrderGroup> getTopTenOrderGroups(Integer storeId) {
        return pinOrderGroupMapper.getTopTenOrderGroups(storeId);
    }

    /**
     * @param orderGroupId 团单ID
     * @return 拼单的人的list
     * @author flyhero
     * 传入orderGroup 返回拼单的人
     */
    public List<PinUser> getUsersByOrderGroup(Integer orderGroupId) {
        List<PinOrderIndividual> individuals = individualMapper.selectByOrderGroupId(orderGroupId);
        return orderIndividualService.getUsers(individuals);
    }

    /**
     * @param orderGroupId 团单ID
     * @return 团单中的人数
     * @author flyhero
     * 返回在某一团单中的人数
     */
    private Integer getUserNumberInAOrderGroup(Integer orderGroupId) {
        return getUsersByOrderGroup(orderGroupId).size();
    }

    /**
     * @param orderIndividualId 订单ID
     * @author flyhero
     * 新建一个团单
     */
    @Transactional
    public Integer createOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        if (!Objects.equals(userId, orderIndividual.getUserId())) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if (!Objects.equals(storeId, orderIndividual.getStoreId())) {
            // 店铺ID不符，返回ID错误
            return STATUS_INVALID_ID;
        }
        if (!(orderIndividual.getPaid() && orderIndividual.getStatus() == 0)) {
            // 给定的订单不满足创团条件：1.isPaid不是未付款 2.status不是0-待发货
            return STATUS_NOT_ALLOWED;
        }
        if (orderIndividual.getOrderGroupId() != null) {
            // orderGroupId不为空，已在某一团单中，返回团单ID
            return orderIndividual.getOrderGroupId();
        }
        // 新建OrderGroup记录
        PinOrderGroup orderGroup = new PinOrderGroup();
        orderGroup.setOwnerUserId(userId);
        orderGroup.setStoreId(storeId);
        orderGroup.setStatus(0);
        orderGroup.setCreateTime(new Date());
        orderGroup.setCloseTime(getOrderGroupCloseTimeFromNow(storeId));
        this.save(orderGroup);
        // 将orderGroup挂载到orderIndividual上
        orderIndividual.setOrderGroupId(orderGroup.getId());
        orderIndividual.setIsGroup(true);
        orderIndividualService.update(orderIndividual);
        return STATUS_SUCCESS;
    }

    /**
     * @param userId            用户ID
     * @param storeId           店铺ID
     * @param orderIndividualId 用户传进来的订单ID
     * @param orderGroupId      要加入的团单 orderGroupID
     * @author flyhero
     * 按照orderGroupId加入团单
     */
    @Transactional
    @MutexLock(key = PinConstants.LOCK_KEY_ORDER_GROUP)
    public Integer joinOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId, @LockKeyVariable Integer orderGroupId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        PinOrderGroup orderGroup = this.findById(orderGroupId);
        if (!Objects.equals(userId, orderIndividual.getUserId())) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if (!Objects.equals(storeId, orderIndividual.getStoreId())) {
            // 店铺ID不符，返回ID错误
            return STATUS_INVALID_ID;
        }
        if (!orderIndividual.getPaid() || orderIndividual.getStatus() != 0) {
            // 给定的订单不满足加团条件：1.isPaid不是未付款 2.status不是0-待发货
            return STATUS_NOT_ALLOWED;
        }
        PinOrderGroup sample = new PinOrderGroup();
        sample.setOwnerUserId(userId);
        sample.setStoreId(storeId);
        List<PinOrderGroup> orderGroups = pinOrderGroupMapper.select(sample);
        for (PinOrderGroup orderGroup1 : orderGroups) {
            // 找到了此店铺此人创建的正在可用的团，则无法再用其他订单新建一个团
            if (orderGroup1.getStatus() == 0) {
                return STATUS_NOT_ALLOWED;
            }
        }
        if (orderIndividual.getOrderGroupId() != null) {
            // orderGroupId不为空，已在某一团单中，返回团单ID
            return orderIndividual.getOrderGroupId();
        }
        if (orderGroup.getStatus() != 0 || orderGroup.getCloseTime().before(new Date())) {
            // 指定的团单已结束
            return STATUS_JOIN_ORDER_GROUP_IS_ENDED;
        }
        Integer userNum = this.getUserNumberInAOrderGroup(orderGroupId);
        Integer limit = storeService.getStoreById(storeId).getPeopleLimit();
        if (userNum >= limit) {
            // 指定的团单人数已满
            return STATUS_JOIN_ORDER_GROUP_IS_FULL;
        }
        // 上述问题都没有出现，则正常加入团单，并更新数据库
        orderIndividual.setOrderGroupId(orderGroupId);
        orderIndividual.setIsGroup(true);
        orderIndividualService.update(orderIndividual);
        // 向房间内的人发送消息
        JSONObject orderGroupJSON = generateOrderGroupJSON(orderGroup);
        orderGroupJSON.put("message", "有人适才加入了房间");
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(userId, orderIndividualId, orderGroupId);
        stompMessageService.sendGroupUpdateMessage(customerPrincipal, orderGroupJSON);
        return STATUS_SUCCESS;
    }

    /**
     * @param userId            用户ID
     * @param orderIndividualId 订单ID
     * @param orderGroupId      团单ID
     * @return 退出状态
     * @author flyhero
     * 退出团单
     */
    public Integer quitOrderGroup(Integer userId, Integer orderIndividualId, Integer orderGroupId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        PinOrderGroup orderGroup = this.findById(orderGroupId);
        if (!Objects.equals(userId, orderIndividual.getUserId())) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if (orderGroup.getOwnerUserId().equals(userId)) {
            // 用户是团单创建者，不允许退出
            return STATUS_QUIT_ORDER_GROUP_FAILED;
        }
        // 上述问题都没有出现，则正常退出团单，并更新数据库
        orderIndividual.setOrderGroupId(null);
        orderIndividual.setIsGroup(false);
        orderIndividualService.update(orderIndividual);
        // 向房间内的人发送消息
        JSONObject orderGroupJSON = generateOrderGroupJSON(orderGroup);
        orderGroupJSON.put("message", "有人适才退出了房间");
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(userId, orderIndividualId, orderGroupId);
        stompMessageService.sendGroupUpdateMessage(customerPrincipal, orderGroupJSON);
        return STATUS_SUCCESS;
    }

    /**
     * @param customerPrincipal 客户principal
     * @author flyhero
     * Stomp 初始化页面消息
     */
    public void sendGroupInitMessageToSingle(CustomerPrincipal customerPrincipal) {
        PinOrderGroup orderGroup = this.findById(customerPrincipal.getOrderGroupId());
        PinOrderIndividual orderIndividualOfCurrentUser = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
        if (orderGroup == null || orderIndividualOfCurrentUser == null) {
            stompMessageService.sendSingleErrorMessage(customerPrincipal,
                    ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, PinConstants.ResponseMessage.INVALID_DATA, null));
            return;
        }
        JSONObject orderGroupJSON = generateOrderGroupJSON(orderGroup);
        orderGroupJSON.put("message", "hello");
        logger.info("Sending: " + orderGroupJSON.toJSONString());
        stompMessageService.sendSingleUpdateMessage(customerPrincipal, orderGroupJSON);
    }

    /**
     * @param orderGroup 团单对象
     * @return 响应JSON
     * @author flyhero
     * 生成返回的JSON格式的orderGroup，由于多个地方会用到，因此将其抽离出来
     */
    private JSONObject generateOrderGroupJSON(PinOrderGroup orderGroup) {
        JSONObject orderGroupJSON = (JSONObject) JSONObject.toJSON(orderGroup);
        // 重新置入团单结束时间 => 转为时间戳
        orderGroupJSON.put("closeTime", orderGroup.getCloseTime());
        // 获取当前用户个人单包含的商品
        List<PinOrderIndividual> orderIndividualsInCurrentGroup = orderIndividualService.getOrderIndividualsByOrderGroupId(orderGroup.getId());
        orderIndividualsInCurrentGroup.forEach(orderIndividual -> {
            orderIndividual.setUser(userService.findById(orderIndividual.getUserId()));
            orderIndividual.setOrderItems(orderItemService.getOrderItemsByOrderIndividualId(orderIndividual.getId()));
        });

        orderGroupJSON.put("orderIndividuals", orderIndividualsInCurrentGroup);
        return orderGroupJSON;
    }

    /**
     * @param storeId 店铺ID
     * @return 返回距离现在最近的收团时间
     * @author flyhero
     * 设置拼团的截止时间
     */
    public Date getOrderGroupCloseTimeFromNow(Integer storeId) {
        // 获设置拼团时间
        PinStoreGroupCloseBatch recentBatch = storeCloseBatchService.getRecentGroupCloseBatchTime(storeId);
        long timeSecondsStampOfClosing;
        if (recentBatch == null) {
            // 向下取整到整点分钟，测试通过
            // 未指定配送批次则2小时后收团
            timeSecondsStampOfClosing = ((System.currentTimeMillis() + GROUP_CLOSE_DELAY_MILLISECOND) / 60000);
            timeSecondsStampOfClosing *= 60000; // 恢复大小到毫秒
        } else {
            long current = System.currentTimeMillis();
            long zero = (current / (1000 * 3600 * 24)) * (1000 * 3600 * 24);
//            System.out.println("zero: " + new Date(zero));
//            System.out.println("batchTime: " + new Date(zero + recentBatch.getTime().getTime()));
//            System.out.println("compareToTenMinutesLater: " + new Date(new Date().getTime() + 600000));
            if (zero + recentBatch.getTime().getTime() < new Date().getTime() + 600000) {
                // 最近发货时间比当前时间早，返回的是下一天的第一批
                current = System.currentTimeMillis();
                zero = (current / (1000 * 3600 * 24) + 1) * (1000 * 3600 * 24);
                timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
//                System.out.println("date: " + new Date(timeSecondsStampOfClosing));
            } else {
                // 返回接下来最近的一个收团时间
                current = System.currentTimeMillis();
                zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24);
                timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
//                System.out.println("选择最近时间: " + new Date(timeSecondsStampOfClosing));
            }
        }
//        else if (recentBatch.getTime().before(new Date(new Date().getTime() + 600000))) {
//            System.out.println("batch time: " + recentBatch.getTime());
//
//            // 返回的是下一天的第一批
//            long current = System.currentTimeMillis();
//            long zero = (current / (1000 * 3600 * 24) + 1) * (1000 * 3600 * 24);
//            timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
//            System.out.println("date: " + new Date(timeSecondsStampOfClosing));
//        } else {
//            // 已指定配送批次，选择最近时间收团，测试通过
//            long current = System.currentTimeMillis();
//            long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24);
//            timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
//            System.out.println("选择最近时间: " + new Date(timeSecondsStampOfClosing));
//        }
        return new Date(timeSecondsStampOfClosing);
    }

    public List<PinOrderGroup> getAllWithOrderIndividual(Integer storeId) {
        return pinOrderGroupMapper.getAllWithOrderIndividual(storeId);
    }

    public List<PinOrderGroup> getOrdersByOrderStatus(List<PinOrderGroup> list, Integer groupStatus) {
        List<PinOrderGroup> returnList = new ArrayList<>();
        switch (groupStatus) {
            case 0://全部
                return list;
            case 1://正在拼团
                for (PinOrderGroup item : list) {
                    if (item.getStatus() == PinOrderGroup.STATUS_PINGING)
                        returnList.add(item);
                }
                break;
            case 2://已结束拼团
                for (PinOrderGroup item : list) {
                    if (item.getStatus() == PinOrderGroup.STATUS_FINISHED)
                        returnList.add(item);
                }
                break;
        }
        return returnList;
    }

    public List<PinOrderGroup> getOrdersByDate(List<PinOrderGroup> list, Date begin, Date end) {
        List<PinOrderGroup> returnList = new ArrayList<>();
        if (begin != null && end != null) {
            for (PinOrderGroup item : list) {
                if (item.getCreateTime().getTime() <= end.getTime() && item.getCreateTime().getTime() >= begin.getTime())
                    returnList.add(item);
            }
        } else if (begin == null && end != null) {
            for (PinOrderGroup item : list) {
                if (item.getCreateTime().getTime() <= end.getTime())
                    returnList.add(item);
            }
        } else if (begin != null) {
            for (PinOrderGroup item : list) {
                if (item.getCreateTime().getTime() >= begin.getTime())
                    returnList.add(item);
            }
        } else {
            return list;
        }
        return returnList;
    }

    public List<PinOrderGroup> getOrdersByStatus(Integer status) {
        return pinOrderGroupMapper.getOrderGroupsByStatus(status);
    }
}
