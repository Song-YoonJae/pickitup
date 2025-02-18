package com.ssafy.pickitup.domain.auth.command;

import com.ssafy.pickitup.domain.auth.command.dto.LoginRequestDto;
import com.ssafy.pickitup.domain.auth.command.dto.LogoutDto;
import com.ssafy.pickitup.domain.auth.command.dto.UserSignupDto;
import com.ssafy.pickitup.domain.auth.entity.Auth;
import com.ssafy.pickitup.domain.auth.entity.Role;
import com.ssafy.pickitup.domain.auth.query.dto.AuthDto;
import com.ssafy.pickitup.domain.user.command.service.UserCommandService;
import com.ssafy.pickitup.domain.user.exception.UserNotFoundException;
import com.ssafy.pickitup.domain.user.query.dto.UserResponseDto;
import com.ssafy.pickitup.security.entity.RefreshToken;
import com.ssafy.pickitup.security.exception.AuthNotFoundException;
import com.ssafy.pickitup.security.exception.PasswordException;
import com.ssafy.pickitup.security.exception.RefreshTokenException;
import com.ssafy.pickitup.security.jwt.JwtTokenDto;
import com.ssafy.pickitup.security.jwt.JwtTokenProvider;
import com.ssafy.pickitup.security.service.RedisService;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final AuthCommandJpaRepository authCommandJpaRepository;
    private final UserCommandService userCommandService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public UserResponseDto signup(UserSignupDto userSignupDto) {
        Auth auth = Auth.builder()
            .username(userSignupDto.getUsername())
            .name(userSignupDto.getName())
            .password(passwordEncoder.encode(userSignupDto.getPassword()))
            .email(userSignupDto.getEmail())
            .role(Role.USER)
            .build();
        authCommandJpaRepository.save(auth);
        return userCommandService.create(auth, userSignupDto);
    }


    @Transactional
    public JwtTokenDto login(LoginRequestDto loginRequestDto) {

        Auth auth = authCommandJpaRepository.findAuthByUsername(loginRequestDto.getUsername());

        if (auth == null) {
            throw new AuthNotFoundException("존재하지 않는 아이디입니다.");
        }
        //출석 횟수 증가
        increaseAttendCount(auth);
        AuthDto authDto = AuthDto.getAuth(auth);
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), authDto.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }

        // Login ID/PW를 기반으로 Authentication Token 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
            loginRequestDto.getPassword());
        // 실제로 검증이 이루어지는 부분
        Authentication authentication =
            authenticationManagerBuilder.getObject()
                .authenticate(usernamePasswordAuthenticationToken);
        // 인증 정보를 기반으로 JWT 토큰 생성
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenDto tokenSet = jwtTokenProvider.generateToken(authentication, authDto);
        // DB에 Refreshtoken 저장
        authDto.setRefreshToken(tokenSet.getRefreshToken());
        Auth updatedAuth = Auth.toDto(authDto);
        updatedAuth.setLastLoginDate();
        authCommandJpaRepository.save(updatedAuth);

        // RefreshToken Redis에 저장
        RefreshToken refreshToken = RefreshToken.builder()
            .authId(updatedAuth.getId())
            .refreshToken(tokenSet.getRefreshToken())
            .build();

        redisService.saveRefreshToken(refreshToken.getAuthId(), refreshToken.getRefreshToken());
        return tokenSet;
    }

    public LogoutDto logout(String accessToken) {
        Integer authId = jwtTokenProvider.extractAuthId(accessToken);
        redisService.saveJwtBlackList(accessToken);
        redisService.deleteRefreshToken(authId);
        Auth auth = authCommandJpaRepository.findAuthById(authId);
        auth.deleteRefreshToken();
        authCommandJpaRepository.save(auth);

        return new LogoutDto(auth.getUsername());
    }

    @Transactional
    public void deactivateAuth(int authId, String password) {
        if (validatePassword(authId, password, true)) {
            Auth auth = authCommandJpaRepository.findAuthById(authId);
            //유저 비활성화
            auth.deactivate();
        }
    }

    @Transactional
    public void activateAuth(int authId, String password) {
        if (validatePassword(authId, password, false)) {
            Auth auth = authCommandJpaRepository.findDeletedAuthById(authId).orElseThrow(
                UserNotFoundException::new);
            //유저 비활성화
            auth.activate();
        }
    }

    @Transactional(readOnly = true)
    public boolean validatePassword(int authId, String password, boolean flag) {
        Auth auth;
        if (flag) {
            //활성화 유저
            auth = authCommandJpaRepository.findById(authId).orElseThrow(
                UserNotFoundException::new);
        } else {
            //비활성화 유저
            auth = authCommandJpaRepository.findDeletedAuthById(authId).orElseThrow(
                UserNotFoundException::new);
        }

        if (passwordEncoder.matches(password, auth.getPassword())) {
            return true;
        }
        throw new PasswordException("비밀번호가 일치하지 않습니다.");
    }

    @Transactional
    public void changePassword(Integer authId, String password) {
        Auth auth = authCommandJpaRepository.findAuthById(authId);
        auth.changePassword(passwordEncoder.encode(password));
    }


    public JwtTokenDto reissueToken(String accessToken, String refreshToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(
            jwtTokenProvider.resolveToken(accessToken));

        Integer principal = Integer.valueOf(authentication.getName());
        String refreshTokenInDB = redisService.getRefreshToken(principal);

        Auth auth = authCommandJpaRepository.findAuthById(principal);

        if (refreshTokenInDB == null) { // Redis에 RT 없을 경우
            refreshTokenInDB = auth.getRefreshToken();
            if (refreshTokenInDB == null) { // MySQL에 RT 없을 경우
                throw new RefreshTokenException("refresh token 값이 존재하지 않습니다.");
            }
        }

        //토큰 꺼내기
        refreshToken = refreshToken.substring(7);

        if (!refreshTokenInDB.equals(refreshToken)) {
            redisService.deleteRefreshToken(principal);
            auth.deleteRefreshToken();
            throw new RefreshTokenException("Refresh Token 값이 일치하지 않습니다.");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            redisService.deleteRefreshToken(principal);
            auth.deleteRefreshToken();
            throw new MalformedJwtException("유효하지 않은 토큰입니다.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (redisService.getRefreshToken(principal) != null) {
            // Redis에 저장되어 있는 RT 삭제
            redisService.deleteRefreshToken(principal);
        }
        // 토큰 재발급
        AuthDto authDto = AuthDto.getAuth(auth);
        JwtTokenDto reissueTokenDto = jwtTokenProvider.generateToken(authentication, authDto);

        String reissueRefreshToken = reissueTokenDto.getRefreshToken();
        // Redis, DB 에 새로 발급 받은 RT 저장
        redisService.saveRefreshToken(principal, reissueRefreshToken);

        auth.setRefreshToken(reissueRefreshToken);

        return reissueTokenDto;
    }

    @Transactional
    public void increaseAttendCount(Auth auth) {
        LocalDate lastLoginDate = auth.getLastLoginDate();
        //최초 로그인이면 출석 증가
        if (lastLoginDate == null) {
            auth.getUser().increaseAttendCount();

        } else {
            //마지막 로그인 날짜가 현재 날짜 이전일 경우에만 출석 증가
            if (lastLoginDate.isBefore(LocalDate.now())) {
                auth.getUser().increaseAttendCount();
            }
        }
    }
}
