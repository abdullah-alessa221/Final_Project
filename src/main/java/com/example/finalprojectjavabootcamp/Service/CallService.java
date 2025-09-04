    package com.example.finalprojectjavabootcamp.Service;

    import com.example.finalprojectjavabootcamp.Api.ApiException;
    import com.example.finalprojectjavabootcamp.Model.Buyer;
    import com.example.finalprojectjavabootcamp.Model.Calls;
    import com.example.finalprojectjavabootcamp.Model.Seller;
    import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
    import com.example.finalprojectjavabootcamp.Repository.CallRepository;
    import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class CallService {

        private final CallRepository callRepository;
        private final BuyerRepository buyerRepository;
        private final SellerRepository sellerRepository;

        public void startCall(Integer buyerId, Integer sellerId){
            Buyer buyer = buyerRepository.findBuyersById(buyerId);
            Seller seller = sellerRepository.findSellerById(sellerId);

            if (buyer == null) {
                throw new ApiException("buyer not found");
            }
            if (seller == null) {
                throw new ApiException("Seller not found");
            }

            Calls call = new Calls();
            call.setBuyer(buyer);
            call.setSeller(seller);
            call.setStartTime(LocalDateTime.now());
            call.setDuration(0.0);

            callRepository.save(call);

        }

        public void endCall(Integer callId){
            Calls call = callRepository.findCallById(callId);
            if (call == null) {
                throw new ApiException("Call not found");
            }
            if (call.getEndTime() != null) {
                throw new ApiException("Call already ended");
            }

            call.setEndTime(LocalDateTime.now());

            long seconds = Duration.between(call.getStartTime(), call.getEndTime()).toSeconds();
            call.setDuration((double) seconds);

            callRepository.save(call);
        }

        public Double getDuration(Integer callId){
            Calls call = callRepository.findById(callId)
                    .orElseThrow(() -> new ApiException("Call not found"));

            if (call.getEndTime() == null) {
                throw new ApiException("Call is still active, end it first");
            }


            return call.getDuration();
        }

        public List<Calls> getAllCalls(){
            return callRepository.findAll();
        }
    }
