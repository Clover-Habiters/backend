package com.clover.habbittracker.domain.bookmark.entity;

import static com.clover.habbittracker.global.util.ValidateUtil.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.base.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bookmark")
@NoArgsConstructor(access = PROTECTED)
public class Bookmark extends BaseEntity {

	private static final int TITLE_MAX_SIZE = 30;
	private static final int DESCRIPTION_MAX_SIZE = 500;

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

	@Builder
	public Bookmark(Member member, String title, String description) {
		this.member = Objects.requireNonNull(member, "북마크에 유저 정보는 필수입니다.");
		this.title = validateTitle(title);
		this.description = validateDescription(description);
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

	private String validateTitle(String title) {
		checkOverLength(title, TITLE_MAX_SIZE, TITLE_MAX_SIZE + "를 초과하였습니다.");
		checkText(title, "제목은 비어 있을 수 없습니다.(null, 빈 문자열, 공백만 있음)");
		return title;
	}

	private String validateDescription(String description) {
		checkText(description, "제목은 비어 있을 수 없습니다.(null, 빈 문자열, 공백만 있음)");
		checkOverLength(description, DESCRIPTION_MAX_SIZE, DESCRIPTION_MAX_SIZE + "를 초과하였습니다.");
		return description;
	}

}
