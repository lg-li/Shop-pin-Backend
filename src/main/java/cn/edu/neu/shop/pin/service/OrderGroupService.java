package cn.edu.neu.shop.pin.service;

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
import cn.edu.neu.shop.pin.websocket.WebSocketService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author flyhero
 * @Description 获取OrderGroup（当前团单信息）表中的内容
 */
@Service
public class OrderGroupService extends AbstractService<PinOrderGroup> {

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

    public static final int GROUP_CLOSE_DELAY_MILLISECOND = 7200000;

    @Autowired
    private PinOrderIndividualMapper individualMapper;

    @Autowired
    private PinOrderGroupMapper pinOrderGroupMapper;

    @Autowired
    private StoreService storeService;

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
    private StoreCloseBatchService storeCloseBatchService;

    /**
     * @author flyhero
     * 获取某一店铺当前活跃的前十条拼团数据
     * @return
     */
    public List<PinOrderGroup> getTopTenOrderGroups(Integer storeId) {
        return pinOrderGroupMapper.getTopTenOrderGroups(storeId);
    }

    /**
     * @author flyhero
     * 传入orderGroup 返回拼单的人
     * @param orderGroupId
     * @return 拼单的人的list
     */
    public List<PinUser> getUsersByOrderGroup(Integer orderGroupId) {
        List<PinOrderIndividual> individuals = individualMapper.selectByOrderGroupId(orderGroupId);
        return orderIndividualService.getUsers(individuals);
    }

    /**
     * @author flyhero
     * 返回在某一团单中的人数
     * @param orderGroupId
     * @return
     */
    public Integer getUserNumberInAOrderGroup(Integer orderGroupId) {
        return getUsersByOrderGroup(orderGroupId).size();
    }

    /**
     * @author flyhero
     * 新建一个团单
     * @param orderIndividualId
     */
    public Integer createOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        if(!Objects.equals(userId, orderIndividual.getUserId())) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if(!Objects.equals(storeId, orderIndividual.getStoreId())) {
            // 店铺ID不符，返回ID错误
            return STATUS_INVALID_ID;
        }
        if(!(orderIndividual.getPaid() && orderIndividual.getStatus() == 0)) {
            // 给定的订单不满足创团条件：1.isPaid不是未付款 2.status不是0-待发货
            return STATUS_NOT_ALLOWED;
        }
        if(orderIndividual.getOrderGroupId() != null) {
            // orderGroupId不为空，已在某一团单中，返回团单ID
            return orderIndividual.getOrderGroupId();
        }
        PinOrderGroup orderGroup = new PinOrderGroup();
        orderGroup.setOwnerUserId(userId);
        orderGroup.setStoreId(storeId);
        orderGroup.setStatus(0);
        orderGroup.setCreateTime(new Date());
        orderGroup.setCloseTime(getOrderGroupCloseTimeFromNow(storeId));
        orderGroupService.save(orderGroup);
        return STATUS_SUCCESS;
    }

    /**
     * @author flyhero
     * 按照orderGroupId加入团单
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @param orderIndividualId 用户传进来的订单ID
     * @param orderGroupId 要加入的团单 orderGroupID
     */
    public Integer joinOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId, Integer orderGroupId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        PinOrderGroup orderGroup = orderGroupService.findById(orderGroupId);
        if(!Objects.equals(userId, orderIndividual.getUserId())) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if(!Objects.equals(storeId, orderIndividual.getStoreId())) {
            // 店铺ID不符，返回ID错误
            return STATUS_INVALID_ID;
        }
        if(!orderIndividual.getPaid() || orderIndividual.getStatus() != 0) {
            // 给定的订单不满足加团条件：1.isPaid不是未付款 2.status不是0-待发货
            return STATUS_NOT_ALLOWED;
        }
        if(orderIndividual.getOrderGroupId() != null) {
            // orderGroupId不为空，已在某一团单中，返回团单ID
            return orderIndividual.getOrderGroupId();
        }
        if(orderGroup.getStatus() != 0 || orderGroup.getCloseTime().before(new Date())) {
            // 指定的团单已结束
            return STATUS_JOIN_ORDER_GROUP_IS_ENDED;
        }
        Integer userNum = orderGroupService.getUserNumberInAOrderGroup(orderGroupId);
        Integer limit = storeService.getStoreById(storeId).getPeopleLimit();
        if(userNum >= limit) {
            // 指定的团单人数已满
            return STATUS_JOIN_ORDER_GROUP_IS_FULL;
        }
        // 上述问题都没有出现，则正常加入团单
        orderIndividual.setOrderGroupId(orderGroupId);
        orderIndividual.setIsGroup(true);
        // 更新数据库
        orderIndividualService.update(orderIndividual);
        // 向房间内的人发送消息
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(userId, orderIndividualId, orderGroupId);
        webSocketService.sendGroupNotifyMessage(customerPrincipal, "有人适才加入了房间");
        return STATUS_SUCCESS;
    }

    public Integer quitOrderGroup(Integer userId, Integer storeId, Integer orderIndividualId, Integer orderGroupId) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        PinOrderGroup orderGroup = orderGroupService.findById(orderGroupId);
        if(userId != orderIndividual.getUserId()) {
            // 用户ID不符，返回权限不够
            return STATUS_PERMISSION_DENIED;
        }
        if(orderGroup.getOwnerUserId().equals(userId)) {
            // 用户是团单创建者，不允许退出
            return STATUS_QUIT_ORDER_GROUP_FAILED;
        }
        orderIndividual.setOrderGroupId(null);
        orderIndividual.setIsGroup(false);
        orderIndividualService.update(orderIndividual);
        CustomerPrincipal customerPrincipal = new CustomerPrincipal(userId, orderIndividualId, orderGroupId);
        webSocketService.sendGroupNotifyMessage(customerPrincipal, "有人适才退出了房间");
        return STATUS_SUCCESS;
    }

    /**
     * Stomp 初始化页面消息
     * @param customerPrincipal 客户principal
     */
    public void sendGroupInitMessageToSingle(CustomerPrincipal customerPrincipal) {
        PinOrderGroup orderGroup = orderGroupService.findById(customerPrincipal.getOrderGroupId());
        PinOrderIndividual orderIndividualOfCurrentUser = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
        if (orderGroup == null || orderIndividualOfCurrentUser == null) {
            webSocketService.sendSingleErrorMessage(customerPrincipal,
                    ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, PinConstants.ResponseMessage.INVALID_DATA, null));
            return;
        }
        JSONObject orderGroupJSON = (JSONObject) JSONObject.toJSON(orderGroup);
        // 重新置入团单结束时间 => 转为时间戳
        orderGroupJSON.put("closeTime", orderGroup.getCloseTime());
        // 获取当前用户个人单包含的商品
        List<PinOrderIndividual> orderIndividualsInCurrentGroup = orderIndividualService.getOrderIndividualsByOrderGroupId(orderGroup.getId());
        orderIndividualsInCurrentGroup.forEach(orderIndividual-> {
            orderIndividual.setUser(userService.findById(orderIndividual.getUserId()));
            orderIndividual.setOrderItems(orderItemService.getOrderItemsByOrderIndividualId(orderIndividual.getId()));
        });

        orderGroupJSON.put("orderIndividuals", orderIndividualsInCurrentGroup);

        webSocketService.sendSingleHelloMessage(customerPrincipal, orderGroupJSON);
    }

    /**
     * @author flyhero
     * 设置拼团的截止时间
     * @param storeId
     * @return
     */
    private Date getOrderGroupCloseTimeFromNow(Integer storeId) {
        // 获设置拼团时间
        PinStoreGroupCloseBatch recentBatch = storeCloseBatchService.getRecentGroupCloseBatchTime(storeId);
        long timeSecondsStampOfClosing;
        if(recentBatch == null) {
            // 向下取整到整点分钟
            // 未指定配送批次则2小时后收团
            timeSecondsStampOfClosing = ((System.currentTimeMillis() + GROUP_CLOSE_DELAY_MILLISECOND) / 60000);
            timeSecondsStampOfClosing *= 60000; // 恢复大小到毫秒
        } else if(recentBatch.getTime().before(new Date(new Date().getTime() + 600000))) {
            // 返回的是下一天的第一批
            long current = System.currentTimeMillis() + 8*1000*3600; // 添加时区offset
            long zero = (current/(1000*3600*24) + 1) * (1000*3600*24);
            timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
        } else {
            // 已指定配送批次，选择最近时间收团
            long current = System.currentTimeMillis() + 8*1000*3600; // 添加时区offset
            long zero = current/(1000*3600*24)*(1000*3600*24);
            timeSecondsStampOfClosing = zero + recentBatch.getTime().getTime();
        }
        return new Date(timeSecondsStampOfClosing);
    }

    public List<PinOrderGroup> getAllWithOrderIndividual(Integer storeId) {
        return pinOrderGroupMapper.getAllWithOrderIndividual(storeId);
    }

    public List<PinOrderGroup> getOrdersByOrderStatus(List<PinOrderGroup> list, Integer groupStatus) {
        List<PinOrderGroup> returnList = new ArrayList<>();
        switch (groupStatus) {
            case 0://全部
                return returnList;
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
        for(PinOrderGroup item:list){
            if (item.getCreateTime().getTime()>=end.getTime()&&item.getCreateTime().getTime()<begin.getTime())
                returnList.add(item);
        }
        return returnList;
    }
}
