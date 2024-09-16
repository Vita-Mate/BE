package com.example.vitamate.repository;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.MemberSupplement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    Page<Supplement> findByNameContaining(String name, PageRequest pageRequest);

    Page<Supplement> findAllByIdIn(Set<Long> idSet, Pageable pageable);
}

