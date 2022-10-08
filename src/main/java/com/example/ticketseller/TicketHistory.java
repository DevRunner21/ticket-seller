package com.example.ticketseller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ticket_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ticket_id")
    private Long ticketId;

    public TicketHistory(Long id, Long userId, Long ticketId) {
        this.id = id;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public TicketHistory(Long userId, Long ticketId) {
        this.userId = userId;
        this.ticketId = ticketId;
    }

}
