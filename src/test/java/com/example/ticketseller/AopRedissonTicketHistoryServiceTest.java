package com.example.ticketseller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.ticketseller.aop_redisson.AopRedissonTicketHistoryProxy;
import com.example.ticketseller.aop_redisson.AopRedissonTicketHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AopRedissonTicketHistoryServiceTest {

    private static final int THREAD_POOL_SIZE = 5;

    @Autowired
    private AopRedissonTicketHistoryProxy ticketHistoryService;

    @Test
    void buyTicket() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            int finalI = i;
            service.execute(() -> {
                    ticketHistoryService.buyTicket(Long.valueOf(1L), 1L);
            });
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}
