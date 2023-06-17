package com.clover.habbittracker.domain.post.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.like.entity.Like;
import com.clover.habbittracker.domain.like.entity.Type;
import com.clover.habbittracker.domain.member.entity.Member;

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
@Table(name = "post")
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE post set deleted = true where id=?")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String content;

	private Category category;

	private Long views;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@OneToMany(
		mappedBy = "post",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private final List<Comment> comments = new ArrayList<>();

	@OneToMany(
		mappedBy = "post",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private final List<Like> likes = new ArrayList<>();

	@Builder
	public Post(String title, String content, Category category, Member member) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.member = member;
	}
}
