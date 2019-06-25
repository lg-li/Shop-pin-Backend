package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
@FeignClient(value = "Pin-Provider")
public interface OrderGroupControllerService {
    @RequestMapping(value = "/commons/order-group/create", method = RequestMethod.POST)
    public JSONObject createOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/join", method = RequestMethod.POST)
    public JSONObject joinOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/quit", method = RequestMethod.POST)
    public JSONObject quitOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/by-order-group-id/{orderGroupId}", method = RequestMethod.GET)
    public JSONObject getOrderGroupByOrderGroupId(@PathVariable(value = "orderGroupId") Integer orderGroupId);

    @RequestMapping(value = "/commons/order-group/by-store-id/{storeId}", method = RequestMethod.GET)
    public JSONObject getTopTenOrderGroupsByStoreId(@PathVariable Integer storeId);
}
