package com.clover.habbittracker.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}