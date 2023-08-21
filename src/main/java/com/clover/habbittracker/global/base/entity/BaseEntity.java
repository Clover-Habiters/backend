package com.clover.habbittracker.global.base.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createDate;

	@LastModifiedDate
	@Column(name = "updated_date")
	private LocalDateTime updateDate;

	private boolean deleted;

	public void setUpdateDate(LocalDateTime updatedDate) {
		this.updateDate = updatedDate;
	}
}
