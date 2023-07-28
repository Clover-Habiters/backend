package com.clover.habbittracker.domain.emoji.service;

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

import com.clover.habbittracker.base.ServiceTest;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.emoji.dto.EmojiResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

@DisplayName("Emoji Service 테스트")
class EmojiServiceTest extends ServiceTest {

	@Autowired
	private EmojiService emojiService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private EmojiRepository emojiRepository;

	private Post savedPost;

	private Comment savedComment;

	@BeforeEach
	@DisplayName("Domain Setting")
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
	void getAllEmojisInDomain(Domain domain) {
		// when
		List<EmojiResponse> allEmojis = emojiService.getAllEmojisInDomain(domain, getDomainId(domain));

		// then
		assertThat(allEmojis).hasSize(1);
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 이모지 저장(생성)")
	void saveForInsert(Domain domain) {
		// given
		Type type = Type.SAD;

		// when
		EmojiResponse savedEmoji = emojiService.save(type, savedMember.getId(), domain, getDomainId(domain));

		// then
		assertThat(savedEmoji)
			.hasFieldOrPropertyWithValue("emojiType", type.name())
			.hasFieldOrPropertyWithValue("memberId", savedMember.getId())
			.hasFieldOrPropertyWithValue("domain", domain.name())
			.hasFieldOrPropertyWithValue("domainId", getDomainId(domain));
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 이모지 저장(수정)")
	void saveForUpdate(Domain domain) {
		// given
		Type oldType = emojiRepository
			.findByUniqueKey(savedMember.getId(), domain, getDomainId(domain))
			.get().getType();

		Type originType = Type.SAD; // 기존의 저장된 것과 다른 타입

		// when
		EmojiResponse newEmoji = emojiService.save(originType, savedMember.getId(), domain, getDomainId(domain));

		// then
		Type newType = Type.valueOf(newEmoji.emojiType());

		assertThat(newType).isNotEqualTo(oldType);
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[성공] 이모지 취소")
	void delete(Domain domain) {
		// when
		emojiService.delete(savedMember.getId(), domain, getDomainId(domain));

		// then
		Optional<Emoji> findEmoji
			= emojiRepository.findByUniqueKey(savedMember.getId(), domain, getDomainId(domain));

		assertThat(findEmoji).isEmpty();
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