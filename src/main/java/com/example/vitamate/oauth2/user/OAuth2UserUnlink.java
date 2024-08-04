package com.example.vitamate.oauth2.user;

// OAuth2 제공자 별로 OAuth2 애플리케이션과 연동 해제하는 방법이 다른데,
// 이를 통합하기 위한 인터페이스 정의
public interface OAuth2UserUnlink {
    void unlink(String accessToken);
}
