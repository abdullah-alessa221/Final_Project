package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Call;
import com.example.finalprojectjavabootcamp.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<Call,Integer> {
    List<Call> findByCustomerNumber(String customerNumber);

    List<Call> findByStartedAt(LocalDateTime startedAt);

    List<Call> findByStatus(String status);

    List<Call> findBySeller(Seller seller);

    List<Call> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Call> findBySellerAndStatus(Seller seller, String status);

    Optional<Call> findCallById(String id);
}

