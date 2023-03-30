package com.clover.habbittracker.domain.diray.entity;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "diary")
public class Diary extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	private String content;

	private LocalDateTime endUpdateDate;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;


}
