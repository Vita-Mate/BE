package com.example.vitamate.repository;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSupplementRepository extends JpaRepository<MemberSupplement, Long> {

    Page<MemberSupplement> findAllByMemberAndIsTakingTrue(Member member, PageRequest pageRequest);

    Optional<MemberSupplement> findByMemberAndSupplement(Member member, Supplement supplement);
}
