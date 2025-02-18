package com.ssafy.pickitup.domain.auth.api;

import com.ssafy.pickitup.domain.auth.command.AuthCommandService;
import com.ssafy.pickitup.domain.auth.command.dto.LoginRequestDto;
import com.ssafy.pickitup.domain.auth.command.dto.LogoutDto;
import com.ssafy.pickitup.domain.auth.command.dto.UserSignupDto;
import com.ssafy.pickitup.domain.auth.query.AuthQueryService;
import com.ssafy.pickitup.domain.auth.query.dto.AuthDto;
import com.ssafy.pickitup.domain.auth.query.dto.AuthProfileDto;
import com.ssafy.pickitup.domain.auth.query.dto.PasswordDto;
import com.ssafy.pickitup.domain.user.query.dto.UserResponseDto;
import com.ssafy.pickitup.global.annotation.AuthID;
import com.ssafy.pickitup.global.api.ApiUtils.ApiResult;
import com.ssafy.pickitup.security.jwt.JwtProperties;
import com.ssafy.pickitup.security.jwt.JwtTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.pickitup.global.api.ApiUtils.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://pickitup.online", "http://localhost:3000",
    "http://localhost:8080", "https://spring.pickitup.online"}, exposedHeaders = "*")
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "회원 인증 정보 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;

    @Operation(summary = "회원 가입 API")
    @PostMapping("/signup")
    public ApiResult<UserResponseDto> signup(@RequestBody UserSignupDto userSignupDto) {
        //auth 정보 저장
        UserResponseDto userResponseDto = authCommandService.signup(userSignupDto);
        return success(userResponseDto);
    }

    @Operation(summary = "자체 로그인 API")
    @PostMapping("/login")
    public ApiResult<JwtTokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtTokenDto authResponseDto = authCommandService.login(loginRequestDto);
        return success(authResponseDto);
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public ApiResult<LogoutDto> logout(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        LogoutDto logout = authCommandService.logout(accessToken);
        return success(logout);
    }

    @Operation(summary = "동시 로그인 체크 API")
    @GetMapping("/check-concurrent-login")
    public ResponseEntity<?> detectConcurrentLogin(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = request.getHeader(JwtProperties.REFRESH_TOKEN);
        authQueryService.detectConcurrentUser(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "회원 프로필 조회 API")
    @GetMapping("/profile")
    public ApiResult<AuthProfileDto> profileUser(@AuthID Integer authId) {
        AuthDto authDto = authQueryService.getUserById(authId);
        AuthProfileDto authProfileDto = AuthProfileDto.authInfoFromAuthDto(authDto);
        return success(authProfileDto);
    }

    @Operation(summary = "회원 비활성화 API")
    @PatchMapping("/deactivate")
    public ApiResult<String> deactivateAuth(@AuthID Integer authId,
        @RequestBody PasswordDto password) {
        authCommandService.deactivateAuth(authId, password.getPassword());
        return success("비활성화 되었습니다.");
    }

    @Operation(summary = "회원 활성화 API")
    @PatchMapping("/activate")
    public ApiResult<String> activateAuth(@AuthID Integer authId,
        @RequestBody PasswordDto password) {
        authCommandService.activateAuth(authId, password.getPassword());
        return success("활성화 되었습니다.");
    }

    @Operation(summary = "비밀번호 재확인 API")
    @PostMapping("/password")
    public ApiResult<?> confirmPassword(@AuthID Integer authId,
        @RequestBody PasswordDto password) {
        authCommandService.validatePassword(authId, password.getPassword(), true);
        return success("비밀번호가 일치합니다.");
    }

    @Operation(summary = "비밀번호 변경 API")
    @PutMapping("/password")
    public ApiResult<?> changePassword(@AuthID Integer authId,
        @RequestBody PasswordDto password) {
        authCommandService.changePassword(authId, password.getPassword());
        return success("비밀번호 변경 성공");
    }

    @Operation(summary = "아이디 중복체크 API")
    @PostMapping("/check/{username}")
    public ApiResult<?> checkUserId(@PathVariable String username) {
        authQueryService.idDuplicated(username);
        return success("사용 가능한 아이디입니다.");
    }

    @Operation(summary = "RT 재발급 API, reqeust(헤더) : Access Token, Refresh Token")
    @PostMapping("/token/refresh")
    public ApiResult<?> reissue(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = request.getHeader(JwtProperties.REFRESH_TOKEN);
        JwtTokenDto reissuedToken = authCommandService.reissueToken(accessToken, refreshToken);
        return success(reissuedToken.responseDto());
    }
}
