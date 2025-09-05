package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Rating;
import com.example.finalprojectjavabootcamp.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {
    List<Rating> findAllByBuyer(Buyer buyer);

    List<Rating> findRatingsBySeller(Seller seller);
}
