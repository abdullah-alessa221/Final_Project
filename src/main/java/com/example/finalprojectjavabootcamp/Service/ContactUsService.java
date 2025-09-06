package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Contact;
import com.example.finalprojectjavabootcamp.Repository.ContactUsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactUsService {

    private final ContactUsRepository contactUsRepository;

    public void createNew(Contact contactUs){
         contactUsRepository.save(contactUs);
    }

    public List<Contact> getAll(){
        return contactUsRepository.findAll();
    }

    public Contact getById(Integer id){
        Contact contactUs = contactUsRepository.findContactUsById(id);

        if (contactUs == null) {
            throw new ApiException("Not found");
        }

        return contactUs;
    }
}
