package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mongo.document.UserProductInteraction;
import cn.edu.neu.shop.pin.mongo.repository.UserProductInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户-产品行为度量值服务
 */
@Service
public class UserProductInteractionService {

    private final UserProductInteractionRepository userProductInteractionRepository;

    @Autowired
    public UserProductInteractionService(UserProductInteractionRepository userProductInteractionRepository) {
        this.userProductInteractionRepository = userProductInteractionRepository;
    }

    private void adjustUserProductInteraction(Integer userId, Integer productId, Double adjustment) {
        Optional<UserProductInteraction> interactionOptional = userProductInteractionRepository.findByUserIdAndProductId(userId, productId);
        UserProductInteraction interaction;
        if(interactionOptional.isPresent()) {
            interaction = interactionOptional.get();
            interaction.setInteractionValue(interaction.getInteractionValue()+adjustment);
        } else {
            // 不存在则新建交互记录值
            interaction = new UserProductInteraction();
            interaction.setProductId(productId);
            interaction.setUserId(userId);
            interaction.setInteractionValue(adjustment);
            interaction.setInteractionId(userId + "-" + productId);
        }
        userProductInteractionRepository.save(interaction);
    }

    public void visitProduct(Integer userId, Integer productId) {
        adjustUserProductInteraction(userId, productId, 0.2);
    }

    public void addProductToCart(Integer userId, Integer productId) {
        adjustUserProductInteraction(userId, productId, 0.7);
    }

    public void removeProductToCart(Integer userId, Integer productId) {
        adjustUserProductInteraction(userId, productId, -0.5);
    }

    public void dislikeProduct(Integer userId, Integer productId) {
        adjustUserProductInteraction(userId, productId, -0.5);
    }
}
