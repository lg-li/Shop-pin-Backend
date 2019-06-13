package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinProductAttributeDefinition;
import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.ProductAttributeDefinitionService;
import cn.edu.neu.shop.pin.service.ProductCategoryService;
import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.img.ImgUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/goods")
public class AdminProductController {
    @Autowired
    UserService userService;
    @Autowired
    PinProductMapper productMapper;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    OrderIndividualService orderIndividualService;
    @Autowired
    ProductAttributeDefinitionService definitionService;

    /**
     * 返回不同类型商品列表
     *
     * @param req
     * @param requestObject
     * @return
     */
    @PostMapping("/goods-list")
    public JSONObject getProducts(HttpServletRequest req, @RequestBody JSONObject requestObject) {
        try {
            Integer storeId = Integer.valueOf(req.getHeader("Current-Store"));
            String queryType = requestObject.getString("queryType");
            JSONObject data = new JSONObject();
            switch (queryType) {
                case "SALING":
                    List<PinProduct> saling = productService.getIsShownProductInfo(storeId);
                    data.put("goodsList", saling);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "READY":
                    List<PinProduct> ready = productService.getIsReadyProductInfo(storeId);
                    data.put("goodsList", ready);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "OUT":
                    List<PinProduct> out = productService.getIsOutProductInfo(storeId);
                    int flag;
                    //删除掉 每一个库存都有的product
                    for (PinProduct item : out) {
                        flag = 0;
                        for (PinProductAttributeValue inner : item.getProductAttributeValues()) {
                            if (inner.getStock() == 0)
                                flag = 1;
                        }
                        if (flag == 0)
                            out.remove(item);
                    }
                    data.put("goodsList", out);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "ALARM":
                    List<PinProduct> alarm = productService.getIsAlarmProductInfo(storeId);
                    //删除掉 每一个库存都有的product
                    for (PinProduct item : alarm) {
                        flag = 0;
                        for (PinProductAttributeValue inner : item.getProductAttributeValues()) {
                            if (inner.getStock() <= 10)
                                flag = 1;
                        }
                        if (flag == 0)
                            alarm.remove(item);
                    }
                    data.put("goodsList", alarm);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                default:
                    return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "获取失败", null);
            }
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, e.getMessage(), null);
        }
    }

    /**
     * 商铺所有者管理本店铺的商品
     * 商品分类部分 获取父级、子级分类名及一些商品信息
     *
     * @param req
     * @return
     */
    @GetMapping("/goods-category")
    public JSONObject getProductFromSameStore(HttpServletRequest req) {
        try {
            String currentStoreId = req.getHeader("Current-Store");
            List<JSONObject> list = productService.getProductInfoFromSameStore(Integer.parseInt(currentStoreId));
            JSONObject data = new JSONObject();
            data.put("list", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/category-list")
    public JSONObject getProductCatrgory() {
        try {
            JSONArray array = productCategoryService.getProductCategory();
            JSONObject categoryList = new JSONObject();
            categoryList.put("categoryList", array);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PutMapping("/update-category")
    public JSONObject updateProductCategory(@RequestBody JSONObject requestJSON) {
        try {
            Integer productId = requestJSON.getInteger("productId");
            Integer categoryId = requestJSON.getInteger("categoryId");
            productService.updateProductCategory(productId, categoryId);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/create-product")
    public JSONObject createProduct(HttpServletRequest req, @RequestBody JSONObject requestJSON) {
        try {
            Integer storeId = Integer.parseInt(req.getHeader("Current-Store"));
            String base64Img = requestJSON.getString("image");
            String url = "http://ww1.sinaimg.cn/large/9d167ea7ly1g3hyzea2xdj20a00a0q33.jpg";
            //String url = ImgUtil.upload(base64Img, "https://sm.ms/api/upload").getBody().getJSONObject("data").getString("url");
            String name = requestJSON.getString("name");
            String info = requestJSON.getString("info");
            String keyword = requestJSON.getString("keyword");
            BigDecimal price = new BigDecimal(requestJSON.getString("price"));
            BigDecimal priceBeforeDiscount = new BigDecimal(requestJSON.getString("priceBeforeDiscount"));
            String unitName = requestJSON.getString("unitName");
            Integer stockCount = requestJSON.getInteger("stockCount");
            BigDecimal shippingFee = new BigDecimal(requestJSON.getString("shippingFee"));
            Integer creditToGive = requestJSON.getInteger("creditToGive");
            BigDecimal cost = new BigDecimal(requestJSON.getString("cost"));
            String description = requestJSON.getString("description");


            PinProduct pinProduct = new PinProduct(storeId, 1, url, name,
                    info, keyword, price, priceBeforeDiscount, unitName, stockCount,
                    0, false, false, true, shippingFee,
                    false, new Date(), creditToGive, cost, 0, description);
            productService.save(pinProduct);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, pinProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/create-sku-definition")
    public JSONObject createSkuDefinition(@RequestBody JSONObject requestJSON) {
        try {
            Integer productId = requestJSON.getInteger("productId");
            String property;
            String value;
            PinProductAttributeDefinition attributeDefinition;
            for (int i = 0; i < requestJSON.getJSONArray("attribute").size(); i++) {
                property = requestJSON.getJSONArray("attribute").getJSONObject(i).getString("attributeName");
                value = requestJSON.getJSONArray("attribute").getJSONObject(i).getString("attributeValue");
                attributeDefinition = new PinProductAttributeDefinition(productId, property, value);
                definitionService.save(attributeDefinition);
            }
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/create-sku")
    public JSONObject createSku(@RequestBody JSONObject requestJSON) {
        try {
            Integer storeId = requestJSON.getInteger("productId");
            JSONObject object;
            String sku;
            Integer stock;
            BigDecimal price;
            String base64Img;
            String imageUrl;
            BigDecimal cost;
            PinProductAttributeValue attributeValue;
            for (int i = 0; i < requestJSON.getJSONArray("list").size(); i++) {
                object = requestJSON.getJSONArray("list").getJSONObject(i);
                sku = object.getString("sku");
                stock = object.getInteger("stock");
                price = new BigDecimal(object.getString("price"));
                base64Img = object.getString("image");
                imageUrl = ImgUtil.upload(base64Img, "https://sm.ms/api/upload").getBody().getJSONObject("data").getString("url");
                cost = new BigDecimal(object.getString("cost"));
                attributeValue = new PinProductAttributeValue(storeId,sku,stock,price,imageUrl,cost);
            }
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
