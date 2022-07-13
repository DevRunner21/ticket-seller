package com.example.ticketseller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisTicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void buyTicket(Long userId, Long ticketId) {

        String name = String.format("ticket:%d:%d", ticketId, userId);
        boolean isNewRequest = redisTemplate.opsForValue().setIfAbsent(name, "true", 1, TimeUnit.MINUTES);

        if (!isNewRequest) {
            log.info("동일 요청이 여러 번 발생");
            return;
        }

        try {
            Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
            if (count > 0) {
                log.info("이미 티켓을 구매하셨습니다.");
                return;
            }
            ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
            log.info("티켓 구매 성공!!!!!");
        } finally {
            redisTemplate.delete(name);
        }
    }


}
