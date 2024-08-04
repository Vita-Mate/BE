package com.example.vitamate.oauth2.exeption;

// OAuth2 인증 관련 실패를 위한 사용자 정의 예외 클래스

import javax.naming.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
