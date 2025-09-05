package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Rating;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BuyerRepository buyerRepository;

    public List<Rating> getAllRatingsByBuyerId(Integer buyerId){
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }
        return ratingRepository.findAllByBuyer(buyer);
    }
}
