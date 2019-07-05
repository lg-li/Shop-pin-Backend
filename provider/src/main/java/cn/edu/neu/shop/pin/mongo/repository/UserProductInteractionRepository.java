package cn.edu.neu.shop.pin.mongo.repository;

import cn.edu.neu.shop.pin.mongo.document.UserProductInteraction;
import cn.edu.neu.shop.pin.mongo.document.UserProductRecommendedRank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProductInteractionRepository extends MongoRepository<UserProductInteraction, String> {

    Optional<UserProductInteraction> findByUserIdAndProductId(Integer userId, Integer productId);
}
