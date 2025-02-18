package com.ssafy.pickitup.global.config.mysql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 현재 트랜잭션이 읽기 전용인 경우는 slave, 아닐 경우 master를 반환한다 -> 트랜잭션의 속성에 따라 데이터베이스 연결을 결정
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        return isReadOnly ? "slave" : "master";
    }
}
