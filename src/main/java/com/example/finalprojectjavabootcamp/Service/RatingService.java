package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.RatingDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Payment;
import com.example.finalprojectjavabootcamp.Model.Rating;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.PaymentRepository;
import com.example.finalprojectjavabootcamp.Repository.RatingRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final PaymentRepository paymentRepository;

    public List<Rating> getAllRatingsByBuyerId(Integer buyerId){
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }
        return ratingRepository.findAllByBuyer(buyer);
    }

    public void addRating(Integer buyerId, Integer sellerId, Integer paymentId ,RatingDTOIn ratingDTOIn){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null){
            throw new ApiException("Seller not found");
        }
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null){
            throw new ApiException("Buyer not found");
        }
        Payment payment = paymentRepository.findPaymentById(paymentId);
        if (payment == null){
            throw new ApiException("Payment not found");
        }
        Rating rating = new Rating(null,ratingDTOIn.getRating(),ratingDTOIn.getReview(),seller,buyer,payment,null);
        ratingRepository.save(rating);
    }
}
