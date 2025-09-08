package com.example.finalprojectjavabootcamp.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, OtpEntry> otpStorage = new HashMap<>();
    private final Random random = new Random();

    private static class OtpEntry {
        String otp;
        LocalDateTime expiryTime;

        OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }


    //Generate OTP
    public String generateOtp(String phoneNumber) {
        String otp = String.format("%05d", random.nextInt(100000));
        otpStorage.put(phoneNumber, new OtpEntry(otp, LocalDateTime.now().plusMinutes(1)));
        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        OtpEntry entry = otpStorage.get(phoneNumber);
        if (entry == null) return false;
        if (LocalDateTime.now().isAfter(entry.expiryTime)) {
            otpStorage.remove(phoneNumber);
            return false;
        }
        boolean valid = entry.otp.equals(otp);
        if (valid) otpStorage.remove(phoneNumber);
        return valid;
    }

}
