package com.example.ticketseller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedissonTicketHistoryService {

    private final RedissonClient redissonClient;
    private final TicketHistoryRepository ticketHistoryRepository;

    @Transactional
    public void buyTicket(Long userId, Long ticketId) {

        RLock lock = redissonClient.getLock("test");
        lock.lock(10, TimeUnit.SECONDS);

        try {
            Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
            if (count > 0) {
                log.info("이미 티켓을 구매하셨습니다.");
                return;
            }
            ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
            log.info("티켓 구매 성공!!!!!");
        } finally {
            lock.unlock();
        }

    }

}
