package com.clover.habbittracker.domain.member.entity;

import static jakarta.persistence.GenerationType.*;

import com.clover.habbittracker.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
