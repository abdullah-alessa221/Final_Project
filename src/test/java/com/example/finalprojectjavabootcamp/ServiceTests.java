package com.example.finalprojectjavabootcamp;

import com.example.finalprojectjavabootcamp.DTOIN.BuyerDTOIn;
import com.example.finalprojectjavabootcamp.Model.Buyer;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.Subscription;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.BuyerRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import com.example.finalprojectjavabootcamp.Repository.SubscriptionRepository;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import com.example.finalprojectjavabootcamp.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private WhatsappService whatsappService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private BuyerService buyerService;

    private Seller seller;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setPhone("0501234567");
        user.setName("Test User");

        seller = new Seller();
        seller.setId(1);
        seller.setUser(user);
    }

    // ===== SubscriptionService tests =====
    @Test
    void testSubscribeMonthly() {
        when(sellerRepository.findSellerById(1)).thenReturn(seller);
        seller.setSubscription(null);

        subscriptionService.subscribeMonthly(1);

        verify(subscriptionRepository).save(any(Subscription.class));
        verify(whatsappService).sendSubscriptionCreatedMessage(anyString(), any(Subscription.class));
    }

    @Test
    void testCancelSubscription() {
        Subscription sub = new Subscription();
        sub.setId(1);
        sub.setStatus("ACTIVE");
        sub.setSeller(seller);

        when(subscriptionRepository.findSubscriptionById(1)).thenReturn(sub);

        subscriptionService.cancelSubscription(1);

        assertThat(sub.getStatus()).isEqualTo("CANCELLED");
        verify(subscriptionRepository).save(sub);
        verify(whatsappService).sendSubscriptionCancelledMessage(anyString(), eq(sub));
    }


    // ===== UserService tests =====
    @Test
    void testBlockUser() {
        user.setStatus("ACTIVE");
        when(userRepository.findUserById(1)).thenReturn(user);

        userService.blockUser(1);

        assertThat(user.getStatus()).isEqualTo("BLOCKED");
        verify(userRepository).save(user);
    }

    @Test
    void testGetBlockedUsers() {
        User blockedUser = new User();
        blockedUser.setId(2);
        blockedUser.setStatus("BLOCKED");

        List<User> users = new ArrayList<>();
        users.add(user); // ACTIVE
        users.add(blockedUser);

        when(userRepository.findAll()).thenReturn(users);

        List<?> result = userService.getBlockedUsers();

        assertThat(result).hasSize(1);
    }

    // ===== BuyerService test =====
    @Test
    void testUpdateBuyer() {
        user.setBuyer(new Buyer());
        when(userRepository.findUserById(1)).thenReturn(user);

        BuyerDTOIn dto = new BuyerDTOIn();
        dto.setName("Updated Name");
        dto.setEmail("updated@test.com");
        dto.setPhone("0555555555");
        dto.setLocation("Riyadh");
        dto.setPassword("1234");
        dto.setConfirmPassword("1234");

        buyerService.updateBuyer(1, dto);

        assertThat(user.getName()).isEqualTo("Updated Name");
        assertThat(user.getEmail()).isEqualTo("updated@test.com");
        verify(userRepository).save(user);
    }
}
