package com.example.authservice.otp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends CrudRepository<Otp, String> {

    Optional<Otp> findByPhoneNumber(String phoneNumber);
}
