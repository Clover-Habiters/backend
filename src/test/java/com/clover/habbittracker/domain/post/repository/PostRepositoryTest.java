package com.clover.habbittracker.domain.post.repository;

import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;

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

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.config.db.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

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
	@DisplayName("게시글을 페이징 처리하여 조회 할 수 있다.")
	void findAllPostsSummary() {
		//given
		for (int i = 0; i < 50; i++) {
			Post testPost = createTestPost(testMember);
			postRepository.save(testPost);
		}
		//when
		List<Post> postsSummary = postRepository.findAllPostsSummary(pageable, null);

		//then
		assertThat(postsSummary.size()).isEqualTo(15);
	}

	@Test
	@DisplayName("게시글 Id 로 게시글의 등록자, 댓글을 모두 포함 하여 조회 할 수 있다.")
	void joinCommentAndLikeFindById() {
		//given
		Post testPost = createTestPost(testMember);
		Post savePost = postRepository.save(testPost);

		//when
		Optional<Post> findPost = postRepository.joinMemberAndCommentFindById(savePost.getId());

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
		List<Post> dailyPostsSummary = postRepository.findAllPostsSummary(pageable, Post.Category.DAILY);

		//then
		assertThat(dailyPostsSummary.size()).isEqualTo(1);
		assertThat(dailyPostsSummary.get(0)).usingRecursiveComparison().isEqualTo(savePostDaily);
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
		assertThat(savedPost.getViews()).isEqualTo(1);
	}

	@Test
	void searchByPostTest() {
		//given
		Post build = Post.builder()
			.title("title")
			.content("content")
			.category(Post.Category.DAILY)
			.member(testMember)
			.build();
		Post save = postRepository.save(build);
		PostSearchCondition searchCondition = new PostSearchCondition(save.getCategory(),
			PostSearchCondition.SearchType.CONTENT, save.getContent());
		//when
		Page<Post> posts = postRepository.searchPostBy(searchCondition, pageable);

		//then
		assertThat(posts.getTotalElements()).isEqualTo(1);
	}
}
