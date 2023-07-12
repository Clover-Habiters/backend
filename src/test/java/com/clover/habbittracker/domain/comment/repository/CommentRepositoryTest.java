package com.clover.habbittracker.domain.comment.repository;


import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.config.JpaConfig;
import com.clover.habbittracker.global.util.MemberProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    private Post testPost;

    @BeforeEach
    void setUp() {
        Member member = MemberProvider.createTestMember();
        testMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("testTitle")
                .content("testContent")
                .category(Category.DAILY)
                .member(testMember)
                .build();

        testPost = postRepository.save(post);
    }

    @Test
    @DisplayName("댓글을 등록 할 수 있다.")
    void saveCommentTest() {
        //given
        Comment testComment = Comment.builder()
                .content("testComment")
                .post(testPost)
                .member(testMember)
                .build();
        //when
        Comment saveComment = commentRepository.save(testComment);
        //then
        assertThat(testComment).usingRecursiveComparison().isEqualTo(saveComment);
    }

    @Test
    @DisplayName("댓글 아이디와 게시글 아이디로 댓글을 조회 할 수 있다.")
    void findByIdAndPostIdTest() {
        //given
        Comment testComment = Comment.builder()
                .content("testComment")
                .post(testPost)
                .member(testMember)
                .build();
        Comment saveComment = commentRepository.save(testComment);

        //when
        Optional<Comment> findComment = commentRepository.findByIdAndPostId(saveComment.getId(), testPost.getId());

        //then
        assertThat(findComment).isPresent();
        assertThat(findComment.get()).usingRecursiveComparison().isEqualTo(saveComment);
    }

    @Test
    @DisplayName("댓글의 부모 아이디로 대댓글 리스트를 조회 할 수 있다.")
    void findByParentIdTest() {
        //given
        Comment testComment = Comment.builder()
                .content("testComment")
                .post(testPost)
                .member(testMember)
                .build();
        Comment saveComment = commentRepository.save(testComment);
        Comment reply = Comment.builder()
                .content("testReply")
                .member(testMember)
                .post(testPost)
                .parentId(saveComment.getId())
                .build();
        //when
        List<Comment> childCommentById = commentRepository.findChildCommentById(saveComment.getId());

        //then
        childCommentById
                .forEach(childComment -> {
                    assertThat(childComment.getParentId()).isEqualTo(saveComment.getId());
                    assertThat(childComment.getContent()).isEqualTo(reply.getContent());
                });
    }
}
