package com.example.ticketseller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedissonTicketHistoryService {

    private final RedissonClient redissonClient;
    private final TicketHistoryRepository ticketHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void buyTicket(Long userId, Long ticketId) {

        RLock lock = redissonClient.getLock("test");
        final String worker = Thread.currentThread().getName();

        try {
            if (!(lock.tryLock(1, 100, TimeUnit.SECONDS))) {
                log.info("Lock 흭득 실패!!!");
                return;
            }

            Thread.sleep(1000);
            Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
            if (count > 0) {
                log.info("이미 티켓을 구매하셨습니다. : " + count);
                return;
            }
            ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
            log.info("티켓 구매 성공!!!!!");

        } catch (InterruptedException e) {
            e.printStackTrace();
//            log.error("{}", e);
            log.error("aaaaaaaaaaaaaa");
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }

    }

//    @Transactional
//    public void buyTicket(Long userId, Long ticketId) {
//
//        RLock lock = redissonClient.getLock("test");
//
//        try {
//            log.info("11111" + " isLock : " + lock.isLocked());
//            boolean isGetLock = lock.tryLock(10, 10, TimeUnit.SECONDS);
//            log.info("22222" + " isGetLock : " + isGetLock);
//            if (!isGetLock) {
//                log.info("Lock 흭득 실패!!!");
//                return;
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            log.info("{}", e);
//        }
//
//        try {
//            log.info("33333");
//            Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
//            if (count > 0) {
//                log.info("이미 티켓을 구매하셨습니다. : " + count + " isLock : " + lock.isLocked());
//                return;
//            }
//            ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
//            entityManager.flush();
//            log.info("티켓 구매 성공!!!!! : " + count + " isLock : " + lock.isLocked());
//        } finally {
//            if(lock != null && lock.isLocked()) {
//                lock.unlock();
//            }
//        }
//
//    }

    @Transactional
    public void buyTicket2(Long userId, Long ticketId) {

        RLock lock = redissonClient.getLock("test");
        lock.lock(10, TimeUnit.SECONDS);

        try {
            Long count = ticketHistoryRepository.countTicketHistoriesByUserIdAndTicketId(userId, ticketId);
            if (count > 0) {
                log.info("이미 티켓을 구매하셨습니다. : " + count);
                return;
            }
            ticketHistoryRepository.save(new TicketHistory(userId, ticketId));
            log.info("티켓 구매 성공!!!!!");
        } finally {
            lock.unlock();
        }

    }

//    public void setStock(String key, int amount){
//        redissonClient.getBucket(key).set(amount);
//    }
//
//    public int currentStock(String key){
//        return (int) redissonClient.getBucket(key).get();
//    }

}
