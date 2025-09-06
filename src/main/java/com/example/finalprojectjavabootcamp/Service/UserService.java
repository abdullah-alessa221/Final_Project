package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOOUT.BuyerDTOOut;
import com.example.finalprojectjavabootcamp.DTOOUT.SellerDTOOut;
import com.example.finalprojectjavabootcamp.DTOOUT.UserDTOOut;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }

        return user;
    }

    public void deleteUser(Integer id){
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        userRepository.delete(user);
    }


//    private Integer id;
//    private String name;
//    private String email;
//    private String phone;
//    private String location;
//    private String role;


    public List<UserDTOOut> getAllUsersDTO(){
        List<User> users = userRepository.findAll();
        List<UserDTOOut> userDTOOuts = new ArrayList<>();

        for(User user: users){
            if (user != null) {
                UserDTOOut dto = new UserDTOOut();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
                dto.setLocation(user.getLocation());
                dto.setRole(user.getRole());
                dto.setStatus(user.getStatus());

                userDTOOuts.add(dto);

            }
        }
        return userDTOOuts;
    }

    public List<SellerDTOOut> getAllSellersDTO(){
        List<User> users = userRepository.findAll();
        List<SellerDTOOut> sellerDTOOuts = new ArrayList<>();

        for(User user: users){
            if (user.getSeller() != null) {
                Seller seller = user.getSeller();

                SellerDTOOut dto = new SellerDTOOut();

                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
                dto.setLocation(user.getLocation());
                dto.setDescription(seller.getDescription());
                dto.setPreferredContact(seller.getPreferredContact());
                dto.setStatus(user.getStatus());

                sellerDTOOuts.add(dto);
            }
        }
        return sellerDTOOuts;
    }

    public List<BuyerDTOOut> getAllBuyerDTO(){
      List<User> users = userRepository.findAll();
      List<BuyerDTOOut> buyerDTOOuts = new ArrayList<>();



      for(User user: users){
          if (user.getBuyer() != null) {
              BuyerDTOOut dto = new BuyerDTOOut();
              dto.setId(user.getId());
              dto.setName(user.getName());
              dto.setEmail(user.getEmail());
              dto.setLocation(user.getLocation());
              dto.setPhone(user.getPhone());
              dto.setStatus(user.getStatus());

              buyerDTOOuts.add(dto);
          }
      }
      return buyerDTOOuts;
    }

    public void blockUser(Integer userId){
        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }
        if("BLOCKED".equals(user.getStatus())){
            throw new ApiException("User already blocked");
        }
        user.setStatus("BLOCKED");

        userRepository.save(user);
    }

    public List<UserDTOOut> getBlockedUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTOOut> blockedUsers = new ArrayList<>();

        for(User user: users){
            if ("BLOCKED".equals(user.getStatus())) {
                UserDTOOut dto = new UserDTOOut();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
                dto.setLocation(user.getLocation());
                dto.setStatus(user.getStatus());
                dto.setRole(user.getRole());

                blockedUsers.add(dto);
            }
        }
        if (blockedUsers.isEmpty()) {
            throw new ApiException("No blocked users found");
        }
        return blockedUsers;
    }

    public void activeUser(Integer userId){
        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }
        if(user.getStatus().equals("ACTIVE")){
            throw new ApiException("User already active");
        }
        user.setStatus("ACTIVE");

        userRepository.save(user);
    }

}
