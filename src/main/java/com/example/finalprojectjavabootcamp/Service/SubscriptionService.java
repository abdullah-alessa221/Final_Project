package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.Subscription;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.example.finalprojectjavabootcamp.Repository.SubscriptionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SellerRepository sellerRepository;

    public void subscribeMonthly(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);

        if(seller == null){
            throw new ApiException("Seller not found");
        }
        if (seller.getSubscription() != null &&
                "ACTIVE".equals(seller.getSubscription().getStatus())) {
            throw new ApiException("You have a valid subscription.");
        }
        Subscription subscription = new Subscription();

        subscription.setType("MONTHLY");
        subscription.setPrice(99.0);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1));
        subscription.setStatus("ACTIVE");

        subscriptionRepository.save(subscription);

    }

    public void subscribeYearly(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);

        if (seller == null) {
            throw new ApiException("Seller not found");
        }
        if (seller.getSubscription() != null &&
                "ACTIVE".equals(seller.getSubscription().getStatus())) {
            throw new ApiException("You have a valid subscription.");
        }

        Subscription subscription = new Subscription();

        subscription.setType("YEARLY");
        subscription.setPrice(999.0);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusYears(1));
        subscription.setStatus("ACTIVE");

        subscriptionRepository.save(subscription);

    }

    public void cancelSubscription(Integer subscriptionId){
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null) {
            throw new ApiException("There is no subscription");
        }

        subscription.setStatus("CANCELLED");
        subscription.setEndDate(LocalDateTime.now());

        subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAll(){
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Integer id){
        return subscriptionRepository.findSubscriptionById(id);
    }


}
