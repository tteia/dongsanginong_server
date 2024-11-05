package org.samtuap.inong.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.samtuap.inong.domain.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class DeliveryNotificationScheduler {
    private final NotificationService notificationService;

    @SchedulerLock(name = "shedLock_notify_delivery", lockAtLeastFor = "1m", lockAtMostFor = "59m")
    @Scheduled(cron = "0 0 7 * * *")
    public void notifyTodayDelivery() {
        notificationService.notifyTodayDelivery();
    }
}
