package com.clover.habbittracker.domain.habit.entity;

import java.util.ArrayList;
import java.util.List;

import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.global.base.entity.BaseEntity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "habbit")
public class Habit extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;

	@OneToMany(
		mappedBy = "habit",
		fetch = FetchType.LAZY,
		cascade = CascadeType.REMOVE,
		orphanRemoval = true)
	private final List<HabitCheck> habitChecks = new ArrayList<>();

	public void setContent(String content) {
		this.content = content;
	}
}
