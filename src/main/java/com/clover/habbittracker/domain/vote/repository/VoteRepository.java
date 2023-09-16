package com.clover.habbittracker.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.bookmark.repository.BookmarkCustomRepository;
import com.clover.habbittracker.domain.vote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>, BookmarkCustomRepository {
}
