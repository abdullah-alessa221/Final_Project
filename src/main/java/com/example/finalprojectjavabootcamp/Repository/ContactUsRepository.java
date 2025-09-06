package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends JpaRepository<Contact,Integer> {

    Contact findContactUsById(Integer id);
}
