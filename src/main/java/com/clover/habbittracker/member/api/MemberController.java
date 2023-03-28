package com.clover.habbittracker.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.member.dto.MemberRequest;
import com.clover.habbittracker.member.dto.MemberResponse;
import com.clover.habbittracker.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	ResponseEntity<MemberResponse> getMyProfile(Authentication authentication) {
		Long memberId = (Long)authentication.getPrincipal();
		return new ResponseEntity<>(memberService.getProfile(memberId), HttpStatus.OK);
	}

	@PutMapping("/me")
	ResponseEntity<MemberResponse> updateMyProfile(Authentication authentication, MemberRequest request) {
		Long memberId = (Long)authentication.getPrincipal();
		return new ResponseEntity<>(memberService.updateProfile(memberId, request), HttpStatus.OK);
	}

	@DeleteMapping("/me")
	ResponseEntity<Void> deleteMember(Authentication authentication) {
		memberService.deleteProfile((Long) authentication.getPrincipal());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
