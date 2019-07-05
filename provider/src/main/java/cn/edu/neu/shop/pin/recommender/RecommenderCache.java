package cn.edu.neu.shop.pin.recommender;

import cn.edu.neu.shop.pin.mongo.document.UserProductRecommendedRank;
import cn.edu.neu.shop.pin.mongo.repository.UserProductRecommendedRankRepository;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RecommenderCache {

    private static Logger logger = LoggerFactory.getLogger(RecommenderCache.class);

    @Autowired
    private UserProductRecommendedRankRepository userProductRecommendedRankRepository;

    public List<Integer> getCachedRankByUser(Integer userId){
        Optional<UserProductRecommendedRank> rankOptional = userProductRecommendedRankRepository.findById(userId);
        if(rankOptional.isPresent()) {
            UserProductRecommendedRank rank = rankOptional.get();
            return rank.getRank();
        } else {
            logger.info("用户 " + userId + " 的推荐组合缓存不存在，等待下一批模型推理时再行生成推荐。");
            return null;
        }
    }

    public void updateCache(JSONArray userIds, JSONArray ranks){
        logger.info("预测用户表长度=" + userIds.size() + "\n Rank 长度=" + ranks.size());
        int rankSize = ranks.size();
        for(int i = 0; i < userIds.size(); i++) {
            Integer userId = userIds.getInteger(i);
            if(userId >= rankSize) {
                logger.error("预测用户超出rank表范围，跳过此用户");
                continue;
            }
            UserProductRecommendedRank rank = new UserProductRecommendedRank();
            rank.setUserId(userId);
            rank.setRank(ranks.getJSONArray(userId).toJavaList(Integer.class));
            userProductRecommendedRankRepository.save(rank);
        }
    }
}
