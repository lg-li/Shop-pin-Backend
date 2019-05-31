package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PinUserProductCommentMapper extends BaseMapper<PinUserProductComment> {
}