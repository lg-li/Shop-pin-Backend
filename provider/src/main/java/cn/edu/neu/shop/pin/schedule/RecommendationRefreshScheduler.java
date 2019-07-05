package cn.edu.neu.shop.pin.schedule;

import cn.edu.neu.shop.pin.recommender.RecommendEngineCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 推荐系统rank刷新定时任务
 */
@Component
public class RecommendationRefreshScheduler {
    private static Logger logger = LoggerFactory.getLogger(RecommendationRefreshScheduler.class);

    @Autowired
    RecommendEngineCaller recommendEngineCaller;

    /**
     * cron字符串决定看门狗每2分钟执行一次清理任务
     */
    @Scheduled(cron = "0 0/2 * * * ? ")
    public void closeTimeoutOrderGroup() {
        logger.info("触发刷新推荐系统模型定时任务。");
        recommendEngineCaller.updateRecommendationModel();
    }
}
