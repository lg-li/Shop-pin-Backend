package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ProductCommentService extends AbstractService<PinUserProductComment> {

    @Autowired
    private PinUserProductCommentMapper pinUserProductCommentMapper;

    /**
     * 根据商品ID 获取该商品评论信息
     *
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
     * 获取评论数
     * @param storeId
     * @return
     */
    public Integer[] getComments(Integer storeId) {
        Integer comment[] = new Integer[7];
        Date date;
        Calendar calendar = Calendar.getInstance();
        for(int i = 0; i < 7; i++){
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            date = calendar.getTime();
            comment[i] = pinUserProductCommentMapper.getNumberOfComment(date, storeId);
        }
        return comment;
    }

    public PageInfo<PinUserProductComment> getCommentAndUserInfoByPage(Integer productId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            pinUserProductCommentMapper.getCommentAndUserInfo(productId);
        });
    }

}
