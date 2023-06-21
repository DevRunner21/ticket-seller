package com.example.ticketseller.aop_redisson;

import com.example.ticketseller.TicketHistory;
import com.example.ticketseller.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AopRedissonTicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;

    @Transactional
    public void buyTicket(Long userId, Long ticketId) {
        Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
        log.info("----------- " + count);
        if (count > 0) {
            log.info("이미 티켓을 구매하셨습니다. : " + count);
            return;
        }
        ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
        log.info("티켓 구매 성공!!!!!");
    }

}
