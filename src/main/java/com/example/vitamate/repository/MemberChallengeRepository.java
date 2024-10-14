package com.example.vitamate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vitamate.domain.mapping.MemberChallenge;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
}
