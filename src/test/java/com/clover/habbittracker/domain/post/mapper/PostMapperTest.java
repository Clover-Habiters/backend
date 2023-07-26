package com.clover.habbittracker.domain.post.mapper;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static com.clover.habbittracker.global.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.mapper.CommentMapper;
import com.clover.habbittracker.domain.comment.mapper.CommentMapperImpl;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.entity.Post;

public class PostMapperTest {

	private final PostMapper postMapper = new PostMapperImpl();

	private Member testMember;

	private Post testPost;

	@BeforeEach
	void setUp() {
		testMember = createTestMember();
		testPost = createTestPost(testMember);
	}

	@Test
	@DisplayName("PostRequest 를 Post 로 매핑 시킬 수 있다.")
	void toPostTest() {
		//given
		PostRequest postRequest = createPostRequest();
		//when
		Post post = postMapper.toPost(postRequest, testMember);
		//then
		assertAll(() -> {
			assertThat(post.getTitle()).isEqualTo(postRequest.title());
			assertThat(post.getContent()).isEqualTo(postRequest.content());
			assertThat(post.getCategory()).isEqualTo(postRequest.category());
		});
	}

	@Test
	@DisplayName("Post 를 PostResponse 로 매핑 시킬 수 있다.")
	void toPostResponseTest() {

		//given & when
		PostResponse postResponse = postMapper.toPostResponse(testPost);

		//then
		assertAll(() -> {
			assertThat(testPost.getId()).isEqualTo(postResponse.id());
			assertThat(testPost.getTitle()).isEqualTo(postResponse.title());
			assertThat(testPost.getContent()).isEqualTo(postResponse.content());
			assertThat(testPost.getCategory()).isEqualTo(postResponse.category());
			assertThat(testPost.getViews()).isEqualTo(postResponse.views());
			assertThat(testPost.getEmojis().size()).isEqualTo(postResponse.numOfEmojis());
			assertThat(testPost.getComments().size()).isEqualTo(postResponse.numOfComments());
		});

	}

	@Test
	@DisplayName("Post 를 PostDetailResponse 로 매핑 시킬 수 있다.")
	void toPostDetailResponseTest() {
		//given
		CommentMapper commentMapper = new CommentMapperImpl();
		List<CommentResponse> comments = testPost.getComments()
			.stream()
			.map(commentMapper::toCommentResponse)
			.toList();

		//when
		PostDetailResponse postDetailResponse = postMapper.toPostDetail(testPost);

		//then
		assertAll(() -> {
			assertThat(testPost.getTitle()).isEqualTo(postDetailResponse.title());
			assertThat(testPost.getContent()).isEqualTo(postDetailResponse.content());
			assertThat(testPost.getCategory()).isEqualTo(postDetailResponse.category());
			assertThat(testPost.getViews()).isEqualTo(postDetailResponse.views());
			assertThat(testPost.getEmojis()).isEqualTo(postDetailResponse.emojis());
			assertThat(comments).isEqualTo(postDetailResponse.comments());
		});
	}
}