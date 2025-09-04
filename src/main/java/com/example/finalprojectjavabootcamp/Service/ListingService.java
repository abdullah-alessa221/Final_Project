package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final SellerRepository sellerRepository;

    public List<Listing> getAllListings(){
        return listingRepository.findAll();
    }

    public List<Listing> getListingBySellerId(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null){
            throw new ApiException("Seller not found");
        }
        return listingRepository.findListingsBySeller(seller);
    }

    public Listing getListingById(Integer id){
        Listing listing = listingRepository.getListingById(id);
        if (listing == null){
            throw new ApiException("Listing not found");
        }
        return listing;
    }

    public void deleteListing(Integer id){
        Listing listing = listingRepository.getListingById(id);
        if (listing == null){
            throw new ApiException("Listing not found");
        }
        listingRepository.delete(listing);
    }
}
