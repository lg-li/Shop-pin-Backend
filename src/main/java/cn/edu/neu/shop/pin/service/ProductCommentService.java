package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
