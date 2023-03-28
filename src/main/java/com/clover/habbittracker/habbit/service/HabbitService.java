package com.clover.habbittracker.habbit.service;

import com.clover.habbittracker.habbit.dto.HabbitRequest;
import com.clover.habbittracker.habbit.dto.HabbitRespose;

public interface HabbitService {
	HabbitRespose register(Long MemberId, HabbitRequest request);

}
