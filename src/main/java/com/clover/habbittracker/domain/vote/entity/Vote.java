package com.clover.habbittracker.domain.vote.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.List;

import com.clover.habbittracker.global.base.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "vote")
@NoArgsConstructor(access = PROTECTED)
public class Vote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String title;

	private String description;

	@OneToMany(mappedBy = "vote")
	private List<VoteItem> voteItemList;

}
