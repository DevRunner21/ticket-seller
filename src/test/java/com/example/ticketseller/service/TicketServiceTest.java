package com.example.ticketseller.service;

import com.example.ticketseller.domain.Ticket;
import com.example.ticketseller.domain.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    private static final int THREAD_POOL_SIZE = 32;
    public static final long TICKET_QUANTITY = 100L; // 티켓 개수

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    public Long ticketId = 0L;

    @BeforeEach
    public void setUp() {
        Ticket ticket = Ticket.of(TICKET_QUANTITY);
        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
        ticketId = savedTicket.getId();
    }

    @AfterEach
    public void deleteAll() {
        ticketRepository.deleteAll();
    }

    @Test
    public void test_buyTicket() {
        Long actual = ticketService.buyTicket(ticketId);
        Long expected = 99L;

        assertEquals(expected, actual);
    }

    @Test
    public void 동시에_100명이_티켓구입() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketService.buyTicket(ticketId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        assertEquals(0, ticket.getQuantity());
    }

//    @Test
//    void buyTicket() throws InterruptedException {
//        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//
//        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
//            service.execute(() -> {
//                ticketService.buyTicket(1L);
//            });
//        }
//
//        service.shutdown();
//        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//    }


}