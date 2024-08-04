package com.example.vitamate.oauth2.user;

import java.util.Map;

// OAuth2 제공자 별로 리턴하는 사용자 정보 데이터의 구조와 필드 이름 등이 다른데,
// 이를 통합하기 위한 인터페이스 정의
public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getEmail();

    String getName();

    String getFirstName();

    String getLastName();

    String getNickName();

    String getProfileImageUrl();
}
