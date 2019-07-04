package cn.edu.neu.shop.pin.recommender;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.mongo.document.UserProductInteraction;
import cn.edu.neu.shop.pin.mongo.repository.UserProductInteractionRepository;
import cn.edu.neu.shop.pin.nlp.NLPUtil;
import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.security.UserService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 表征数据生成器
 * 用于推荐系统嵌入
 */
@Component
public class RepresentationDataGenerator {

    private static Logger logger = LoggerFactory.getLogger(RepresentationDataGenerator.class);


    private final UserService userService;
    private final ProductService productService;
    private final UserProductInteractionRepository userProductInteractionRepository;

    // 用户表征：性别，余额数量级，积分数量级
    private final static int USER_GENDER = 0;
    private final static int USER_BALANCE_CLASS = 1;
    private final static int USER_CREDIT_CLASS = 2;

    @Autowired
    public RepresentationDataGenerator(UserService userService, ProductService productService, UserProductInteractionRepository userProductInteractionRepository) {
        logger.info("表征生成器初始化");
        this.userService = userService;
        this.productService = productService;
        this.userProductInteractionRepository = userProductInteractionRepository;
    }

    private static int getBalanceClass(BigDecimal balance) {
        return balance.intValue();
    }

    /**
     * 生成所有用户、表征
     * @return 表征数据
     */
    public JSONObject generateAllRepresentation() {
        logger.info("开始生成推荐表征");
        JSONObject reps = new JSONObject();
        reps.put("interaction", generateInteractionRepresentation());
        reps.put("user", generateUserRepresentation());
        reps.put("product", generateProductRepresentation());
        return reps;
    }

    /**
     * 生成平台用户表征
     *
     * @return 用户表征数据
     */
    private JSONObject generateUserRepresentation() {
        logger.info("生成用户表征...");
        List<PinUser> userList = userService.findAll();
        List<Integer> userIds = new ArrayList<>();
        List<Integer> userRepRow = new ArrayList<>();
        List<Integer> userRepCol = new ArrayList<>();
        List<Integer> userRepData = new ArrayList<>();
        for (PinUser user : userList) {
            // 添加 USER ID 便于存储 rank 标记
            userIds.add(user.getId());

            userRepRow.add(user.getId());
            userRepCol.add(USER_GENDER);
            userRepData.add(user.getGender());

            userRepRow.add(user.getId());
            userRepCol.add(USER_BALANCE_CLASS);
            userRepData.add(getBalanceClass(user.getBalance()));

            userRepRow.add(user.getId());
            userRepCol.add(USER_CREDIT_CLASS);
            userRepData.add(user.getCredit());
        }
        JSONObject userReps = packRepData(userRepRow, userRepCol, userRepData);
        userReps.put("ids", userIds);
        return userReps;
    }

    // 商品表征：分类ID，价格，销量，访问量，好评率，产品得分
    private final static int PRODUCT_CATEGORY_ID = 0;
    private final static int PRODUCT_PRICE = 1;
    private final static int PRODUCT_SALE_COUNT = 2;
    private final static int PRODUCT_VISIT_COUNT = 3;
    private final static int PRODUCT_GOOD_COMMENT_RATE = 4;
    private final static int PRODUCT_AVERAGE_RATE_SCORE = 5;


    /**
     * 生成商品表征
     *
     * @return 商品表征
     */
    private JSONObject generateProductRepresentation() {
        logger.info("生成商品表征...");
        List<PinProduct> productList = productService.findAll();
        List<Integer> productRepRow = new ArrayList<>();
        List<Integer> productRepCol = new ArrayList<>();
        List<Double> productRepData = new ArrayList<>();
        for (PinProduct product : productList) {
            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_CATEGORY_ID);
            productRepData.add(product.getCategoryId().doubleValue());

            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_PRICE);
            productRepData.add(product.getPrice().doubleValue());

            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_SALE_COUNT);
            productRepData.add(product.getSoldCount().doubleValue());

            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_VISIT_COUNT);
            productRepData.add(product.getVisitCount().doubleValue());

            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_GOOD_COMMENT_RATE);
            productRepData.add(0.0);

            productRepRow.add(product.getId());
            productRepCol.add(PRODUCT_AVERAGE_RATE_SCORE);
            productRepData.add(1.0);

        }
        return packRepData(productRepRow, productRepCol, productRepData);
    }

    /**
     * 生成交互表征
     * @return 交互表征
     */
    private JSONObject generateInteractionRepresentation() {
        logger.info("生成交互表征...");
        List<UserProductInteraction> interactionList = userProductInteractionRepository.findAll();
        List<Integer> interactionRepRow = new ArrayList<>();
        List<Integer> interactionRepCol = new ArrayList<>();
        List<Double> interactionRepData = new ArrayList<>();
        // 注入
        for(UserProductInteraction interaction : interactionList) {
            interactionRepRow.add(interaction.getUserId());
            interactionRepCol.add(interaction.getProductId());
            interactionRepData.add(interaction.getInteractionValue());
        }
        return packRepData(interactionRepRow, interactionRepCol, interactionRepData);
    }

    private JSONObject packRepData(List repRow, List repCol, List repData) {
        JSONObject rep = new JSONObject();
        rep.put("row", repRow);
        rep.put("col", repCol);
        rep.put("data", repData);
        return rep;
    }

}
