package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProductCommentService extends AbstractService<PinUserProductComment> {

    @Autowired
    private PinUserProductCommentMapper pinUserProductCommentMapper;

    public void addComment(Integer userId, Integer orderIndividualId, Integer productId, Integer grade,
                           Integer productScore, Integer serviceScore, String content, String imageUrl) {
        PinUserProductComment comment = new PinUserProductComment();
        comment.setUserId(userId);
        comment.setOrderIndividualId(orderIndividualId);
        comment.setProductId(productId);
        comment.setGrade(grade);

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
     * @return
     */
    public Integer[] getComments(Integer storeId) {
        Integer comment[] = new Integer[7];
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
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<PinUserProductComment> getCommentAndUserInfoByPage(Integer productId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            pinUserProductCommentMapper.getCommentAndUserInfo(productId);
        });
    }

    /**
     * 获取该店铺商家未评论总数
     * @param storeId
     * @return
     */
    public Integer getMerchantNotComment(Integer storeId) {
        return pinUserProductCommentMapper.getNumberOfMerchantNotComment(storeId);
    }

    /**
     * 指定偏移的天数，计算某天的日期
     * @param today 当前时间
     * @param delta 偏移量
     * @return
     */
    private java.util.Date getDateByOffset(java.util.Date today, Integer delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + delta);
        return calendar.getTime();
    }

    public List<JSONObject> getProductWithComment(Integer storeId) {
        return pinUserProductCommentMapper.getAllProductWithComment(storeId);
    }
}
