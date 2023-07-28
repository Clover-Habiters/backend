package com.clover.habbittracker.domain.emoji.entity;

import static lombok.AccessLevel.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.core.convert.converter.Converter;

import com.clover.habbittracker.global.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "emoji",
	indexes = {
		@Index(name = "idx_emoji_member_id", columnList = "memberId"),
		@Index(name = "idx_emoji_domain", columnList = "domain,domainId"),
		@Index(name = "idx_emoji_unique", columnList = "memberId, domain,domainId", unique = true)
	}
)
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE emoji set deleted = true where id=?")
public class Emoji extends BaseEntity {

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
	public Emoji(Type type, Long memberId, Domain domain, Long domainId) { // 무엇을 누가 어디에
		this.type = type;
		this.memberId = memberId;
		this.domain = domain;
		this.domainId = domainId;
	}

	public Emoji updateStatus(Type type) {
		this.type = type;
		return this;
	}

	public enum Type {
		SMILE, ANGRY, SAD, SURPRISED, HEART, NONE;

		@JsonCreator
		public static Type fromJson(@JsonProperty("type") String name) {
			return valueOf(name.toUpperCase());
		}

		public boolean isSame(Type type) {
			return this == type;
		}

		public static class StringToEmojiTypeConverter
			implements Converter<String, Type> {

			@Override
			public Type convert(String source) {
				return Type.valueOf(source.toUpperCase());
			}
		}
	}

	public enum Domain {
		COMMENT, POST;

		@JsonCreator
		public static Domain fromJson(@JsonProperty("domain") String name) {
			return valueOf(name.toUpperCase());
		}

		public boolean isSame(Domain domain) {
			return this == domain;
		}

		public static class StringToEmojiDomainConverter
			implements Converter<String, Domain> {

			@Override
			public Domain convert(String source) {
				return Domain.valueOf(source.toUpperCase());
			}
		}
	}

}
