package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCommentService extends AbstractService<PinUserProductComment> {

    @Autowired
    private PinUserProductCommentMapper pinUserProductCommentMapper;

    /**
     * 根据商品ID 获取该商品评论信息
     * @param productId 商品 ID
     * @return List
     */
    public List<PinUserProductComment> getCommentByProductId(Integer productId){
        PinUserProductComment pinUserProductComment = new PinUserProductComment();
        pinUserProductComment.setProductId(productId);
        return pinUserProductCommentMapper.select(pinUserProductComment);
    }
}
