package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public interface StoreControllerService {
    @RequestMapping(value = "/commons/store/{storeId}/products/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public JSONObject getProductInfoByStoreId(@PathVariable(value = "storeId") Integer storeId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/commons/store/{storeId}", method = RequestMethod.GET)
    public JSONObject getStoreInfoByStoreId(@PathVariable Integer storeId);
}
