package com.example.ticketseller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedissonTicketHistoryServiceTest {

    private static final int THREAD_POOL_SIZE = 5;

    @Autowired
    private RedissonTicketHistoryService ticketHistoryService;

    @Test
    void buyTicket() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            service.execute(() -> {
                ticketHistoryService.buyTicket(1L, 1L);
            });
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}