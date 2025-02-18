package com.ssafy.pickitup.domain.keyword.api;


import static com.ssafy.pickitup.global.api.ApiUtils.success;

import com.ssafy.pickitup.domain.keyword.dto.KeywordResponseDto;
import com.ssafy.pickitup.domain.keyword.service.KeywordQueryService;
import com.ssafy.pickitup.global.api.ApiUtils.ApiResult;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://pickitup.online", "http://localhost:3000",
    "http://localhost:8080", "https://spring.pickitup.online"}, exposedHeaders = "*")
@RequestMapping("/keywords")
public class KeywordController {

    private final KeywordQueryService keywordQueryService;

    @GetMapping
    public ApiResult<?> getAllKeywords() {
        List<KeywordResponseDto> allKeyword = keywordQueryService.findAllKeyword();
        return success(allKeyword);
    }
    @GetMapping("/map")
    public ApiResult<?> getKeywordMap() {
        Map<String, Integer> keywordMap = keywordQueryService.findKeywordMap();
        return success(keywordMap);
    }
}
