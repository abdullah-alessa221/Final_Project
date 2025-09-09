
package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchCarDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchRealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.CarListing;
import com.example.finalprojectjavabootcamp.Model.RealEstateListing;
import com.example.finalprojectjavabootcamp.Model.Search;
import com.example.finalprojectjavabootcamp.Model.Result;


import com.example.finalprojectjavabootcamp.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ResultRepository resultRepository;
    private final CarListingRepository carListingRepository;
    private final RealEstateListingRepository realEstateListingRepository;
    private final BuyerRepository buyerRepository;
    private final NegotiationService negotiationService;

    public void CreateCarSearch(SearchCarDTOIn searchCarDTOIn, Integer buyerId) {
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }
        List<CarListing> listings = carListingRepository.findAll();

        if (searchCarDTOIn.getColor() != null) {
            listings.retainAll(carListingRepository.findCarListingsByColor(searchCarDTOIn.getColor()));
        }

        if (searchCarDTOIn.getMake() != null) {
            listings.retainAll(carListingRepository.findCarListingsByMake(searchCarDTOIn.getMake()));
        }

        if (searchCarDTOIn.getModel() != null) {
            listings.retainAll(carListingRepository.findCarListingsByModel(searchCarDTOIn.getModel()));
        }

        if (searchCarDTOIn.getYear() != null) {
            listings.retainAll(carListingRepository.findCarListingsByYear(searchCarDTOIn.getYear()));
        }

        if (searchCarDTOIn.getFuel_type() != null) {
            listings.retainAll(carListingRepository.findCarListingsByFuelType(searchCarDTOIn.getFuel_type()));
        }

        if (searchCarDTOIn.getMileage() != null) {
            listings.retainAll(carListingRepository.findCarListingsByMileageLessThanEqual((searchCarDTOIn.getMileage())));
        }

        if (searchCarDTOIn.getCity() != null) {
            listings.retainAll(carListingRepository.findCarListingsByCity(searchCarDTOIn.getCity()));
        }

        Search search = new Search(null,"car",searchCarDTOIn.getMake()+ " " + searchCarDTOIn.getModel()+ " " + searchCarDTOIn.getYear(),searchCarDTOIn.getAutoNegotiation(),buyer,null);

        for (CarListing listing : listings) {
                if (searchCarDTOIn.getAutoNegotiation()){
                negotiationService.createAi(listing.getId(),buyerId,new AiDTOIn(searchCarDTOIn.getPrice(),searchCarDTOIn.getNotes()));
                }
                Result result = new Result(null,listing.getListing(),search);
                resultRepository.save(result);
        }

        searchRepository.save(search);
    }

    public void CreateRealEstateSearch(SearchRealEstateDTOIn searchRealEstateDTOIn, Integer buyerId) {

        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }

        List<RealEstateListing> listings = realEstateListingRepository.findAll();

        if (searchRealEstateDTOIn.getType() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByReal_estate_type(searchRealEstateDTOIn.getType()));
        }

        if (searchRealEstateDTOIn.getIsRental() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByIsRental(searchRealEstateDTOIn.getIsRental()));
        }

        if (searchRealEstateDTOIn.getRooms() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByRoomsGreaterThanEqual(searchRealEstateDTOIn.getRooms()));
        }

        if (searchRealEstateDTOIn.getBathrooms() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByBathroomsGreaterThanEqual(searchRealEstateDTOIn.getBathrooms()));
        }

        if (searchRealEstateDTOIn.getSquareMeter() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsBySquareMeterGreaterThanEqual(searchRealEstateDTOIn.getSquareMeter()));
        }

        if (searchRealEstateDTOIn.getNeighborhood() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByNeighborhood(searchRealEstateDTOIn.getNeighborhood()));
        }

        Search search = new Search(null, "real_estate",
                searchRealEstateDTOIn.getType() + " " +
                searchRealEstateDTOIn.getNeighborhood() + " " +
                searchRealEstateDTOIn.getRooms() + "rooms",
                searchRealEstateDTOIn.getAutoNegotiation(),
                buyer,
                null);
        searchRepository.save(search);

        for (RealEstateListing listing : listings) {
            if (searchRealEstateDTOIn.getAutoNegotiation()) {
                negotiationService.createAi(listing.getId(), buyerId,
                        new AiDTOIn(searchRealEstateDTOIn.getPrice(), searchRealEstateDTOIn.getNotes()));
            }
            Result result = new Result(null, listing.getListing(), search);
            resultRepository.save(result);
        }

        searchRepository.save(search);

    }

}