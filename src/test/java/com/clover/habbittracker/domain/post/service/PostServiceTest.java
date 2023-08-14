package com.clover.habbittracker.domain.post.service;

import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.util.CustomTransaction;

@SpringBootTest
@Transactional
class PostServiceTest {

	private final Pageable pageable = PageRequest.of(0, 15);
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostService postService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	private Member testMember;

	@BeforeEach
	public void setUp() {
		Member member = createTestMember();
		testMember = memberRepository.save(member);
	}

	@Test
	@DisplayName("사용자의 아이디와 게시글 등록 요청을 받아 게시글을 등록 할 수 있다.")
	void registerTest() {
		//given
		PostRequest request = createPostRequest();
		//when
		Long postId = postService.register(testMember.getId(), request);
		//then
		Optional<Post> savedPost = postRepository.findById(postId);
		assertAll(() -> {
			assertThat(savedPost).isPresent();
			assertThat(savedPost.get().getTitle()).isEqualTo(request.title());
			assertThat(savedPost.get().getContent()).isEqualTo(request.content());
			assertThat(savedPost.get().getCategory()).isEqualTo(request.category());
		});
	}

	@Test
	@DisplayName("사용자의 아이디와 게시글 수정 요청을 받아 게시글을 수정 할 수 있다.")
	void updatePostTest() {
		//given
		Post post = postRepository.save(createTestPost(testMember));
		PostRequest updateRequest = createPostRequest();
		//when
		Long postId = postService.updatePost(post.getId(), updateRequest, testMember.getId());
		//then
		Optional<Post> updatedPost = postRepository.findById(postId);
		assertAll(() -> {
			assertThat(updatedPost).isPresent();
			assertThat(updatedPost.get().getTitle()).isEqualTo(updateRequest.title());
			assertThat(updatedPost.get().getContent()).isEqualTo(updateRequest.content());
			assertThat(updatedPost.get().getThumbnail()).isEqualTo(updateRequest.thumbnail());
			assertThat(updatedPost.get().getCategory()).isEqualTo(updateRequest.category());
		});
	}

	@Test
	@DisplayName("게시글 아이디로 게시글의 상세내용을 조회 할 수 있다.")
	void getPostTest() {
		//given
		Post post = postRepository.save(createTestPost(testMember));
		//when
		PostDetailResponse postDetail = postService.getPostBy(post.getId());
		//then
		assertAll(() -> {
			assertThat(post.getTitle()).isEqualTo(postDetail.title());
			assertThat(post.getContent()).isEqualTo(postDetail.content());
			assertThat(post.getCategory()).isEqualTo(postDetail.category());
			assertThat(post.getComments()).isNotNull();
			assertThat(post.getEmojis()).isNotNull();
		});
	}

	@Test
	@DisplayName("게시글 아이디로 게시글을 삭제 할 수 있다.")
	void deletePostTest() {
		//given
		Post post = postRepository.save(createTestPost(testMember));
		//when
		postService.deletePost(post.getId(), testMember.getId());
		//then
		Optional<Post> deletedPost = postRepository.findById(post.getId());
		assertThat(deletedPost).isEmpty();
	}

	@Nested
	@Sql(scripts = "/searchTest_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("검색은")
	class searchBy {

		private Post savedPost;
		private final TransactionDefinition transactionDefinition = new CustomTransaction();

		@BeforeEach
		void setUp() {
			TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
			Post post = createTestPost(testMember);
			savedPost = postRepository.save(post);
			transactionManager.commit(transactionStatus);
		}

		@Test
		@DisplayName("카테고리로 할수있다")
		void categoryTest() {
			//when
			List<PostResponse> categoryFilterPost = postService.getPostAllBy(savedPost.getCategory(), pageable);
			//then
			assertAll(() -> {
				assertThat(categoryFilterPost.size()).isEqualTo(1);
				assertThat(categoryFilterPost.get(0).title()).isEqualTo(savedPost.getTitle());
				assertThat(categoryFilterPost.get(0).content()).isEqualTo(savedPost.getContent());
				assertThat(categoryFilterPost.get(0).thumbnail()).isEqualTo(savedPost.getThumbnail());
				assertThat(categoryFilterPost.get(0).category()).isEqualTo(savedPost.getCategory());
			});

		}

		@Test
		@DisplayName("제목으로 할수있다")
		void titleTest() {
			//given
			PostSearchCondition condition = new PostSearchCondition(
				Post.Category.ALL,
				PostSearchCondition.SearchType.TITLE,
				savedPost.getTitle());

			//when
			Page<PostResponse> titleFilterPost = postService.getPostBy(condition, pageable);
			List<PostResponse> postList = titleFilterPost.getContent();
			//then
			assertAll(() -> {
				assertThat(postList.size()).isEqualTo(1);
				assertThat(postList.get(0).title()).isEqualTo(savedPost.getTitle());
				assertThat(postList.get(0).content()).isEqualTo(savedPost.getContent());
				assertThat(postList.get(0).thumbnail()).isEqualTo(savedPost.getThumbnail());
				assertThat(postList.get(0).category()).isEqualTo(savedPost.getCategory());
			});
		}

		@Test
		@DisplayName("본문으로 할수있다")
		void contentTest() {
			//given
			PostSearchCondition condition = new PostSearchCondition(
				Post.Category.ALL,
				PostSearchCondition.SearchType.CONTENT,
				savedPost.getContent());

			//when
			Page<PostResponse> titleFilterPost = postService.getPostBy(condition, pageable);
			List<PostResponse> postList = titleFilterPost.getContent();
			//then
			assertAll(() -> {
				assertThat(postList.size()).isEqualTo(1);
				assertThat(postList.get(0).title()).isEqualTo(savedPost.getTitle());
				assertThat(postList.get(0).content()).isEqualTo(savedPost.getContent());
				assertThat(postList.get(0).thumbnail()).isEqualTo(savedPost.getThumbnail());
				assertThat(postList.get(0).category()).isEqualTo(savedPost.getCategory());
			});
		}
	}
}
