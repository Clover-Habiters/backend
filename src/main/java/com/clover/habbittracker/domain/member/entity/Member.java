package com.clover.habbittracker.domain.member.entity;

import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import com.clover.habbittracker.domain.diray.entity.Diary;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "member")
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String email;

	private String profileImgUrl;

	private String nickName;

	private String provider;

	private String oauthId;

	@OneToMany(
		mappedBy = "member",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private List<Diary> diaries = new ArrayList<>();

	@OneToMany(
		mappedBy = "member",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private List<Habit> habits = new ArrayList<>();

	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
