package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    @Query(value = """
            SELECT t.* 
            FROM teachers t
            INNER JOIN users u ON t.user_id = u.id
            WHERE u.phone_number = :phone
            """, nativeQuery = true)
    Optional<Teacher> findByPhoneNumber(String phone);

}
