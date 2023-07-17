package com.clover.habbittracker.domain.emoji.entity;

import static lombok.AccessLevel.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.clover.habbittracker.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "emoji")
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE emoji set deleted = true where id=?")
public class Emoji extends BaseEntity {

	public enum Type {
		SMILE, ANGRY, SAD, SURPRISED, NONE
	}

	public enum Domain {
		COMMENT, POST;

		public boolean isPost() {
			return this == POST;
		}

		public boolean isComment() {
			return this == COMMENT;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Domain domain;

	@Enumerated(EnumType.STRING)
	private Type type;

	private Long memberId;

	private Long domainId;

	@Builder
	public Emoji(Domain domain, Type type, Long memberId, Long domainId) {
		this.domain = domain;
		this.type = type;
		this.memberId = memberId;
		this.domainId = domainId;
	}

	public void updateStatus(Type type) {
		this.type = type;
	}

	public boolean isSameType(Type type) {
		return this.type == type;
	}

}
