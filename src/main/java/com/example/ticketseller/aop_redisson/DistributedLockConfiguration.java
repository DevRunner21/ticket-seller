package com.example.ticketseller.aop_redisson;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedLockConfiguration {

    @Bean
    public RLockAspect rLockAspect(RedissonClient redissonClient) {
        return new RLockAspect(redissonClient);
    }

}
