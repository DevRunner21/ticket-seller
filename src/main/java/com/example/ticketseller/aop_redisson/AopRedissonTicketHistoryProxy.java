package com.example.ticketseller.aop_redisson;

import com.pawn.autodistributedlock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AopRedissonTicketHistoryProxy {

    private final AopRedissonTicketHistoryService ticketHistoryService;

    @DistributedLock(key = "#ticketId")
    public void buyTicket(Long userId, Long ticketId) {
        ticketHistoryService.buyTicket(userId, ticketId);
    }

}
