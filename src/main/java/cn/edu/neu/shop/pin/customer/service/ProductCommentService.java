package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCommentService {

    @Autowired
    private PinUserProductCommentMapper pinUserProductCommentMapper;

    public List<PinUserProductComment> getCommentByProductId(Integer productId){
        return pinUserProductCommentMapper.getCommentByProductId(productId);
    }
}
