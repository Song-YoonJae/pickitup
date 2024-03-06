package com.ssafy.pickitup.domain.user.command;

import com.ssafy.pickitup.domain.user.command.dto.UserSignupDto;
import com.ssafy.pickitup.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCommandJpaRepository userCommandJpaRepository;

    public void signup(UserSignupDto userSignupDto) {
        User user = User.builder()
            .name(userSignupDto.getName())
            .build();
        userCommandJpaRepository.save(user);
    }

}
