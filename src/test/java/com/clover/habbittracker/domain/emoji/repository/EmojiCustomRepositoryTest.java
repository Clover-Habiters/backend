package com.clover.habbittracker.domain.emoji.repository;

import static com.clover.habbittracker.domain.emoji.entity.Emoji.*;
import static com.clover.habbittracker.util.CommentProvider.*;
import static com.clover.habbittracker.util.EmojiProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.clover.habbittracker.base.RepositoryTest;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

@DisplayName("Emoji Query 테스트")
class EmojiCustomRepositoryTest extends RepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private EmojiRepository emojiRepository;

	private Post savedPost;

	private Comment savedComment;

	@BeforeEach
	void setUpDomain() {
		// Post
		savedPost = postRepository.save(createTestPost(savedMember));
		emojiRepository.save(createTestEmojiInPost(savedMember, savedPost));

		// Comment
		savedComment = commentRepository.save(createTestComment(savedMember, savedPost));
		emojiRepository.save(createTestEmojiInComment(savedMember, savedComment));
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 도메인과 도메인아이디로 할당된 이모지 전체 조회")
	void findAllInDomain(Domain domain) {
		// when
		List<Emoji> emojiList = emojiRepository.findAllInDomain(domain, getDomainId(domain));

		// then
		assertThat(emojiList).isNotEmpty().hasSize(1);
		assertThat(emojiList.get(0).getDomain()).isEqualTo(domain);
		assertThat(emojiList.get(0).getDomainId()).isEqualTo(getDomainId(domain));
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 유니크 식별값으로 조회")
	void findByUniqueKey(Domain domain) {
		// when
		Optional<Emoji> emoji =
			emojiRepository.findByUniqueKey(savedMember.getId(), domain, getDomainId(domain));

		// then
		assertThat(emoji).isPresent();
		assertThat(emoji.get().getDomain()).isEqualTo(domain);
		assertThat(emoji.get().getDomainId()).isEqualTo(getDomainId(domain));
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 도메인에 할당된 이모지 개수 조회")
	void countByDomain(Domain domain) {
		// when
		int count = emojiRepository.countByDomain(domain, getDomainId(domain));

		// then
		assertThat(count).isEqualTo(1);
	}

	private Long getDomainId(Domain domain) {
		if (domain == Domain.POST) {
			return savedPost.getId();
		}
		if (domain == Domain.COMMENT) {
			return savedComment.getId();
		}
		return null;
	}
}