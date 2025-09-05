package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.NegotiationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationMessageRepository extends JpaRepository<NegotiationMessage, Integer> {

    NegotiationMessage findNegotiationMessageById(Integer id);

    List<NegotiationMessage> findAllByNegotiation_IdOrderByCreatedAtAsc(Integer negotiationId);

    List<NegotiationMessage> findAllByNegotiation_IdOrderByCreatedAtDesc(Integer negotiationId);

}
