package cn.edu.neu.shop.pin.recommender;

import cn.edu.neu.shop.pin.mongo.document.UserProductRecommendedRank;
import cn.edu.neu.shop.pin.mongo.repository.UserProductRecommendedRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RecommenderCache {

    @Autowired
    private UserProductRecommendedRankRepository userProductRecommendedRankRepository;

    public List<Integer> getCachedRankByUser(Integer userId){
        Optional<UserProductRecommendedRank> rankOptional = userProductRecommendedRankRepository.findById(userId);
        if(rankOptional.isPresent()) {
            UserProductRecommendedRank rank = rankOptional.get();
            return rank.getRank();
        } else {
            return null;
        }
    }
}
