package com.example.vitamate.repository;

import java.util.List;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.mapping.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllBySupplement(Supplement supplement, PageRequest pageRequest);

    List<Review> findAllByMember(Member member);
}
