package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
