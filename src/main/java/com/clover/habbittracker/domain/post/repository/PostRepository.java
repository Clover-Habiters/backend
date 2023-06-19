package com.clover.habbittracker.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clover.habbittracker.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("""
			SELECT p.title, p.content, p.category, p.views, p.createdAt, p.likes, p.comments
			FROM Post p
		""")
	Page<Post> findAllPostsSummary(Pageable pageable);
}
