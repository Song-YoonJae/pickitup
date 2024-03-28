package com.ssafy.pickitup.domain.user.command.service;

import com.ssafy.pickitup.domain.user.dto.UserRecommendDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserRecommendService {


    private final WebClient webClient;

    @Autowired
    public UserRecommendService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://recommend.pickitup.online")
            .build();
    }

    public void sendRequestToScalaServer() {
        // 요청 본문 데이터 - JSON 형식
        String requestBody = "{\"key\":\"value\"}";

        // WebClient를 사용하여 비동기 요청 보내기
        Mono<String> responseMono = webClient.get()
            .uri("/api/test")
            .retrieve()
            .bodyToMono(String.class);

        // Mono를 구독하고 응답을 처리
        responseMono.subscribe(
            response -> System.out.println("스칼라 서버에서 받은 응답: " + response),
            error -> System.err.println("에러 발생: " + error),
            () -> System.out.println("응답 처리 완료")
        );
    }

    public void sendUserKeywordToScalaServer() {
        // 요청 본문 데이터 - JSON 형식
        String requestBody = "{\"key\":\"value\"}";

        // WebClient를 사용하여 비동기 요청 보내기
        Mono<String> responseMono = webClient.post()
            .uri("/api/test")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .retrieve()
            .bodyToMono(String.class);

        // Mono를 구독하고 응답을 처리
        responseMono.subscribe(
            response -> System.out.println("스칼라 서버에서 받은 응답: " + response),
            error -> System.err.println("에러 발생: " + error),
            () -> System.out.println("응답 처리 완료")
        );
    }

//    public Mono<String> getUserRecommendRecruitList(Integer userId) {
//        // WebClient를 사용하여 비동기 요청 보내기
//        return webClient.get()
//            .uri("/api/recommend/normal/{userId}", userId)
//            .retrieve()
//            .bodyToMono(String.class)
//            .doOnSuccess(response -> log.info("스칼라 서버에서 받은 응답 = {}", response))
//            .doOnError(error -> log.error("에러 발생 = {}", error))
//            .doFinally(signal -> log.info("응답 처리 완료"));
//    }

    public List<UserRecommendDto> getUserRecommendRecruitList(Integer userId) {
        // WebClient를 사용하여 동기 요청 보내기
        Flux<UserRecommendDto> response = webClient.get()
            .uri("/api/recommend/normal/{userId}", userId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(UserRecommendDto.class);

        return response.collectList().block();
    }
}
