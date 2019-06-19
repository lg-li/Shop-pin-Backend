package cn.edu.neu.shop.pin.util.base;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@Component
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T>, InsertListMapper<T>, ConditionMapper<T> {
}
