package com.clover.habbittracker.habbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.habbit.entity.Habbit;

public interface HabbitRepository extends JpaRepository<Habbit,Long> {

}
