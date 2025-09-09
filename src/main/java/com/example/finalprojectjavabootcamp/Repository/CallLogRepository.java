package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.CallLog;
import com.example.finalprojectjavabootcamp.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CallLogRepository extends JpaRepository<CallLog,Integer> {
    List<CallLog> findByCustomerNumber(String customerNumber);

    List<CallLog> findByStartedAt(LocalDateTime startedAt);

    List<CallLog> findByStatus(String status);

    List<CallLog> findBySeller(Seller seller);

    List<CallLog> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<CallLog> findBySellerAndStatus(Seller seller, String status);

    Optional<CallLog> findCallLogById(String id);
}

