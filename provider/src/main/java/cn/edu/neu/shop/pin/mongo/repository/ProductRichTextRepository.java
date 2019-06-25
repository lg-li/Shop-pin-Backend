package cn.edu.neu.shop.pin.mongo.repository;

import cn.edu.neu.shop.pin.mongo.document.ProductRichTextDescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRichTextRepository extends MongoRepository<ProductRichTextDescription, Integer> {
}
