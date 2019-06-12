package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProductCommentService extends AbstractService<PinUserProductComment> {

    public static final int STATUS_ADD_COMMENT_SUCCESS = 0;
    public static final int STATUS_ADD_COMMENT_FAILED = -1;

    private final PinUserProductCommentMapper pinUserProductCommentMapper;

    private final OrderIndividualService orderIndividualService;

    public ProductCommentService(PinUserProductCommentMapper pinUserProductCommentMapper, OrderIndividualService orderIndividualService) {
        this.pinUserProductCommentMapper = pinUserProductCommentMapper;
        this.orderIndividualService = orderIndividualService;
    }

    /**
     * @author flyhero
     * 为商品添加评论
     * @param userId 用户ID
     * @param orderIndividualId 订单ID
     * @param productId 产品ID
     * @param skuId skuID
     * @param grade 0-好评，1-中评，2-差评
     * @param productScore 产品评分（1～5）
     * @param serviceScore 服务评分（1～5）
     * @param content 评论内容
     * @param imagesUrls 评论图片
     * @return 添加评论成功与否的状态
     */
    public Integer addComment(Integer userId, Integer orderIndividualId, Integer productId, Integer skuId, Integer grade,
                           Integer productScore, Integer serviceScore, String content, String imagesUrls) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
        PinUserProductComment commentSample = new PinUserProductComment();
        commentSample.setOrderIndividualId(orderIndividualId);
        commentSample.setSkuId(skuId);
        List<PinUserProductComment> list = pinUserProductCommentMapper.select(commentSample);
        if(list != null || orderIndividual.getStatus() != PinOrderIndividual.STATUS_PENDING_COMMENT) {
            // 表中已有对此订单的评论，或订单状态不是"待评价"
            return STATUS_ADD_COMMENT_FAILED;
        }
        // 评论表中新增一条记录
        PinUserProductComment comment = new PinUserProductComment();
        comment.setUserId(userId);
        comment.setOrderIndividualId(orderIndividualId);
        comment.setProductId(productId);
        comment.setSkuId(skuId);
        comment.setGrade(grade);
        comment.setProductScore(productScore);
        comment.setServiceScore(serviceScore);
        comment.setContent(content);
        comment.setImagesUrls(imagesUrls);
        pinUserProductCommentMapper.insert(comment);
        // 更新订单状态为已评价
        orderIndividual.setStatus(PinOrderIndividual.STATUS_PENDING_COMMENT);
        orderIndividualService.update(orderIndividual);
        return STATUS_ADD_COMMENT_SUCCESS;
    }

    /**
     * 根据商品ID 获取该商品评论信息
     * @param productId 商品 ID
     * @return List
     */
    public PageInfo<PinUserProductComment> getCommentByProductIdByPage(Integer productId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            PinUserProductComment pinUserProductComment = new PinUserProductComment();
            pinUserProductComment.setProductId(productId);
            pinUserProductCommentMapper.select(pinUserProductComment);
        });
    }

    /**
     * 从当前时间算起，获取之前7天内每天的评论数
     * @param storeId 店铺ID
     * @return 获取评论数成功与否的状态
     */
    public Integer[] getComments(Integer storeId) {
        Integer[] comment = new Integer[7];
        Date date = new Date();
        date = getDateByOffset(date, 1);
        for(int i = 0; i < 7; i++) {
            Date toDate = date;
            date = getDateByOffset(date, -1);
            Date fromDate = date;
//            System.out.println("fromDate: " + fromDate + " --- toDate: " + toDate);
            comment[i] = pinUserProductCommentMapper.getNumberOfComment(fromDate, toDate, storeId);
//            System.out.println("comment[i]: " + comment[i]);
        }
        return comment;
    }

    /**
     * 管理端
     * 分页获取商品评论信息 包括评论用户昵称和头像 购买商品类型
     */
    public List<JSONObject> getCommentAndUserInfoByPage(Integer productId) {
        return pinUserProductCommentMapper.getCommentAndUserInfo(productId);
    }

    /**
     * @param list 列表长度
     * @param pageNumber 分页数量
     * @param pageSize 页面长度
     * @return 评论列表
     */
    public List<?> getCommentsByPageNumAndSize(List<?> list, Integer pageNumber, Integer pageSize) {
        if (pageNumber * pageSize < list.size()) {
            return list.subList((pageNumber - 1) * pageSize, pageNumber * pageSize);
        } else {
            return list.subList((pageNumber - 1) * pageSize, list.size());
        }
    }

    /**
     * 获取该店铺商家未评论总数
     * @param storeId 店铺ID
     * @return 未评论的店家数量
     */
    public Integer getMerchantNotComment(Integer storeId) {
        return pinUserProductCommentMapper.getNumberOfMerchantNotComment(storeId);
    }

    /**
     * 指定偏移的天数，计算某天的日期
     * @param today 当前时间
     * @param delta 偏移量
     * @return 希望得到的日期
     */
    private java.util.Date getDateByOffset(java.util.Date today, Integer delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delta);
        return calendar.getTime();
    }

    /**
     * 获取某一店铺内尚未评论的产品列表
     * @param storeId 店铺ID
     * @return 商品列表
     */
    public List<JSONObject> getProductWithComment(Integer storeId) {
        return pinUserProductCommentMapper.getAllProductWithComment(storeId);
    }

    public void updateMerchantCommentContent(Integer commentId, String commentContent, Date commentTime){
        pinUserProductCommentMapper.updateMerchantComment(commentId, commentContent, commentTime);
    }
}
