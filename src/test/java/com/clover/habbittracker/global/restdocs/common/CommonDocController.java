package com.clover.habbittracker.global.restdocs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.base.dto.ApiResponse;
import com.clover.habbittracker.global.base.entity.RestDocsEnum;
import com.clover.habbittracker.global.restdocs.enums.EnumCode;
import com.clover.habbittracker.global.restdocs.enums.EnumResponse;

@RestController
@RequestMapping("/test/docs")
public class CommonDocController {

	@GetMapping("/commons")
	public ApiResponse<Object> findResponse() {
		return ApiResponse.success("data");
	}

	@GetMapping("/enums")
	public EnumResponse<EnumCode> findEnums() {

		Map<String, String> emojiDomain = getDocs(Emoji.Domain.values());
		Map<String, String> emojiType = getDocs(Emoji.Type.values());
		Map<String, String> category = getDocs(Post.Category.values());
		Map<String, String> searchType = getDocs(PostSearchCondition.SearchType.values());

		// 전부 담아서 반환 -> 테스트에서는 이걸 꺼내 해석하여 조각을 만들면 된다.
		return EnumResponse.of(EnumCode.builder()
			.emojiDomain(emojiDomain)
			.emojiType(emojiType)
			.category(category)
			.searchType(searchType)
			.build()
		);
	}

	@GetMapping("/pageable")
	public Page<Void> findPageable() {
		List<Void> voids = new ArrayList<>();
		return new PageImpl<>(voids, PageRequest.of(0, 15), 1);
	}

	private Map<String, String> getDocs(RestDocsEnum[] enumTypes) {
		return Arrays.stream(enumTypes)
			.collect(Collectors.toMap(RestDocsEnum::getName, RestDocsEnum::getDescription));
	}
}
