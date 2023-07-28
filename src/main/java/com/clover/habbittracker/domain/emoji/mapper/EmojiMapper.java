package com.clover.habbittracker.domain.emoji.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.clover.habbittracker.domain.emoji.dto.EmojiResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface EmojiMapper {

	@Mapping(target = "emojiType", source = "emoji", qualifiedByName = "emojiTypeMethod")
	@Mapping(target = "domain", source = "emoji", qualifiedByName = "emojiDomainMethod")
	EmojiResponse toEmojiResponse(Emoji emoji);

	@Named("emojiTypeMethod")
	default String mapEmojiType(Emoji emoji) {
		return emoji.getType().name();
	}

	@Named("emojiDomainMethod")
	default String mapEmojiDomain(Emoji emoji) {
		return emoji.getDomain().name();
	}
}