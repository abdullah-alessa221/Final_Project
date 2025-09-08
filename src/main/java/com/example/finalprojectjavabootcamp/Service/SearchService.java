
package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchCarDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchRealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.CarListing;
import com.example.finalprojectjavabootcamp.Model.RealEstateListing;
import com.example.finalprojectjavabootcamp.Repository.CarListingRepository;
import com.example.finalprojectjavabootcamp.Repository.RealEstateListingRepository;
import com.example.finalprojectjavabootcamp.Repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final CarListingRepository carListingRepository;
    private final RealEstateListingRepository realEstateListingRepository;
    private final NegotiationService negotiationService;

    public void CreateCarSearch(SearchCarDTOIn searchCarDTOIn, Integer buyerId) {
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
        if (searchCarDTOIn.getAutoNegotiation()){
            for (CarListing listing : listings) {
                negotiationService.createAi(listing.getId(),buyerId,new AiDTOIn(searchCarDTOIn.getPrice(),searchCarDTOIn.getNotes()));
            }
        }

    }

    public void CreateRealEstateSearch(SearchRealEstateDTOIn searchRealEstateDTOIn, Integer buyerId) {
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

        if (searchRealEstateDTOIn.getAutoNegotiation()) {
            for (RealEstateListing listing : listings) {
                negotiationService.createAi(listing.getId(), buyerId, new AiDTOIn(searchRealEstateDTOIn.getPrice(), searchRealEstateDTOIn.getNotes()));
            }
        }
    }

}