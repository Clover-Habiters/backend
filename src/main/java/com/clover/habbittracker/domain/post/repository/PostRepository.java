package com.clover.habbittracker.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.post.entity.Post;

public interface PostRepository extends PostCustomRepository, JpaRepository<Post, Long> {
}
