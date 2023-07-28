package com.clover.habbittracker.domain.emoji.repository;

import java.util.List;
import java.util.Optional;

import com.clover.habbittracker.domain.emoji.entity.Emoji;

public interface EmojiCustomRepository {

	List<Emoji> findAllInDomain(Emoji.Domain domain, Long domainId);

	Optional<Emoji> findByUniqueKey(Long memberId, Emoji.Domain domain, Long domainId);

	int countByDomain(Emoji.Domain domain, Long domainId);

}
