package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface PinUserProductCommentMapper extends BaseMapper<PinUserProductComment> {

    Integer getNumberOfComment(Date fromTime, Date toTime, Integer storeId);

    List<JSONObject> getCommentAndUserInfo(Integer productId);

    Integer getNumberOfMerchantNotComment(Integer storeId);

    List<JSONObject> getAllProductWithComment(Integer storeId);

    void updateMerchantComment(Integer commentId, String commentContent, Date commentTime);

    Double getAvgScore(Integer id);
}