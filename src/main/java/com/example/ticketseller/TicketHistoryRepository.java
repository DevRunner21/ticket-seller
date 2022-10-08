package com.example.ticketseller;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {

    Long countTicketHistoriesByUserIdAndTicketId(Long userId, Long ticketId);

}
