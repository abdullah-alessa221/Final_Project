package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Result;
import com.example.finalprojectjavabootcamp.Model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ResultRepository extends JpaRepository<Result, Integer> {

    List<Result> findResultsBySearch(Search search);
}
