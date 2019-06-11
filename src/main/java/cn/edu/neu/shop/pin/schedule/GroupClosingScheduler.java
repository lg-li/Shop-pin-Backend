package cn.edu.neu.shop.pin.schedule;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@Component
public class GroupClosingScheduler {

    @Autowired
    private OrderGroupService orderGroupService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * cron字符串决定看门狗每1分钟执行一次清理任务
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeTimeoutOrderGroup() {
        List<PinOrderGroup> orderGroups = orderGroupService.getOrdersByStatus(PinOrderGroup.STATUS_PINGING);
        orderGroups.forEach(orderGroup -> {
            executorService.submit(()->{
                // 并发更新团单状态
                finishGroupOrder(orderGroup);
            });
        });
    }

    /**
     * 关闭指定的团单（设置为已结束，并标记结束时间）
     * @param orderGroup 团单实体
     */
    private void finishGroupOrder(PinOrderGroup orderGroup) {
        orderGroup.setActualFinishTime(new Date());
        orderGroup.setStatus(PinOrderGroup.STATUS_FINISHED);
        orderGroupService.update(orderGroup);
    }
}
