package com.example.ticketseller.redisson;

import com.example.ticketseller.redisson.RedissonTicketHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonTicketHistoryProxy {

    private final RedissonClient redissonClient;

    private final RedissonTicketHistoryService ticketHistoryService;

    public void buyTicket(Long userId, Long ticketId) {

        RLock lock = redissonClient.getLock("test");
        long threadId = Thread.currentThread().getId();
        try {

            log.info("{} isLocked : " + lock.isLocked(), threadId);

            if (!(lock.tryLock(10, 10, TimeUnit.SECONDS))) {
                log.info("Lock 흭득 실패!!!");
                return;
            }
            log.info("{} isLocked2 : " + lock.isLocked(), threadId);

            ticketHistoryService.buyTicket(userId, ticketId);

        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("aaaaaaaaaaaaaa");
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }

    }


}
