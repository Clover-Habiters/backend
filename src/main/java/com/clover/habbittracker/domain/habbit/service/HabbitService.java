package com.clover.habbittracker.domain.habbit.service;

import com.clover.habbittracker.domain.habbit.dto.HabbitRequest;
import com.clover.habbittracker.domain.habbit.dto.HabbitRespose;

public interface HabbitService {
	HabbitRespose register(Long MemberId, HabbitRequest request);

}
