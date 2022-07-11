package com.example.ticketseller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TicketHistoryService {

    private static final Logger log = LoggerFactory.getLogger(TicketHistoryService.class);

    private final TicketHistoryRepository ticketHistoryRepository;

    public TicketHistoryService(TicketHistoryRepository ticketHistoryRepository) {
        this.ticketHistoryRepository = ticketHistoryRepository;
    }

    @Transactional
    public void buyTicket(Long userId, Long ticketId) {
        Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
        if (count > 0) {
            log.info("이미 티켓을 구매하셨습니다.");
            return;
        }
        ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
        log.info("티켓 구매 성공!!!!!");
    }

}
