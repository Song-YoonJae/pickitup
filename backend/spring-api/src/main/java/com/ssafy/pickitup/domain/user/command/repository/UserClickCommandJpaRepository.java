package com.ssafy.pickitup.domain.user.command.repository;

import com.ssafy.pickitup.domain.user.entity.UserClick;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClickCommandJpaRepository extends JpaRepository<UserClick, Integer> {

    Optional<UserClick> findByUserIdAndRecruitId(Integer userId, Integer recruitIt);

    List<UserClick> findAllByUserId(Integer userId);
}
