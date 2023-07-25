package com.clover.habbittracker.domain.vote.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "vote")
@NoArgsConstructor(access = PROTECTED)
public class VoteItem {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String content;

	private Integer count;

	private Integer order;

	@ManyToOne
	private Vote vote;

}
