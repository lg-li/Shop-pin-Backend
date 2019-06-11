package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.OrderItemsAreNotInTheSameStoreException;
import cn.edu.neu.shop.pin.exception.ProductSoldOutException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderIndividualService extends AbstractService<PinOrderIndividual> {

    @Autowired
    private UserRoleListTransferService userRoleListTransferService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderIndividualService orderIndividualService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PinOrderIndividualMapper pinOrderIndividualMapper;

    /**
     * 获取最近三个月的OrderIndividual信息
     *
     * @param userId
     * @return
     */
    public List<PinOrderIndividual> getRecentThreeMonthsOrderIndividuals(Integer userId) {
        List<PinOrderIndividual> orderIndividuals =
                pinOrderIndividualMapper.getRecentThreeMonthsOrderIndividuals(userId);
        for (PinOrderIndividual o : orderIndividuals) {
            o.setOrderItems(orderItemService.getOrderItemsByOrderIndividualId(o.getId()));
            o.setStore(storeService.getStoreById(o.getStoreId()));
        }
        return orderIndividuals;
    }

    /**
     * 传入一串PinOrderIndividual，返回它们对应的用户list
     *
     * @param list 一串PinOrderIndividual
     * @return 返回它们对应的用户list
     */
    public List<PinUser> getUsers(List<PinOrderIndividual> list) {
        List<PinUser> users = new ArrayList<>();
        for (PinOrderIndividual item : list) {
            users.add(userRoleListTransferService.findById(item.getUserId()));
        }
        return users;
    }

    /**
     * 提交订单，即把一条OrderItem记录变为Submitted
     *
     * @param user
     * @param list
     * @param addressId
     * @return
     * @throws OrderItemsAreNotInTheSameStoreException
     * @throws ProductSoldOutException
     */
    public PinOrderIndividual addOrderIndividual(PinUser user, List<PinOrderItem> list, Integer addressId, String userRemark) throws OrderItemsAreNotInTheSameStoreException, ProductSoldOutException {
        PinUserAddress address = addressService.findById(addressId);
        if (address == null) {
            throw new RecordNotFoundException("地址ID不正确");
        }
        boolean isSameStore = productService.isBelongSameStore(list);
        //如果属于一家店铺
        if (isSameStore) {
            Integer amount = orderItemService.getProductAmount(list);
            if (amount == -1) {
                //库存不够，只能终止这次创建orderIndividual
                throw new ProductSoldOutException("库存不足");
            }
            int storeId = productService.getProductById(list.get(0).getProductId()).getStoreId();    // 店铺id
            BigDecimal originallyPrice = orderItemService.getProductTotalPrice(list);   // 计算本来的价格
            BigDecimal shippingFee = orderItemService.getAllShippingFee(list);  // 邮费
            BigDecimal totalPrice = originallyPrice.add(shippingFee);   //总费用
            // OrderItemService.PayDetail payDetail = orderItemService.new PayDetail(user.getId(), totalPrice);    //支付详情
            BigDecimal totalCost = orderItemService.getTotalCost(list);
            String addressString = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail();
            PinOrderIndividual orderIndividual = new PinOrderIndividual(null, storeId, user.getId(),
                    address.getRealName(), address.getPhone(), addressString,
                    orderItemService.getProductAmount(list), totalPrice/*总价格 邮费加本来的费用*/,
                    shippingFee, null, /*卖家可以改动实际支付的邮费，修改的时候总价格也要修改，余额支付，实际支付也要改*/
                    null, null, false, null,
                    new Date(System.currentTimeMillis()), 0, 0, null, null, null,
                    null, null, null, null, null, null, null, userRemark, null, totalCost);
            this.save(orderIndividual);
            //将list中的PinOrderItem挂载到PinOrderIndividual上
            orderItemService.mountOrderItems(list, orderIndividual.getId());
            return orderIndividual;
        } else {
            //如果不属于一家店铺
            throw new OrderItemsAreNotInTheSameStoreException("不属于一家店铺");
        }
    }

    /**
     * @param orderGroupId
     * @return
     * @author flyhero
     * 根据OrderGroupId获取在此团单内的OrderIndividual的List
     * @author flyhero
     * 根据OrderGroupId获取在此团单内的OrderIndividual的List
     * @author flyhero
     * 根据OrderGroupId获取在此团单内的OrderIndividual的List
     */
    public List<PinOrderIndividual> getOrderIndividualsByOrderGroupId(Integer orderGroupId) {
        PinOrderIndividual orderIndividual = new PinOrderIndividual();
        orderIndividual.setOrderGroupId(orderGroupId);
        List<PinOrderIndividual> orderIndividuals = pinOrderIndividualMapper.select(orderIndividual);
        return orderIndividuals;
    }

    /**
     * 获取订单数
     *
     * @param storeId 商店ID
     * @return
     */
    public Integer[] getOrders(Integer storeId) {
        Integer[] order = new Integer[7];
        java.util.Date date = new java.util.Date();
        date = getDateByOffset(date, 1); // tomorrow
        for (int i = 0; i < 7; i++) {
            java.util.Date toDate = date;
            date = getDateByOffset(date, -1); // yesterday
            java.util.Date fromDate = date;
            order[i] = pinOrderIndividualMapper.getNumberOfOrder(fromDate, toDate, storeId);
        }
        return order;
    }

    private java.util.Date getDateByOffset(java.util.Date today, Integer delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delta);
        return calendar.getTime();
    }

    /**
     * @param keyWord 关键词
     * @return 返回符合关键词的 PinOrderIndividual 数组，
     * 每个 PinOrderIndividual 里面有多个 PinOrderItem
     * 每个 PinOrderItem 里面有一个 PinProduct
     */
    public List<PinOrderIndividual> getAllWithProductsByKeyWord(String keyWord, Integer storeId) {
        return pinOrderIndividualMapper.getAllWithProductsByKeyWord(keyWord == null ? "" : keyWord, storeId);
    }

    /**
     * TODO：待测试
     *
     * @param list      待过滤的PinOrderIndividual的数组
     * @param orderType 传过来的orderType
     * @return 返回过滤过后的list
     */
    public List<PinOrderIndividual> getOrdersByOrderType(List<PinOrderIndividual> list, Integer orderType) {
        List<PinOrderIndividual> returnList = new ArrayList<>();
        switch (orderType) {
            case 0://全部
                return list;
            case 1://未付款
                for (PinOrderIndividual item : list) {
                    if (!item.getPaid())
                        returnList.add(item);
                }
                break;
            case 2://待发货
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.STATUS_DEPENDING_TO_SHIP)
                        returnList.add(item);
                }
                break;
            case 3://待收货
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.STATUS_SHIPPED)
                        returnList.add(item);
                }
                break;
            case 4://待评价
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.STATUS_PENDING_COMMENT)
                        returnList.add(item);
                }
                break;
            case 5://交易完成
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.STATUS_COMMENTED)
                        returnList.add(item);
                }
                break;
            case 6://退款中
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.REFUND_STATUS_APPLYING)
                        returnList.add(item);
                }
                break;
            case 7://已退款
                for (PinOrderIndividual item : list) {
                    if (item.getStatus() == PinOrderIndividual.REFUND_STATUS_FINISHED)
                        returnList.add(item);
                }
                break;
        }
        return returnList;
    }

    /**
     * TODO: 待测试
     *
     * @param list      待过滤的PinOrderIndividual的数组
     * @param orderDate 时间条件对应的码
     * @param queryType 前端传入的json
     * @return 返回符合条件的数组
     */
    public List<PinOrderIndividual> getOrdersByOrderDate(List<PinOrderIndividual> list, Integer orderDate, JSONObject queryType) {
        List<PinOrderIndividual> returnList = new ArrayList<>();
        java.util.Date createTime;
        java.util.Date now = new java.util.Date();
        Calendar caNow = Calendar.getInstance();
        caNow.setTime(now);
        Calendar caCreate = Calendar.getInstance();
        for (PinOrderIndividual item : list) {
            createTime = item.getCreateTime();
            caCreate.setTime(createTime);

            switch (orderDate) {
                case 0://全部
                    returnList.add(item);
                case 1://昨天
                    caCreate.add(Calendar.DAY_OF_YEAR, 1);
                    if ((caCreate.get(Calendar.DAY_OF_YEAR) == caNow.get(Calendar.DAY_OF_YEAR)) &&
                            (caCreate.get(Calendar.YEAR) == caNow.get(Calendar.YEAR)))
                        returnList.add(item);
                    break;
                case 2://今天
                    if (caCreate.get(Calendar.DAY_OF_YEAR) == caNow.get(Calendar.DAY_OF_YEAR))
                        returnList.add(item);
                    break;
                case 3://本周
                    if ((caCreate.get(Calendar.YEAR) == caNow.get(Calendar.YEAR)) &&
                            (caCreate.get(Calendar.WEEK_OF_YEAR) == caNow.get(Calendar.WEEK_OF_YEAR)))
                        returnList.add(item);
                    break;
                case 4://本月
                    if ((caCreate.get(Calendar.YEAR) == caNow.get(Calendar.YEAR)) &&
                            (caCreate.get(Calendar.MONTH) == caNow.get(Calendar.MONTH)))
                        returnList.add(item);
                    break;
                case 5://本季度
                    int createMonth = caCreate.get(Calendar.MONTH) + 1; //创建时的月份
                    int rightMonth = caNow.get(Calendar.MONTH) + 1; //现在的月份
                    if (caCreate.get(Calendar.YEAR) == caNow.get(Calendar.YEAR)) {
                        if (createMonth >= 1 && rightMonth >= 1 && createMonth <= 3 && rightMonth <= 3)
                            returnList.add(item);
                        else if (createMonth >= 4 && rightMonth >= 4 && createMonth <= 6 && rightMonth <= 6)
                            returnList.add(item);
                        else if (createMonth >= 7 && rightMonth >= 7 && createMonth <= 9 && rightMonth <= 9)
                            returnList.add(item);
                        else if (createMonth >= 10 && rightMonth >= 10 && createMonth <= 12 && rightMonth <= 12)
                            returnList.add(item);
                    }
                    break;
                case 6://本年
                    if (caCreate.get(Calendar.YEAR) == caNow.get(Calendar.YEAR))
                        returnList.add(item);
                    break;
                case 7://自定义
                    java.util.Date begin = queryType.getDate("begin");
                    java.util.Date end = queryType.getDate("end");
                    if (begin != null && end != null) {
                        if (caCreate.getTime().getTime() >= begin.getTime() && caCreate.getTime().getTime() <= end.getTime())
                            returnList.add(item);
                    } else if (begin == null && end != null) {
                        if (caCreate.getTime().getTime() <= end.getTime())
                            returnList.add(item);
                    } else if (end == null && begin != null) {
                        if (caCreate.getTime().getTime() >= begin.getTime())
                            returnList.add(item);
                    }else {
                        returnList.add(item);
                    }
                    break;
            }
        }
        return returnList;
    }

    /**
     * 未发货商品数量
     *
     * @param storeId
     * @return
     */
    public Integer getProductNotShip(Integer storeId) {
        return pinOrderIndividualMapper.getNumberOfOrderNotShip(storeId);
    }

    public Integer getOrderRefund(Integer storeId) {
        return pinOrderIndividualMapper.getNumberOfOrderRefund(storeId);
    }

    /**
     * @param list       传入 一个对象 的list
     * @param pageNumber 传入的页码数
     * @param pageSize   传入一页的size
     * @return 返回要查找的那页
     */
    public List<?> getOrdersByPageNumAndSize(List<?> list, Integer pageNumber, Integer pageSize) {
        if (pageNumber * pageSize < list.size()) {
            return list.subList((pageNumber - 1) * pageSize, pageNumber * pageSize);
        } else {
            return list.subList((pageNumber - 1) * pageSize, list.size());
        }
    }

//    public void kickOutAnOrder(Integer orderIndividualId) {
//        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
//        orderIndividual.setOrderGroupId(null);
//        orderIndividual.setStatus(0);
//    }
}
