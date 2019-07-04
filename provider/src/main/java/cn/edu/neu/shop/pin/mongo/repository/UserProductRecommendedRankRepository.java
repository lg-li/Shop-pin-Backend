package cn.edu.neu.shop.pin.mongo.repository;

import cn.edu.neu.shop.pin.mongo.document.UserProductRecommendedRank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProductRecommendedRankRepository extends MongoRepository<UserProductRecommendedRank, Integer> {
}
