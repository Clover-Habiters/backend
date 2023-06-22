package com.clover.habbittracker.domain.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query(value = """
			SELECT p
			FROM Post p
			LEFT JOIN FETCH p.comments pc
			LEFT JOIN FETCH p.likes pl
		""",
		countQuery = "SELECT count(p) FROM Post p")
	Page<Post> findAllPostsSummary(Pageable pageable);  //TODO: pagination 과 Fetch join 을 같이 사용하면 안됨.

	@Query("""
	SELECT p
	FROM Post p
	LEFT JOIN FETCH p.comments pc
	LEFT JOIN FETCH p.likes pl
	WHERE p.id = :postId
""")
	Optional<Post> joinCommentAndLikeFindById(@Param("postId") Long postId);

	@Modifying
	@Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :postId")
	int updateViews(Long postId);

}
