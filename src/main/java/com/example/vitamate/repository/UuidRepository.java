package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.Uuid;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
