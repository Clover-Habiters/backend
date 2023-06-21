package com.clover.habbittracker.domain.emoji.entity;

import static lombok.AccessLevel.*;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "emoji")
@NoArgsConstructor(access = PROTECTED)
public class Emoji {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Type type;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "postId")
	private Post post;

	@ManyToOne
	@JoinColumn(name = "commentId")
	private Comment comment;

	@Builder
	public Emoji(Type type, Member member, Post post, Comment comment) {
		this.type = type;
		this.member = member;
		this.post = post;
		this.comment = comment;
	}

	public void updateStatus(Type type) {
		this.type = isSameType(type) ? Type.NONE : type;
	}

	public boolean isSameType(Type type) {
		return this.type == type;
	}

}
