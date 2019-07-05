package cn.edu.neu.shop.pin.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserProductInteraction")
public class UserProductInteraction {
    @Id
    private String interactionId;

    private Integer userId;

    private Integer productId;

    private Double interactionValue;

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getInteractionValue() {
        return interactionValue;
    }

    public void setInteractionValue(Double interactionValue) {
        this.interactionValue = interactionValue;
    }
}
