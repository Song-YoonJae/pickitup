package com.ssafy.pickitup.domain.selfdocument.command.dto;

import com.ssafy.pickitup.domain.selfdocument.entity.MainQuestion;
import com.ssafy.pickitup.domain.selfdocument.entity.SubQuestion;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class SubQuestionCommandRequestDto {

    private String title;
    private String content;
    private String company;

    public SubQuestion toEntity(MainQuestion mainQuestion) {
        return SubQuestion.builder()
            .title(this.title)
            .content(this.content)
            .company(this.company)
            .mainQuestion(mainQuestion)
            .build();
    }
}
