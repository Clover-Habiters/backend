package com.clover.habbittracker.domain.post.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.entity.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PostMapper {

	Post toPost(PostRequest request, Member member);

	@Mapping(source = "post.member.id", target = "memberId")
	PostDetailResponse toPostDetail(Post post);
}
