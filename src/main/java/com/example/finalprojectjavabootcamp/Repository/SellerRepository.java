package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.User;
import com.zaxxer.hikari.util.ConcurrentBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    Seller findSellerById(Integer id);

    Seller findSellerByUser(User user);
}
