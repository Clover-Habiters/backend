package com.clover.habbittracker.domain.post.repository;

import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.config.db.JpaConfig;
import com.clover.habbittracker.util.CustomTransaction;
import com.clover.habbittracker.util.EmojiProvider;
import com.clover.habbittracker.util.PostProvider;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private EmojiRepository emojiRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private Member testMember;

	private Pageable pageable;

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(createTestMember());
		pageable = PageRequest.of(0, 15);
	}

	@Test
	@DisplayName("사용자는 게시글을 등록할 수 있다.")
	void savePostTest() {
		//given
		Post testPost = createTestPost(testMember);
		//when
		Post savePost = postRepository.save(testPost);
		//then
		assertThat(savePost).usingRecursiveComparison().isEqualTo(testPost);
	}

	@Test
	@DisplayName("게시글 리스트를 조회 할 경우 댓글 및 이모지의 총 개수와 게시글의 요약된 정보를 조회 할 수 있다.")
	void findAllPostsSummaryTest() {
		//given
		Post testPost = PostProvider.createTestPost(testMember);
		Post savedPost = postRepository.save(testPost);
		Emoji testEmojiInPost = EmojiProvider.createTestEmojiInPost(testMember, savedPost);
		emojiRepository.save(testEmojiInPost);
		Comment comment = Comment.builder().post(savedPost).member(testMember).content("댓글").build();
		commentRepository.save(comment);

		//when
		List<PostResponse> allPostsSummary = postRepository.findAllPostsSummary(pageable, null);

		//then
		PostResponse postResponse = allPostsSummary.get(0);
		assertThat(postResponse.numOfComments()).isEqualTo(1);
		assertThat(postResponse.numOfEmojis()).isEqualTo(1);
		assertAll(() -> {
			assertThat(postResponse.id()).isEqualTo(savedPost.getId());
			assertThat(postResponse.title()).isEqualTo(savedPost.getTitle());
			assertThat(postResponse.content()).isEqualTo(savedPost.getContent());
			assertThat(postResponse.category()).isEqualTo(savedPost.getCategory());
			assertThat(postResponse.thumbnailUrl()).isEqualTo(savedPost.getThumbnailUrl());
			assertThat(postResponse.views()).isEqualTo(0);
		});
	}

	@Test
	@DisplayName("게시글 리스트를 페이징 처리하여 조회 할 수 있다.")
	void pagingPostsSummaryTest() {
		//given
		for (int i = 0; i < 15; i++) {
			Post testPost = createTestPost(testMember);
			postRepository.save(testPost);
		}
		//when
		List<PostResponse> page01 = postRepository.findAllPostsSummary(pageable, null);
		List<PostResponse> page02 = postRepository.findAllPostsSummary(PageRequest.of(1, 15), null);
		assertThat(page01.size()).isEqualTo(15);
		assertThat(page02.size()).isEqualTo(0);

	}

	@Test
	@DisplayName("게시글 Id 로 게시글을 상세 조회 할 수 있다.")
	void joinCommentAndLikeFindById() {
		//given
		Post testPost = createTestPost(testMember);
		Post savePost = postRepository.save(testPost);

		//when
		Optional<Post> findPost = postRepository.joinMemberAndEmojisFindById(savePost.getId());

		//then
		assertThat(findPost).isPresent();
		assertThat(findPost.get()).usingRecursiveComparison().isEqualTo(savePost);
	}

	@Test
	@DisplayName("게시글의 카테고리 별 조회를 할 수 있다.")
	void postCategoryFilterTest() {
		//given
		Post postDaily = createTestPost(testMember, Post.Category.DAILY);
		Post postETC = createTestPost(testMember, Post.Category.ETC);
		Post savePostDaily = postRepository.save(postDaily);
		Post savePostETC = postRepository.save(postETC);

		//when
		List<PostResponse> dailyPostsSummary = postRepository.findAllPostsSummary(pageable, Post.Category.DAILY);

		//then
		assertThat(dailyPostsSummary.size()).isEqualTo(1);
		assertThat(dailyPostsSummary.get(0).category()).isEqualTo(savePostDaily.getCategory());
	}

	@Disabled // TODO: 조회수 개선 이후 테스트 진행. 현재 정상적으로 테스트가 불가능.
	@Test
	@DisplayName("게시글의 조회수는 1씩 증가한다.")
	void updateViewTest() throws InterruptedException {
		//given
		int threadNum = 3;
		Post testPost = createTestPost(testMember);
		Post savedPost = postRepository.save(testPost);
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		CountDownLatch latch = new CountDownLatch(threadNum);

		//when
		for (int i = 0; i < threadNum; i++) {
			executorService.execute(() -> {
				postRepository.updateViews(savedPost.getId());
				latch.countDown();
			});
		}
		latch.await();

		//then
		assertThat(savedPost.getViews()).isEqualTo(threadNum);
	}

	@Test
	@DisplayName("[성공] 게시글 본문 조회 테스트")
	@Sql(scripts = "/searchTest_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void searchByPostTest() {
		//given
		TransactionDefinition definition = new CustomTransaction();
		TransactionStatus status = transactionManager.getTransaction(definition);
		Post post = createTestPost(testMember);
		Post save = postRepository.save(post);
		transactionManager.commit(status);
		PostSearchCondition searchCondition = new PostSearchCondition(save.getCategory(),
			PostSearchCondition.SearchType.CONTENT, save.getContent());
		//when
		Page<PostResponse> posts = postRepository.searchPostBy(searchCondition, pageable);

		//then
		assertThat(posts.getContent().size()).isEqualTo(1);
	}
}
