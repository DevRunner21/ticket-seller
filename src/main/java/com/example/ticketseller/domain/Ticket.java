package com.example.ticketseller.domain;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    protected Ticket() {}

    public static Ticket of(Long quantity) {
        Ticket ticket = new Ticket();
        ticket.setQuantity(quantity);

        return ticket;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    private void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long decreaseQuantity() {
        long decreasedQuantity = this.quantity - 1;
        if (decreasedQuantity < 0) {
            throw new RuntimeException("Ticket 수량 부족");
        }

        this.quantity = decreasedQuantity;

        return this.quantity;
    }

}
