package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Meeting;
import com.example.finalprojectjavabootcamp.Model.Negotiation;
import com.example.finalprojectjavabootcamp.Repository.MeetingRepository;
import com.example.finalprojectjavabootcamp.Repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final NegotiationRepository negotiationRepository;

    public Meeting create(Integer negotiationId, Meeting body) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");
        if (body == null) throw new ApiException("Request body is required");
        if (body.getNegotiation() != null) {
            throw new ApiException("Do not include negotiation object in the request body.");
        }
        if (body.getScheduledAt() == null) {
            throw new ApiException("scheduledAt is required");
        }
        if (body.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new ApiException("scheduledAt must be in the future");
        }

        Negotiation negotiation = negotiationRepository.findNegotiationById(negotiationId);
        if (negotiation == null) throw new ApiException("Negotiation not found: " + negotiationId);

        Meeting m = new Meeting();
        m.setNegotiation(negotiation);
        m.setScheduledAt(body.getScheduledAt());
//        m.setLink(body.getLink());

        return meetingRepository.save(m);
    }

    public Meeting update(Integer id, Meeting body) {
        if (id == null) throw new ApiException("id is required for update");
        if (body == null) throw new ApiException("Request body is required");

        Meeting existing = meetingRepository.findMeetingById(id);
        if (existing == null) throw new ApiException("Meeting not found: " + id);

        if (body.getNegotiation() != null) {
            throw new ApiException("Cannot change negotiation");
        }

        if (body.getScheduledAt() != null) {
            if (body.getScheduledAt().isBefore(LocalDateTime.now())) {
                throw new ApiException("scheduledAt must be in the future");
            }
            existing.setScheduledAt(body.getScheduledAt());
        }

//        if (body.getLink() != null) {
//            existing.setLink(body.getLink());
//        }

        return meetingRepository.save(existing);
    }

    public Meeting findById(Integer id) {
        Meeting m = meetingRepository.findMeetingById(id);
        if (m == null) throw new ApiException("Meeting not found: " + id);
        return m;
    }

    public List<Meeting> listAsc(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        return meetingRepository.findAllByNegotiation_IdOrderByScheduledAtAsc(negotiationId);
    }

    public List<Meeting> listDesc(Integer negotiationId) {
        if (negotiationId == null) throw new ApiException("negotiationId is required");

        Negotiation n = negotiationRepository.findNegotiationById(negotiationId);
        if (n == null) throw new ApiException("Negotiation not found: " + negotiationId);

        return meetingRepository.findAllByNegotiation_IdOrderByScheduledAtDesc(negotiationId);
    }

    public void delete(Integer id) {
        if (id == null) throw new ApiException("id is required for delete");
        Meeting m = meetingRepository.findMeetingById(id);
        if (m == null) throw new ApiException("Meeting not found: " + id);
        meetingRepository.delete(m);
    }
}
