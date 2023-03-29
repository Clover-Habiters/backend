package com.clover.habbittracker.domain.habbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.habbit.entity.Habbit;

public interface HabbitRepository extends JpaRepository<Habbit,Long> {

}
