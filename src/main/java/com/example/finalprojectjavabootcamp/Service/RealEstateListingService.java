package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.RealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.RealEstateListing;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.RealEstateListingRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RealEstateListingService {

    private final RealEstateListingRepository realEstateListingRepository;
    private final ListingRepository listingRepository;
    private final SellerRepository sellerRepository;

    public List<RealEstateListing> getAllRealEstateListings(){
        return realEstateListingRepository.findAll();
    }

    public void listRealEstate(RealEstateDTOIn realEstateDTOIn, Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null){
            throw new ApiException("Seller not found");
        }

        Listing listing = new Listing(null,realEstateDTOIn.getTitle(), realEstateDTOIn.getDescription(), "real_estate", "Listed",realEstateDTOIn.getLeast_price(),realEstateDTOIn.getCity(),seller,null,null,null,null);
        RealEstateListing realEstateListing = new RealEstateListing(null,realEstateDTOIn.getType(),realEstateDTOIn.getIsRental(),realEstateDTOIn.getRooms(), realEstateDTOIn.getBathrooms(), realEstateDTOIn.getSquareMeter(),realEstateDTOIn.getCity(), realEstateDTOIn.getNeighborhood(), null);
        listing.setRealEstateListing(realEstateListing);
        realEstateListing.setListing(listing);
        listingRepository.save(listing);
        realEstateListingRepository.save(realEstateListing);
    }
}
