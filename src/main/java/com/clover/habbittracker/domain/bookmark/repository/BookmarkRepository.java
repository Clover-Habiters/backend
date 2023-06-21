package com.clover.habbittracker.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkCustomRepository {
}
