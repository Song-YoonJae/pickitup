package com.ssafy.pickitup.domain.auth.entity;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;
}
