package com.clover.habbittracker.domain.diary.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.dto.DiaryResponse;
import com.clover.habbittracker.domain.diary.entity.Diary;
import com.clover.habbittracker.domain.diary.exception.DiaryExpiredException;
import com.clover.habbittracker.domain.diary.exception.DiaryNotFoundException;
import com.clover.habbittracker.domain.diary.repository.DiaryRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.util.DateCalculate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;

	private final MemberRepository memberRepository;

	@Override
	public Long register(Long memberId, DiaryRequest request) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

		Diary diary = Diary.builder()
			.content(request.getContent())
			.member(member)
			.endUpdateDate(LocalDateTime.now().plusHours(24))
			.build();
		diaryRepository.save(diary);

		return diary.getId();
	}

	@Override
	public List<DiaryResponse> getMyList(Long memberId, String date) {
		Map<String, LocalDateTime> dateMap = DateCalculate.startEnd(date);
		return diaryRepository.findByMemberId(memberId, dateMap.get("start"), dateMap.get("end"))
			.stream()
			.map(DiaryResponse::from)
			.toList();
	}

	@Override
	@Transactional
	public DiaryResponse updateDiary(Long diaryId, DiaryRequest request) {
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() -> new DiaryNotFoundException(diaryId));
		if (diary.getEndUpdateDate().isBefore(LocalDateTime.now())) {
			throw new DiaryExpiredException(diaryId);
		}
		Optional.ofNullable(request.getContent()).ifPresent(diary::setContent);

		return DiaryResponse.from(diary);
	}

	@Override
	public void delete(Long diaryId) {
		diaryRepository.deleteById(diaryId);
	}
}
