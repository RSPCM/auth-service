package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID>{
     @Query(value = """
            SELECT s.* 
            FROM students s
            INNER JOIN users u ON s.user_id ON u.id
            WHERE u.phone_number = :phone
            """, nativeQuery = true)
    Optional<Student> findByPhoneNumber(String phone);
}
