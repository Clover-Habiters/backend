package com.clover.habbittracker.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.bookmark.entity.BookMark;

public interface BookMarkRepository extends JpaRepository<BookMark,Long> {

}
