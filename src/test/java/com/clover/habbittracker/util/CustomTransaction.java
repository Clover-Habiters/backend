package com.clover.habbittracker.util;

import org.springframework.transaction.TransactionDefinition;

public class CustomTransaction implements TransactionDefinition {

	@Override
	public int getPropagationBehavior() {
		return TransactionDefinition.PROPAGATION_REQUIRES_NEW;
	}
}
