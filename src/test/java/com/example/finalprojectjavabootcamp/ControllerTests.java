package com.example.finalprojectjavabootcamp;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.Controller.BuyerController;
import com.example.finalprojectjavabootcamp.Controller.SellerController;
import com.example.finalprojectjavabootcamp.Controller.SubscriptionController;
import com.example.finalprojectjavabootcamp.Controller.UserController;
import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SellerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.BuyerService;
import com.example.finalprojectjavabootcamp.Service.SellerService;
import com.example.finalprojectjavabootcamp.Service.SubscriptionService;
import com.example.finalprojectjavabootcamp.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BuyerController.class, SellerController.class, SubscriptionController.class, UserController.class})
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BuyerService buyerService;

    @MockBean
    private SellerService sellerService;

    @MockBean
    private SubscriptionService subscriptionService;



    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllSubscriptions() throws Exception {
        Mockito.when(subscriptionService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/subscription/get-all"))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(authorities = "BUYER")
    void testFilterListings() throws Exception {
        Listing listing = new Listing();
        listing.setTitle("Car Listing");

        Mockito.when(buyerService.getListingsByFilters(eq("car"), eq(""), eq(false)))
                .thenReturn(Collections.singletonList(listing));

        mockMvc.perform(get("/api/v1/buyer/filter")
                        .param("type", "car"))
                .andExpect(status().isOk());
    }


    // ===== Test getAllUsersDTO =====
    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllUsersDTO() throws Exception {
        Mockito.when(userService.getAllUsersDTO()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/user/get-all-dto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ===== Test getAllSellersDTO =====
    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllSellersDTO() throws Exception {
        Mockito.when(userService.getAllSellersDTO()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/user/sellers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ===== Test getAllBuyerDTO =====
    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllBuyersDTO() throws Exception {
        Mockito.when(userService.getAllBuyerDTO()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/user/buyers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
