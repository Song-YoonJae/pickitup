package com.ssafy.pickitup.domain.selfdocument.command;

import com.ssafy.pickitup.domain.selfdocument.command.dto.SubQuestionCommandRequestDto;
import com.ssafy.pickitup.domain.selfdocument.command.dto.SubQuestionCommandResponseDto;
import com.ssafy.pickitup.domain.selfdocument.entity.MainQuestion;
import com.ssafy.pickitup.domain.selfdocument.entity.SubQuestion;
import com.ssafy.pickitup.domain.selfdocument.exception.SubQuestionNotFoundException;
import com.ssafy.pickitup.domain.selfdocument.query.MainQuestionQueryService;
import com.ssafy.pickitup.domain.selfdocument.query.SubQuestionQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubQuestionCommandServiceImpl implements SubQuestionCommandService {

    private final MainQuestionQueryService mainQueryService;
    private final MainQuestionCommandJpaRepository mainCommandRepository;
    private final SubQuestionQueryJpaRepository subQueryRepository;
    private final SubQuestionCommandJpaRepository subCommandRepository;

    @Override
    @Transactional
    public SubQuestionCommandResponseDto registerSubQuestion(
        Integer mainId, SubQuestionCommandRequestDto dto) {
        MainQuestion mainQuestion = mainQueryService.searchById(mainId);
        SubQuestion subQuestion = dto.toEntity(mainQuestion);
        mainQuestion.getSubQuestions().add(subQuestion);
        mainCommandRepository.save(mainQuestion);
        return subQuestion.toCommandResponse();
    }

    @Override
    @Transactional
    public boolean deleteSubQuestion(Integer subId) {
        try {
            SubQuestion subQuestion = subQueryRepository.findById(subId)
                .orElseThrow(SubQuestionNotFoundException::new);
            subQuestion.getMainQuestion().getSubQuestions().remove(subQuestion);
            subCommandRepository.delete(subQuestion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public SubQuestionCommandResponseDto modifySubQuestion(
        Integer subId, SubQuestionCommandRequestDto dto) {
        SubQuestion subQuestion = subQueryRepository.findById(subId)
            .orElseThrow(SubQuestionNotFoundException::new);
        if (dto.getTitle() != null) {
            subQuestion.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            subQuestion.setContent(dto.getContent());
        }
        if (dto.getCompany() != null) {
            subQuestion.setCompany(dto.getCompany());
        }
        return subCommandRepository.save(subQuestion).toCommandResponse();
    }
}
