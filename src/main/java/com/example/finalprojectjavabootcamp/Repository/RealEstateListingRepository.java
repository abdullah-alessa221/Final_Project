package com.example.finalprojectjavabootcamp.Repository;


import com.example.finalprojectjavabootcamp.Model.RealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateListingRepository extends JpaRepository<RealEstateListing, Integer> {
    @Query("SELECT r FROM RealEstateListing r WHERE LOWER(r.real_estate_type) = LOWER(:type)")
    List<RealEstateListing> findRealEstateListingsByReal_estate_type(String type);
    List<RealEstateListing> findRealEstateListingsByIsRental(Boolean isRental);
    List<RealEstateListing> findRealEstateListingsByRoomsGreaterThanEqual(Integer rooms);
    List<RealEstateListing> findRealEstateListingsByBathroomsGreaterThanEqual(Integer bathrooms);
    List<RealEstateListing> findRealEstateListingsBySquareMeterGreaterThanEqual(Integer squareMeter);
    List<RealEstateListing> findRealEstateListingsByNeighborhood(String neighborhood);
}

