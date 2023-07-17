package com.clover.habbittracker.domain.post.mapper;

import static org.mapstruct.ReportingPolicy.*;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.entity.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PostMapper {

	Post toPost(PostRequest request, Member member);

	@Mappings({
		@Mapping(source = "post.comments", target = "numOfComments", qualifiedByName = "commentsToSize"),
		@Mapping(source = "post.emojis", target = "numOfEmojis", qualifiedByName = "emojisToSize"),
		@Mapping(source = "post.createdAt", target = "createDate")
	})
	PostResponse toPostResponse(Post post);

	@Mappings({
		@Mapping(source = "post.createdAt", target = "createDate"),
		@Mapping(source = "post.updatedAt", target = "updateDate")
	})
	PostDetailResponse toPostDetail(Post post);

	@Named("commentsToSize")
	static Integer commentsToSize(List<Comment> comments) {
		return comments.size();
	}

	@Named("emojisToSize")
	static Integer emojisToSize(List<Emoji> emojis) {
		return emojis.size();
	}
}
