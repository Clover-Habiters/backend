package com.clover.habbittracker.global.restdocs.common;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.base.entity.RestDocsEnum;
import com.clover.habbittracker.global.restdocs.enums.EnumDocs;
import com.clover.habbittracker.global.restdocs.enums.EnumResponse;

@RestController
@RequestMapping("/docs")
public class CommonDocController {

	@GetMapping("/enums")
	public EnumResponse<EnumDocs> findEnums() {

		Map<String, String> emojiDomain = getDocs(Emoji.Domain.values());
		Map<String, String> emojiType = getDocs(Emoji.Type.values());
		Map<String, String> category = getDocs(Post.Category.values());

		// 전부 담아서 반환 -> 테스트에서는 이걸 꺼내 해석하여 조각을 만들면 된다.
		return EnumResponse.of(EnumDocs.builder()
			.emojiDomain(emojiDomain)
			.emojiType(emojiType)
			.category(category)
			.build()
		);
	}

	private Map<String, String> getDocs(RestDocsEnum[] enumTypes) {
		return Arrays.stream(enumTypes)
			.collect(Collectors.toMap(RestDocsEnum::getName, RestDocsEnum::getDescription));
	}
}
