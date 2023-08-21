package com.clover.habbittracker.domain.comment.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CommentMapper {

	@Mappings({
		@Mapping(source = "request.content", target = "content"),
		@Mapping(source = "member", target = "member")
	})
	Comment toComment(CommentRequest request, Member member, Post post);

	@Mappings({
		@Mapping(source = "request.content", target = "content"),
		@Mapping(source = "member", target = "member")
	})
	Comment toReply(CommentRequest request, Member member, Post post, Long parentId);

	@Mapping(source = "comment.member.id", target = "authorId")
	CommentResponse toCommentResponse(Comment comment);
}
