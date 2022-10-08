package com.example.ticketseller;

import com.example.ticketseller.aop_redisson.EnableDistritbutedLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@EnableDistritbutedLock
@SpringBootApplication
public class TicketSellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketSellerApplication.class, args);
    }

}
