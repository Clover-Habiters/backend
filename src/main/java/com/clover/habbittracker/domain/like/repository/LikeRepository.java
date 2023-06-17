package com.clover.habbittracker.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.like.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
