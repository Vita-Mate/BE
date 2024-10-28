package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.RecordImage;

public interface RecordImageRepository extends JpaRepository<RecordImage, Long> {
}
