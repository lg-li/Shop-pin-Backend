package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface PinUserProductCommentMapper extends BaseMapper<PinUserProductComment> {

    //根据商品ID 获取该商品评论
    List<PinUserProductComment> getCommentByProductId(@Param("productId") int productId);
}