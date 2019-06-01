package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class OrderItemService {

    @Autowired
    private PinOrderItemMapper pinOrderItemMapper;


    /**TODO:未测试
     * 通过传进来的JSONArray 产生 PinOrderItem 的array
     * @param array 传入的JSONArray 里面是order_item的id
     * @return 返回由PinOrderItem组成的ArrayList
     */
    public ArrayList<PinOrderItem> getItemListByJSONArray(JSONArray array){
        ArrayList<PinOrderItem> list = new ArrayList<>();
        for (int i = 0;i<array.size();i++){
            //这个selectByPrimaryKey 不知道有没有用
            list.add(pinOrderItemMapper.selectByPrimaryKey(array.getInteger(i)));
        }
        return list;
    }


    /**
     * 通过JSONArray 传入PinOrderItem的数组
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public Integer getProductAmount(ArrayList<PinOrderItem> array){
        Integer amount = 0;
        for (PinOrderItem item:array){
            amount += item.getAmount();
        }
        return amount;
    }

    /**
     * 通过JSONArray 传入PinOrderItem的数组
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public BigDecimal getProductTotalPrice(ArrayList<PinOrderItem> array){
        //BigDecimal 尽量用字符串初始化
        BigDecimal price = new BigDecimal("0");
        for (PinOrderItem item:array){
            price = price.add(item.getTotalPrice());
        }
        return price;
    }

}
