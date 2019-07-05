package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.*;
import cn.edu.neu.shop.pin.model.*;
import cn.edu.neu.shop.pin.mongo.document.ProductRichTextDescription;
import cn.edu.neu.shop.pin.mongo.repository.ProductRichTextRepository;
import cn.edu.neu.shop.pin.recommender.RecommenderCache;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author flyhero, LLG, CQF, YDY
 */
@Component
public class ProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;
    @Autowired
    private PinOrderItemMapper pinOrderItemMapper;

    private final PinProductMapper pinProductMapper;

    private final PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper;

    private final PinProductAttributeValueMapper pinProductAttributeValueMapper;

    private final PinUserProductCollectionMapper pinUserProductCollectionMapper;

    private final PinUserProductCommentMapper pinUserProductCommentMapper;

    private final StoreService storeService;

    private final ProductRichTextRepository productRichTextRepository;

    private final UserProductRecordService userProductRecordService;

    // 推荐排名缓存
    private final RecommenderCache recommenderCache;

    @Autowired
    public ProductService(PinProductMapper pinProductMapper, PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper, PinProductAttributeValueMapper pinProductAttributeValueMapper, PinUserProductCollectionMapper pinUserProductCollectionMapper, PinUserProductCommentMapper pinUserProductCommentMapper, StoreService storeService, ProductRichTextRepository productRichTextRepository, UserProductRecordService userProductRecordService, RecommenderCache recommenderCache) {
        this.pinProductMapper = pinProductMapper;
        this.pinProductAttributeDefinitionMapper = pinProductAttributeDefinitionMapper;
        this.pinProductAttributeValueMapper = pinProductAttributeValueMapper;
        this.pinUserProductCollectionMapper = pinUserProductCollectionMapper;
        this.pinUserProductCommentMapper = pinUserProductCommentMapper;
        this.storeService = storeService;
        this.productRichTextRepository = productRichTextRepository;
        this.userProductRecordService = userProductRecordService;
        this.recommenderCache = recommenderCache;
    }

    /**
     * 根据商品Id 获取商品详情信息
     *
     * @param productId 商品 ID
     * @return 单个PinProduct类实体
     */
    public PinProduct getProductById(Integer productId) {
        PinProduct pinProduct = pinProductMapper.getProductById(productId);
        PinProductAttributeDefinition ppad = new PinProductAttributeDefinition();
        ppad.setProductId(productId);
        List<PinProductAttributeDefinition> defList = pinProductAttributeDefinitionMapper.select(ppad);
        PinProductAttributeValue ppav = new PinProductAttributeValue();
        ppav.setProductId(productId);
        List<PinProductAttributeValue> valList = pinProductAttributeValueMapper.select(ppav);
        pinProduct.setProductAttributeDefinitions(defList);
        pinProduct.setProductAttributeValues(valList);
        return pinProduct;
    }

    public JSONObject getProductByIdWithOneCommentAndSaveVisitRecord(Integer userId, Integer productId, String ipAddress){
        // 保存访问记录
        userProductRecordService.recordProductVisit(userId, productId, ipAddress);
        return getProductByIdWithOneComment(productId);
    }

    /**
     * 根据商品Id 获取商品详情信息
     *
     * @param productId 商品 ID
     * @return JSON 包装了Product和一条grade为0的（好评）评论
     */
    public JSONObject getProductByIdWithOneComment(Integer productId) {
        PinProduct product = getProductById(productId);
        if (product == null) {
            return null;
        }
        PinStore store = storeService.findById(product.getStoreId());
        product.setStore(store);
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("product", product);
        returnJSON.put("description", getProductRichTextDescription(productId));
        List<JSONObject> list = pinUserProductCommentMapper.getCommentAndUserInfo(productId);
        if (list == null || list.size() == 0) returnJSON.put("comment", null);
        else returnJSON.put("comment", list.get(0));
        return returnJSON;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     *
     * @param storeId  店铺 ID
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @return 商品列表
     */
    public PageInfo<PinProduct> getProductByStoreIdByPage(Integer storeId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            PinProduct pinProduct = new PinProduct();
            pinProduct.setStoreId(storeId);
            pinProductMapper.select(pinProduct);
        });
    }

    /**
     * 根据分类ID，获取该分类下所有在售商品信息
     *
     * @param categoryId 分类ID
     * @param pageNum    分页页码
     * @param pageSize   分页大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getProductByCategoryIdByPage(Integer categoryId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            PinProduct pinProduct = new PinProduct();
            pinProduct.setCategoryId(categoryId);
            pinProductMapper.select(pinProduct);
            // pinProductMapper.getProductByCategoryId(categoryId);
        });
    }

    /**
     * 返回热门商品，支持分页操作
     *
     * @param pageNum  页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getHotProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getHotProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(pinProductMapper::getHotProducts);
    }

    /**
     * 返回推荐商品，支持分页操作
     *
     * @param userId  用户 ID
     * @param pageNum  页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     * @author LLG
     */
    public PageInfo<PinProduct> getRecommendedProductsByPage(Integer userId, int pageNum, int pageSize) {
        List<Integer> rankedIds = recommenderCache.getCachedRankByUser(userId);
        if(rankedIds == null) {
            return getNewProductsByPage(pageNum, pageSize);
        }
        StringBuilder rankStringBuilder = new StringBuilder();
        int len = rankedIds.size();
        for(int i = 0; i < len; i ++) {
            rankStringBuilder.append(rankedIds.get(i));
            if(i != len - 1) {
                rankStringBuilder.append(",");
            }
        }
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mapper.selectByIds(rankStringBuilder.toString());
        });
    }

    /**
     * 返回最新商品，支持分页操作
     *
     * @param pageNum  页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getNewProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getNewProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(pinProductMapper::getNewProducts);
    }

    /**
     * TODO:ydy未测试
     * 判断传入的 order_item 是否属于同一家店铺
     *
     * @param list 传入的数组，由order_item组成
     * @return 如果都属于同一家店铺，则返回true
     */
    boolean isBelongSameStore(List<PinOrderItem> list) {
        boolean isSameStore = true;
        //判断是否属于一家店铺
        Integer storeId = getProductById(list.get(0).getProductId()).getStoreId();
        for (PinOrderItem pinOrderItem : list) {
            Integer id = pinOrderItem.getProductId();
            if (!storeId.equals(getProductById(id).getStoreId()))
                isSameStore = false;
        }
        return isSameStore;
    }

    /**
     * 根据userId和productId判断某一商品是否被某一用户收藏
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 是否被收藏
     */
    public boolean isCollected(Integer userId, Integer productId) {
        PinUserProductCollection p = new PinUserProductCollection();
        p.setUserId(userId);
        p.setProductId(productId);
        List<PinUserProductCollection> list = pinUserProductCollectionMapper.select(p);
        return list.size() != 0;
    }

    /**
     * 获取正在上架的商品信息
     *
     * @param storeId 店铺ID
     * @return 商品信息JSON list
     */
    public List<PinProduct> getIsShownProductInfo(Integer storeId) {
        return pinProductMapper.getIsShownProductInfo(storeId);
    }

    /**
     * 获取已就绪但未上架的商品信息
     *
     * @param storeId 店铺ID
     * @return 商品信息JSON list
     */
    public List<PinProduct> getIsReadyProductInfo(Integer storeId) {
        return pinProductMapper.getIsReadyProductInfo(storeId);
    }

    /**
     * 获取已售空的商品信息
     *
     * @param storeId 店铺ID
     * @return 商品信息JSON list
     */
    public List<PinProduct> getIsOutProductInfo(Integer storeId) {
        return pinProductMapper.getIsOutProductInfo(storeId);
    }

    /**
     * 获取库存预警的商品信息
     *
     * @param storeId 店铺ID
     * @return 商品信息JSON list
     */
    public List<PinProduct> getIsAlarmProductInfo(Integer storeId) {
        return pinProductMapper.getIsAlarmProductInfo(storeId);
    }

    /**
     * 获取同一店铺的商品信息
     *
     * @param storeId 店铺ID
     * @return 商品信息JSON list
     */
    public List<JSONObject> getProductInfoFromSameStore(Integer storeId) {
        return pinProductMapper.getProductFromSameStore(storeId);
    }

    /**
     * @param productId 产品ID
     * @return 富文本字符串
     * @author LLG
     * 获取来自MongoDB的产品富文描述
     */
    public String getProductRichTextDescription(Integer productId) {
        Optional<ProductRichTextDescription> productRichTextDescriptionOptional =
                productRichTextRepository.findById(productId);
        if (productRichTextDescriptionOptional.isPresent()) {
            return productRichTextDescriptionOptional.get().getContent();
        } else {
            // 不存在记录则返回空字符串
            return "";
        }
    }

    /**
     * @param productId 产品ID
     * @param richText  要保存的富文本字符串
     * @author LLG
     * 将富文本描述保存到 Mongo DB
     */
    public void updateProductRichTextDescription(Integer productId, String richText) {
        Optional<ProductRichTextDescription> productRichTextDescriptionOptional =
                productRichTextRepository.findById(productId);
        Date now = new Date();
        if (productRichTextDescriptionOptional.isPresent()) {
            ProductRichTextDescription productRichTextDescription = productRichTextDescriptionOptional.get();
            productRichTextDescription.setContent(richText);
            productRichTextDescription.setEditTime(now);
            productRichTextRepository.save(productRichTextDescription);
        } else {
            ProductRichTextDescription productRichTextDescription = new ProductRichTextDescription();
            productRichTextDescription.setEditTime(now);
            productRichTextDescription.setCreateTime(now);
            productRichTextDescription.setContent(richText);
            productRichTextDescription.setProductId(productId);
            productRichTextRepository.save(productRichTextDescription);
        }
    }

//    /**
//     * 店铺库存预警商品个数
//     * @param storeId 店铺ID
//     * @return 商品个数
//     */
//    public Integer getStockWarningProductNum(Integer storeId) {
//        return pinProductMapper.getNumberOfProductLessStock(storeId);
//    }

    /**
     * 更新商品目录
     *
     * @param productId  商品ID
     * @param categoryId 类别ID
     */
    @Transactional
    public void updateProductCategory(Integer productId, Integer categoryId) {
        pinProductMapper.updateProductCategory(productId, categoryId);
    }

    @Transactional
    public void updateProductIsShownStatus(Integer productId) {
        pinProductMapper.updateIsShownStatus(productId);
    }

    @Transactional
    public void updateProductIsNotShownStatus(Integer productId) {
        pinProductMapper.updateIsNotShownStatus(productId);
    }

    public PageInfo<PinProduct> searchByKeyWordByPage(String keyword, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(
                ()->pinProductMapper.searchByKeyword(keyword));
    }
}
