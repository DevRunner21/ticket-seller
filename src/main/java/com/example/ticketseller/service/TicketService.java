package com.example.ticketseller.service;

import com.example.ticketseller.domain.Ticket;
import com.example.ticketseller.domain.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Long buyTicket(Long ticketId) {
        Ticket foundTicket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Not found Ticket : " + ticketId));

        return foundTicket.decreaseQuantity();
    }

}
