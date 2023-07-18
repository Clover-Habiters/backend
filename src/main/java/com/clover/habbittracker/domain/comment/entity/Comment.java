package com.clover.habbittracker.domain.comment.entity;

import static com.clover.habbittracker.global.util.ValidateUtil.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.base.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE comment set deleted = true where id=?")
public class Comment extends BaseEntity {

	@OneToMany
	@JoinColumn(name = "domainId")
	private final List<Emoji> emojis = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;
	@ManyToOne
	@JoinColumn(name = "postId")
	private Post post;
	private Long parentId;

	@Builder
	public Comment(String content, Member member, Post post, Long parentId) {
		this.content = content;
		this.member = member;
		this.post = post;
		this.parentId = parentId;
	}

	public void updateComment(CommentRequest request) {
		this.content = validateContent(request.content());
	}

	private String validateContent(String content) {
		checkText(content, "내용이 비어 있을 수 없습니다.(null, 빈 문자열, 공백만 있음)");
		return content;
	}
}