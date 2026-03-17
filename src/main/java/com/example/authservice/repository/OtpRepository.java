package com.example.authservice.repository;

import com.example.authservice.entity.Otp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends CrudRepository<Otp, String> {

    Optional<Otp> findByPhoneNumber(String phoneNumber);
}
