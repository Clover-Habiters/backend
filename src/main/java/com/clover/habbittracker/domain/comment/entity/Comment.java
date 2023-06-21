package com.clover.habbittracker.domain.comment.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Comment {

	@OneToMany(
		mappedBy = "comment",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private final List<Emoji> likes = new ArrayList<>();
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
	private Long commentId;

	@Builder
	public Comment(String content, Member member, Post post, Long commentId) {
		this.content = content;
		this.member = member;
		this.post = post;
		this.commentId = commentId;
	}

}