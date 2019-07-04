package cn.edu.neu.shop.pin.recommender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecommendEngineCaller {

    private static Logger logger = LoggerFactory.getLogger(RecommendEngineCaller.class);

    private RestTemplate restTemplate =  new RestTemplate();

    @Autowired
    private RepresentationDataGenerator representationDataGenerator;

    @Autowired
    private RecommenderCache recommenderCache;

    /**
     * 更新推荐系统缓存 rank
     */
    public void updateRecommendationModel() {
        JSONObject reps = representationDataGenerator.generateAllRepresentation();
        JSONArray ids = reps.getJSONObject("user").getJSONArray("ids");
        JSONArray ranks = restTemplate.postForEntity(
                 "localhot:5689/fit-model-and-get-ranks",
                 reps,
                 JSONArray.class).getBody();
        logger.info("已接收到模型返回的 rank： \n" + ranks);
        recommenderCache.updateCache(ids, ranks);
        logger.info("缓存更新完成");
    }
}
