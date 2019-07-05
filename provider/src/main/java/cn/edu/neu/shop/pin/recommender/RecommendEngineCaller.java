package cn.edu.neu.shop.pin.recommender;

import cn.edu.neu.shop.pin.lock.annotation.MutexLock;
import cn.edu.neu.shop.pin.service.SettingsConstantService;
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

    private final RepresentationDataGenerator representationDataGenerator;

    private final RecommenderCache recommenderCache;

    private final SettingsConstantService settingsConstantService;

    @Autowired
    public RecommendEngineCaller(RepresentationDataGenerator representationDataGenerator, RecommenderCache recommenderCache, SettingsConstantService settingsConstantService) {
        this.representationDataGenerator = representationDataGenerator;
        this.recommenderCache = recommenderCache;
        this.settingsConstantService = settingsConstantService;
    }

    /**
     * 更新推荐系统缓存 rank 列表
     * 使用互斥锁防止重入
     */
    @MutexLock(key = "updateRecommendationModel")
    public void updateRecommendationModel() {
        try {
            String recommenderUrl = settingsConstantService.findByKey("recommender_url");
            logger.info("推荐服务器 URL="+recommenderUrl);
            JSONObject reps = representationDataGenerator.generateAllRepresentation();
            JSONArray ids = reps.getJSONObject("user").getJSONArray("ids");
            JSONArray ranks = restTemplate.postForEntity(
                    recommenderUrl,
                    reps,
                    JSONArray.class).getBody();
            logger.info("已接收到模型返回的 rank： \n" + ranks);
            recommenderCache.updateCache(ids, ranks);
            logger.info("缓存更新完成");
        }catch (Exception ex) {
            logger.error("更新rank时出现错误" + ex.getMessage());
        }
    }
}
