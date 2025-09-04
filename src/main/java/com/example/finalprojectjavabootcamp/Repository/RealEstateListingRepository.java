package com.example.finalprojectjavabootcamp.Repository;


import com.example.finalprojectjavabootcamp.Model.RealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateListingRepository extends JpaRepository<RealEstateListing,Integer> {
}
