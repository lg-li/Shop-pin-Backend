package cn.edu.neu.shop.pin.recommender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendationTest {
    @Autowired
    private RepresentationDataGenerator representationDataGenerator;

    @Test
    public void testRepresentationGenerator() {
        System.out.println(representationDataGenerator.generateAllRepresentation());
    }

}
