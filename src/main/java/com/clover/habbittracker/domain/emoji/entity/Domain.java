package com.clover.habbittracker.domain.emoji.entity;

public enum Domain {
	COMMENT, POST;

	public boolean isPost() {
		return this == POST;
	}

	public boolean isComment() {
		return this == COMMENT;
	}
}
