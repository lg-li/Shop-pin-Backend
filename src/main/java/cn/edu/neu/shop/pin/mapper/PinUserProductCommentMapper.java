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

    JSONObject getCommentAndUserInfo(Integer productId);
}