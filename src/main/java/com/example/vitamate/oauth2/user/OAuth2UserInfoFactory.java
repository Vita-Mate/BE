package com.example.vitamate.oauth2.user;

// OAuth2 인증시 엑세스 토큰으로 사용자 정보를 가져왔을 때,
// OAuth2 제공자 별로 분기하여 OAuth2UserInfo 인터페이스 구현체를 호출하여
// OAuth2UserInfo 객체를 생성해주는 그대로 퍅토리 클래스

import java.util.Map;

import com.example.vitamate.oauth2.exeption.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                   String accessToken,
                                                   Map<String, Object> attributes)
                                                    throws OAuth2AuthenticationProcessingException {
        if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
