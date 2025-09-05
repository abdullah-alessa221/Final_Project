package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findPaymentById(Integer id);

    Payment findPaymentByNegotiation_Id(Integer negotiationId);

    List<Payment> findAllByBuyer_Id(Integer buyerId);

    List<Payment> findAllBySeller_Id(Integer sellerId);

    List<Payment> findAllByBuyer_IdAndSeller_Id(Integer buyerId, Integer sellerId);
}
