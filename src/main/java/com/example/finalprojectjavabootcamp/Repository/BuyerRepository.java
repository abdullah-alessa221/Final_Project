package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer,Integer> {
    Buyer findBuyersById(Integer id);
}
