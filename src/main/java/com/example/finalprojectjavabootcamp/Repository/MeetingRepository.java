package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Integer> {
    Meeting findMeetingById(Integer id);

    List<Meeting> findAllByNegotiation_IdOrderByScheduledAtAsc(Integer negotiationId);

    List<Meeting> findAllByNegotiation_IdOrderByScheduledAtDesc(Integer negotiationId);
}
