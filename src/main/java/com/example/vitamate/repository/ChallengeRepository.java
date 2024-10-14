package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

}
