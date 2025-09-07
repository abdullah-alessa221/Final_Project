package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.Subscription;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.example.finalprojectjavabootcamp.Repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SellerRepository sellerRepository;
    private final WhatsappService whatsappService;

    public void subscribeMonthly(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);

        if(seller == null){
            throw new ApiException("Seller not found");
        }
        if (seller.getSubscription() != null &&
                "ACTIVE".equals(seller.getSubscription().getStatus())) {
            throw new ApiException("You have a valid subscription.");
        }

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = new Subscription();


        subscription.setType("MONTHLY");
        subscription.setPrice(99.0);
        subscription.setStartDate(now);
        subscription.setEndDate(now.plusMonths(1));
        subscription.setStatus("ACTIVE");
        subscription.setSeller(seller);

        subscriptionRepository.save(subscription);
        whatsappService.sendSubscriptionCreatedMessage(seller.getUser().getPhone(), subscription);

    }

    public void subscribeYearly(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if(seller == null) throw new ApiException("Seller not found");

        if (seller.getSubscription() != null &&
                "ACTIVE".equals(seller.getSubscription().getStatus())) {
            throw new ApiException("You have a valid subscription.");
        }

        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = new Subscription();
        subscription.setType("YEARLY");
        subscription.setPrice(999.0);
        subscription.setStartDate(now);
        subscription.setEndDate(now.plusYears(1));
        subscription.setStatus("ACTIVE");
        subscription.setSeller(seller);

        subscriptionRepository.save(subscription);
        whatsappService.sendSubscriptionCreatedMessage(seller.getUser().getPhone(), subscription);
    }

    public void cancelSubscription(Integer subscriptionId){
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null) throw new ApiException("Subscription not found");

        subscription.setStatus("CANCELLED");
        subscription.setEndDate(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        whatsappService.sendSubscriptionCancelledMessage(subscription.getSeller().getUser().getPhone(), subscription);
    }

    public List<Subscription> getAll(){
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Integer id){
        return subscriptionRepository.findSubscriptionById(id);
    }

    public void pauseSubscription(Integer subscriptionId){
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if(subscription == null) throw new ApiException("Subscription not found");
        if(!"ACTIVE".equals(subscription.getStatus())) throw new ApiException("Only active subscriptions can be paused");

        LocalDateTime now = LocalDateTime.now();
        long daysRemaining = java.time.Duration.between(now, subscription.getEndDate()).toDays();
        if(daysRemaining <= 0) throw new ApiException("No remaining days to pause");

        subscription.setStatus("PAUSED");
        subscription.setRemainingDays((int) daysRemaining);
        subscription.setEndDate(now);
        subscriptionRepository.save(subscription);

        whatsappService.sendSubscriptionPausedMessage(subscription.getSeller().getUser().getPhone(), subscription);
    }



    public void resumeSubscription(Integer subscriptionId){
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if(subscription == null) throw new ApiException("Subscription not found");
        if(!"PAUSED".equals(subscription.getStatus())) throw new ApiException("Only paused subscriptions can be resumed");
        if(subscription.getRemainingDays() == null || subscription.getRemainingDays() <= 0) throw new ApiException("No remaining days to resume");

        LocalDateTime now = LocalDateTime.now();
        subscription.setStatus("ACTIVE");
        subscription.setStartDate(now);
        subscription.setEndDate(now.plusDays(subscription.getRemainingDays()));
        subscription.setRemainingDays(0);
        subscriptionRepository.save(subscription);

        whatsappService.sendSubscriptionResumedMessage(subscription.getSeller().getUser().getPhone(), subscription);
    }




}
