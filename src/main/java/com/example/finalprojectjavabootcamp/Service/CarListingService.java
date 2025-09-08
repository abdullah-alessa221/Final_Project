package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.CarDTOIn;
import com.example.finalprojectjavabootcamp.Model.CarListing;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Repository.CarListingRepository;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarListingService {

    private final CarListingRepository carListingRepository;
    private final SellerRepository sellerRepository;
    private final ListingRepository listingRepository;

    public List<CarListing> getAllCarListings(){
        return carListingRepository.findAll();
    }

    public void listCar(CarDTOIn carDTOIn, Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null){
            throw new ApiException("Seller not found");
        }
        Listing listing = new Listing(null,carDTOIn.getTitle(), carDTOIn.getDescription(), "car", "listed",carDTOIn.getLeast_price(),carDTOIn.getCity(),seller,null,null,null,null,null);
        CarListing carListing = new CarListing(null,carDTOIn.getCar_type(), carDTOIn.getMake(),carDTOIn.getModel(),carDTOIn.getYear(),carDTOIn.getColor(),carDTOIn.getFuel_type(),carDTOIn.getMileage(),listing);
        listing.setCarListing(carListing);
        carListing.setListing(listing);
        listingRepository.save(listing);
        carListingRepository.save(carListing);
    }

}
