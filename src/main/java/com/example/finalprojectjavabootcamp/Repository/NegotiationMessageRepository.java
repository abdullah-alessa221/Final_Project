package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationMessageRepository extends JpaRepository<NegotiationMessage, Integer> {


    List<NegotiationMessage> findAllByNegotiation_IdOrderByCreatedAtAsc(Integer negotiationId);

}
