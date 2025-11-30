package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = """
            SELECT u.* 
            FROM users u 
            WHERE u.phoneNumber = :phoneNumber AND u.password = :password
            """, nativeQuery = true)
    Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);

    @Query(value = """
            SELECT u.* FROM users u
            """, nativeQuery = true)
    Optional<User> findByPhoneNumber(String phoneNumber);

}
