package com.clover.habbittracker.domain.bookmark.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bookmark")
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE bookmark set deleted = true where id=?")
public class Bookmark extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String title;

	private String description;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@ManyToMany
	@JoinTable(name = "bookmark_folder")
	private final Set<Post> posts = new HashSet<>();

	public Bookmark(Member member, String title, String description) {
		this.member = member;
		this.title = title;
		this.description = description;
	}

	public static Bookmark defaultBookmark(Member member) {
		return new Bookmark(member, "기본", "기본값으로 제공되는 북마크입니다.");
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public void removePostBy(Long postId) {
		this.posts.removeIf(p -> p.getId().equals(postId));
	}

}
