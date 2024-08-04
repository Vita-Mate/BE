package com.example.vitamate.oauth2.user;

import com.example.vitamate.oauth2.exeption.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// OAuth2 연동 해제시 OAuth2 제공자 별로 분기하여 OAuth2UserUnlink 인터페이스 구현체를 호출하여 연동 해제해주는 클래스
@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

    private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken) throws OAuth2AuthenticationProcessingException {
        if(OAuth2Provider.KAKAO.equals(provider)){
            kakaoOAuth2UserUnlink.unlink(accessToken);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                    "Unlink with " + provider.getRegistrationId() + " is not supported");
        }
    }

}
