package com.clover.habbittracker.domain.member.api;

import static com.clover.habbittracker.global.dto.ResponseType.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.service.MemberService;
import com.clover.habbittracker.global.dto.BaseResponse;
import com.clover.habbittracker.ncp.ObjectStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

	private final MemberService memberService;

	private final ObjectStorageService objectStorageService;

	@GetMapping("/me")
	ResponseEntity<BaseResponse<MemberResponse>> getMyProfile(@AuthenticationPrincipal Long memberId) {
		MemberResponse profile = memberService.getProfile(memberId);
		BaseResponse<MemberResponse> response = BaseResponse.of(profile, MEMBER_READ);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping(path = "/me",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	ResponseEntity<BaseResponse<MemberResponse>> updateMyProfile(@AuthenticationPrincipal Long memberId,
		MemberRequest request,
		@RequestPart(required = false) MultipartFile file) {
		String profileImgUrl = objectStorageService.profileImgSave(file);
		request.setProfileImgUrl(profileImgUrl);
		MemberResponse updateProfile = memberService.updateProfile(memberId, request);
		BaseResponse<MemberResponse> response = BaseResponse.of(updateProfile, MEMBER_UPDATE);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/me")
	ResponseEntity<BaseResponse<Void>> deleteMember(@AuthenticationPrincipal Long memberId) {
		memberService.deleteProfile(memberId);
		BaseResponse<Void> response = BaseResponse.of(null, MEMBER_DELETE);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

}
