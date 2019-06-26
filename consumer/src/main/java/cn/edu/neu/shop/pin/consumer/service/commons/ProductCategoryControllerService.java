package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface ProductCategoryControllerService {
    @RequestMapping(value = "/commons/product/category/get-all-by-layer", method = RequestMethod.POST)
    public JSONObject getCategoryByLayer(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/product/category/all", method = RequestMethod.GET)
    public JSONObject geAllCategory();
}
