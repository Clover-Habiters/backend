package com.clover.habbittracker.domain.emoji.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.emoji.dto.EmojiResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.mapper.EmojiMapper;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmojiService {

	private final EmojiRepository emojiRepository;
	private final EmojiMapper emojiMapper;

	public List<EmojiResponse> getAllEmojisInDomain(Emoji.Domain domain, Long domainId) {
		return emojiRepository.findAllInDomain(domain, domainId)
			.stream()
			.map(emojiMapper::toEmojiResponse)
			.toList();
	}

	@Transactional
	public EmojiResponse save(Emoji.Type emojiType, Long memberId, Emoji.Domain domain, Long domainId) {

		// 존재하면 상태 변경, 존재하지 않는 경우 생성 -> Insert or Update
		// 그냥 save하고 다른 점이 있는지 확인 필요
		Emoji savedEmoji = emojiRepository.findByUniqueKey(memberId, domain, domainId)
			.map(e -> e.updateStatus(emojiType))
			.orElseGet(
				() -> emojiRepository.save(
					Emoji.builder()
						.type(emojiType)
						.domain(domain)
						.domainId(domainId)
						.memberId(memberId)
						.build()
				)
			);

		return emojiMapper.toEmojiResponse(savedEmoji);
	}

	@Transactional
	public void delete(Long memberId, Emoji.Domain domain, Long domainId) {
		// 존재하는 경우에만 동작 없으면 로깅
		emojiRepository.findByUniqueKey(memberId, domain, domainId)
			.ifPresentOrElse(emojiRepository::delete,
				() -> log.warn("해당 이모지가 존재하지 않습니다.{},{},{}", memberId, domain, domainId)
			);
	}
}
