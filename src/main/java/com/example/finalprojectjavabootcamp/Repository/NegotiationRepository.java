package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation,Integer> {
    List<Negotiation> findAllByBuyer_Id(Integer buyerId);

    List<Negotiation> findAllByListing_Id(Integer listingId);

    Negotiation findNegotiationByListing_IdAndBuyer_Id(Integer listingId, Integer buyerId);

    Negotiation findNegotiationById(Integer id);

    @Query("select n from Negotiation n where n.listing.seller.id = ?1")
    List<Negotiation> findNegotiationsBySellerId(Integer sellerId);

    List<Negotiation> findNegotiationsByStatus(String status);
}
