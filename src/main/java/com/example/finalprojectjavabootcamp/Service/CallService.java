    package com.example.finalprojectjavabootcamp.Service;

    import com.example.finalprojectjavabootcamp.Api.ApiException;
    import com.example.finalprojectjavabootcamp.DTOOUT.BuyerCallDTOOut;
    import com.example.finalprojectjavabootcamp.DTOOUT.SellerCallDTOOut;
    import com.example.finalprojectjavabootcamp.Model.Buyer;
    import com.example.finalprojectjavabootcamp.Model.Calls;
    import com.example.finalprojectjavabootcamp.Model.Seller;
    import com.example.finalprojectjavabootcamp.Model.User;
    import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
    import com.example.finalprojectjavabootcamp.Repository.CallRepository;
    import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
    import com.example.finalprojectjavabootcamp.Repository.UserRepository;
    import com.fasterxml.jackson.core.PrettyPrinter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class CallService {

        private final CallRepository callRepository;
        private final BuyerRepository buyerRepository;
        private final SellerRepository sellerRepository;
        private final UserRepository userRepository;

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
            call.setStatus("ACTIVE");

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

            call.setStatus("COMPLETED");

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


        public List<BuyerCallDTOOut> getAllBuyerCall(Integer buyerId){
            User user = userRepository.findUserById(buyerId);
            if (user == null) {
                throw new ApiException("User not found");
            }
            Buyer buyer = buyerRepository.findBuyersByUser(user);
            if (buyer == null) {
                throw new ApiException("Buyer not found");
            }
            List<BuyerCallDTOOut> buyerCalls = new ArrayList<>();;
            for(Calls c: buyer.getCalls()){
                BuyerCallDTOOut dto = new BuyerCallDTOOut();
                dto.setCallId(c.getId());
                dto.setStartTime(c.getStartTime());
                dto.setEndTime(c.getEndTime());
                dto.setDuration(c.getDuration());
                dto.setStatus(c.getStatus());

                if (c.getSeller() != null && c.getSeller().getUser() != null) {
                    dto.setSellerName(c.getSeller().getUser().getName());
                }else {
                    dto.setSellerName(null);
                }

                buyerCalls.add(dto);
            }
            return buyerCalls;
        }

        public List<SellerCallDTOOut> getAllSellerCall(Integer sellerId){
            User user = userRepository.findUserById(sellerId);

            if (user == null) {
                throw new ApiException("User not found");
            }

            Seller seller = sellerRepository.findSellerByUser(user);

            if (seller == null) {
                throw new ApiException("Seller not found");
            }

            List<SellerCallDTOOut> sellerCalls = new ArrayList<>();
            for (Calls c : seller.getCalls()) {
                SellerCallDTOOut dto = new SellerCallDTOOut();
                dto.setCallId(c.getId());
                dto.setStartTime(c.getStartTime());
                dto.setEndTime(c.getEndTime());
                dto.setDuration(c.getDuration());
                dto.setStatus(c.getStatus());

                if (c.getBuyer() != null && c.getBuyer().getUser() != null) {
                    dto.setBuyerName(c.getBuyer().getUser().getName());
                } else {
                    dto.setBuyerName(null);
                }


                sellerCalls.add(dto);
            }

            return sellerCalls;
        }

    }
