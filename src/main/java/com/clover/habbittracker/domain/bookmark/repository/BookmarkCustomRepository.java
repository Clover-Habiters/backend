package com.clover.habbittracker.domain.bookmark.repository;

import java.util.List;
import java.util.Optional;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;

public interface BookmarkCustomRepository {

	List<Bookmark> findByMemberId(Long memberId);

	Optional<Bookmark> findByIdAndMemberId(Long bookmarkId, Long memberId);

	void deleteByIdAndMemberId(Long bookmarkId, Long memberId);
}
