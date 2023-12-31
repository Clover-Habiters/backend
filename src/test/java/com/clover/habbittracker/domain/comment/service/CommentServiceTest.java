package com.clover.habbittracker.domain.comment.service;

import static com.clover.habbittracker.util.CommentProvider.*;
import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.exception.PostNotFoundException;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.base.exception.PermissionDeniedException;

@SpringBootTest
@Transactional
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CommentRepository commentRepository;

	private Member testMember;

	private Post testPost;

	private Long memberId;

	private Long postId;

	@BeforeEach
	void setUp() {
		Member member = createTestMember();
		testMember = memberRepository.save(member);
		memberId = testMember.getId();
		testPost = postRepository.save(createTestPost(member));
		postId = testPost.getId();
	}

	@Test
	@DisplayName("회원은 게시글의 댓글을 등록 할 수 있다.")
	void createCommentTest() {
		//given
		CommentRequest request = new CommentRequest("testContent");

		//when
		CommentResponse response = commentService.createComment(memberId, postId, request);

		//then
		Optional<Comment> saveComment = commentRepository.findById(response.id());
		assertThat(saveComment).isPresent();
		assertThat(saveComment.get().getId()).isEqualTo(response.id());
		assertThat(saveComment.get().getContent()).isEqualTo(response.content());
	}

	@Test
	@DisplayName("게시글의 댓글을 조회 할 수 있다")
	void getCommentsTest() {
		//given
		Comment testComment = createTestComment(testMember, testPost);
		Comment savedComment = commentRepository.save(testComment);

		//when
		List<CommentResponse> comments = commentService.getCommentsOf(testPost.getId());

		//then
		CommentResponse response = comments.get(0);
		assertThat(response.id()).isEqualTo(savedComment.getId());
		assertThat(response.content()).isEqualTo(savedComment.getContent());
		assertThat(response.authorId()).isEqualTo(savedComment.getMember().getId());
	}

	@Test
	@DisplayName("게시글의 댓글을 조회 할 때, 대댓글은 조회 하지 않는다.")
	void getCommentsWithOutReplyTest() {
		//given
		Comment testComment = createTestComment(testMember, testPost);
		Comment savedComment = commentRepository.save(testComment);
		Comment testReply = createTestReply(testMember, testPost, savedComment);
		Comment savedReply = commentRepository.save(testReply);

		//when
		List<CommentResponse> comments = commentService.getCommentsOf(testPost.getId());

		//then
		assertThat(comments.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("회원은 게시글의 댓글을 수정 할 수 있다.")
	void updateCommentTest() {
		//given
		Comment testComment = Comment.builder()
			.post(testPost)
			.member(testMember)
			.content("testContent")
			.build();
		Comment saveComment = commentRepository.save(testComment);
		CommentRequest request = new CommentRequest("updateContent");
		Long commentId = saveComment.getId();

		//when
		CommentResponse response = commentService.updateComment(memberId, commentId, postId, request);

		//then
		Optional<Comment> updateComment = commentRepository.findById(response.id());
		assertThat(updateComment).isPresent();
		assertThat(updateComment.get().getId()).isEqualTo(response.id());
		assertThat(updateComment.get().getContent()).isEqualTo(response.content());
	}

	@Test
	@DisplayName("댓글 작성자는 댓글을 삭제 할 수 있다.")
	void deleteCommentTest() {
		//given
		Comment testComment = createTestComment(testMember, testPost);
		Comment savedComment = commentRepository.save(testComment);

		//when
		commentService.deleteComment(testMember.getId(), savedComment.getId(), testPost.getId());

		//then
		Optional<Comment> deletedComment = commentRepository.findById(savedComment.getId());
		assertThat(deletedComment).isEmpty();
	}

	@Test
	@DisplayName("회원은 게시글 댓글의 대댓글을 등록 할 수 있다.")
	void createReplyTest() {
		//given
		Comment comment = Comment.builder()
			.content("have a reply")
			.member(testMember)
			.post(testPost)
			.build();
		Comment saveComment = commentRepository.save(comment);
		CommentRequest request = new CommentRequest("testReply");
		Long commentId = saveComment.getId();

		//when
		commentService.createReply(memberId, commentId, postId, request);

		//then
		List<Comment> replyList = commentRepository.findChildCommentById(commentId);
		replyList.forEach(response -> {
			assertThat(response.getParentId()).isEqualTo(commentId);
			assertThat(response.getContent()).isEqualTo(request.content());
		});
	}

	@Test
	@DisplayName("대댓글을 조회 할 수 있다")
	void getReplyListTest() {
		//given
		Comment comment = Comment.builder()
			.content("have a replyList")
			.member(testMember)
			.post(testPost)
			.build();

		Comment saveComment = commentRepository.save(comment);
		Long parentId = saveComment.getId();

		for (int i = 0; i < 10; i++) {
			Comment build = Comment.builder()
				.content("reply" + i)
				.member(testMember)
				.post(testPost)
				.parentId(parentId)
				.build();
			commentRepository.save(build);
		}

		//when
		List<CommentResponse> replyList = commentService.getReplyList(parentId, postId);

		//then
		assertThat(replyList.size()).isEqualTo(10);
	}

	@Test
	@DisplayName("잘못된 ID 로 요청할 경우 예외가 발생한다")
	void failedWrongIdCommentTest() {
		//given
		Long wrongMemberId = -1L;
		Long wrongCommentId = -1L;
		Long wrongPostId = -1L;
		CommentRequest request = new CommentRequest("test");

		//when
		assertAll(
			// 잘못된 MemberId
			() -> assertThrows(MemberNotFoundException.class,
				() -> commentService.createComment(wrongMemberId, postId, request)),
			() -> assertThrows(MemberNotFoundException.class,
				() -> commentService.createReply(wrongMemberId, wrongCommentId, postId, request)),

			// 잘못된 PostId
			() -> assertThrows(PostNotFoundException.class,
				() -> commentService.createComment(memberId, wrongPostId, request)),

			() -> assertThrows(PostNotFoundException.class,
				() -> commentService.createReply(memberId, wrongCommentId, wrongPostId, request)),

			// 잘못된 CommentId
			() -> assertThrows(IllegalArgumentException.class,
				() -> commentService.updateComment(wrongMemberId, wrongCommentId, postId, request))

		);

	}

	@Test
	@DisplayName("작상자가 아닌 회원이 수정,삭제 요청할 경우 예외가 발생한다")
	void failedPermissionCommentTest() {
		//given
		Long notAuthorMemberId = -1L;
		CommentRequest request = new CommentRequest("testContent");
		Comment testComment = createTestComment(testMember, testPost);
		Comment savedComment = commentRepository.save(testComment);

		//when & then
		assertAll(
			() -> assertThrows(PermissionDeniedException.class,
				() -> commentService.deleteComment(notAuthorMemberId, savedComment.getId(), postId)),
			() -> assertThrows(PermissionDeniedException.class,
				() -> commentService.updateComment(notAuthorMemberId, savedComment.getId(), postId, request))

		);

	}

}
