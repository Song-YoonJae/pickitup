package com.ssafy.pickitup.domain.recruit.query;

import com.ssafy.pickitup.domain.recruit.entity.RecruitDocumentElasticsearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

public interface RecruitQueryElasticsearchRepository extends
    ElasticsearchRepository<RecruitDocumentElasticsearch, Integer>,
    CrudRepository<RecruitDocumentElasticsearch, Integer> {

    @Query("{\"bool\": {\"must\": [{\"match_phrase\": {\"qualification_requirements\": \"?0\"}}]}}")
    Page<RecruitDocumentElasticsearch> findByQualificationRequirementsContaining(String keyword,
        Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match_phrase\": {\"preferred_requirements\": \"?0\"}}]}}")
    Page<RecruitDocumentElasticsearch> findByPreferredRequirementsContaining(String keyword,
        Pageable pageable);
}
