package com.example.vitamate.oauth2.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// OAuth2 서비스를 구분하기 위한 enum
@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
//    GOOGLE("google"),
//    NAVER("naver"),
    KAKAO("kakao");

    private final String registrationId;
}
