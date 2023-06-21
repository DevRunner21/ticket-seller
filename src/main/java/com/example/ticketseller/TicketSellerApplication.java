package com.example.ticketseller;

import com.pawn.autodistributedlock.EnableDistributedLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDistributedLock
@SpringBootApplication
public class TicketSellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketSellerApplication.class, args);
    }

}
