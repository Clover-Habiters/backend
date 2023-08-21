package com.clover.habbittracker.domain.post.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.global.base.entity.BaseEntity;
import com.clover.habbittracker.global.base.entity.RestDocsEnum;

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
public class Post extends BaseEntity {

	@OneToMany(
		mappedBy = "post",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	@BatchSize(size = 100)
	private final List<Comment> comments = new ArrayList<>();
	@OneToMany
	@JoinColumn(name = "domainId")
	private final List<Emoji> emojis = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	private Category category;
	private Long views;
	private String thumbnailUrl;
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@Builder
	public Post(String title, String content, Category category, String thumbnailUrl, Member member) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.thumbnailUrl = thumbnailUrl;
		this.member = member;
		this.views = 0L;
	}

	public void updatePost(PostRequest postRequest) {
		this.title = postRequest.title();
		this.content = postRequest.content();
		this.thumbnailUrl = postRequest.thumbnailUrl();
		this.category = postRequest.category();
	}

	public enum Category implements RestDocsEnum {
		ALL("전체"),
		NOTICE("공지 사항"),
		STUDY("공부"),
		EXERCISE("운동"),
		HEALTH("건강"),
		DAILY("일상"),
		ETC("기타");

		private final String description;

		Category(String description) {
			this.description = description;
		}

		@Override
		public String getName() {
			return this.name();
		}

		@Override
		public String getDescription() {
			return description;
		}
	}
}