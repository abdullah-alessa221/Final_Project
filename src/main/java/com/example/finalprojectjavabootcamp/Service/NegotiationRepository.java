package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Model.Negotiation;
import org.springframework.data.repository.Repository;

interface NegotiationRepository extends Repository<Negotiation, Integer> {
    Negotiation findNegotiationById(Integer id);
}
