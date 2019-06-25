package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(value = "Pin-Provider")
public interface OrderGroupControllerService {
    @RequestMapping(value = "/commons/order-group/create", method = RequestMethod.POST)
    JSONObject createOrderGroup(@RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/join", method = RequestMethod.POST)
    JSONObject joinOrderGroup(@RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/quit", method = RequestMethod.POST)
    JSONObject quitOrderGroup(@RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/commons/order-group/by-order-group-id/{orderGroupId}", method = RequestMethod.GET)
    JSONObject getOrderGroupByOrderGroupId(@PathVariable(value = "orderGroupId") Integer orderGroupId);

    @RequestMapping(value = "/commons/order-group/by-store-id/{storeId}", method = RequestMethod.GET)
    JSONObject getTopTenOrderGroupsByStoreId(@PathVariable Integer storeId);
}
