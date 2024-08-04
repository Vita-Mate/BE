package com.example.vitamate.oauth2.handler;

import com.example.vitamate.domain.Member;
import com.example.vitamate.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.vitamate.oauth2.exeption.OAuth2AuthenticationProcessingException;
import com.example.vitamate.oauth2.service.OAuth2UserPrincipal;
import com.example.vitamate.oauth2.user.OAuth2Provider;
import com.example.vitamate.oauth2.user.OAuth2UserUnlinkManager;
import com.example.vitamate.oauth2.util.CookieUtils;
import com.example.vitamate.service.MemberService.MemberCommandService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.example.vitamate.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.example.vitamate.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

// OAuth2 인증 성공시 호출되는 핸들러
// 처음 프론트엔드에서 백엔드로 로그인 요청시 mode 쿼리 파라미터에 담긴 값에 따라 분기하여 처리한다
// mode 값이 login 이면 사용자 정보를 DB에 저장
// 서비스 자체 엑세스 토큰과 리프레시 토큰을 생성하고, 리프레시 토큰을 DB에 저장
// mode 값이 unlink 이면 각 OAuth2 서비스에 맞는 연결 끊기 API를 호출하고, 사용자 정보를 DB에서 삭제하고, 리프레시 토큰을 DB에서 삭제한다.

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
//    private final MemberCommandService memberCommandService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

//        // targetUrl 인코딩 추가
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).build().toString();

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
//            // 오류 메시지 인코딩 추가
//            String errorMessage = UriUtils.encode("[authorization_request_not_found]", StandardCharsets.UTF_8);
//
//            return UriComponentsBuilder.fromUriString(targetUrl)
//                    .queryParam("error", errorMessage) //인코딩된 메시지 사용
//                    .build().toUriString();
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();

        }

        if ("login".equalsIgnoreCase(mode)) {
            // TODO: DB 저장
//            // 사용자의 이메일로 기존 회원 정보 조회 또는 신규 사용자 생성
//            Member member = memberCommandService.findOrCreateMember(
//                    principal.getUserInfo().getEmail(),
//                    principal.getUserInfo().getNickName()
//            );

            // TODO: 액세스 토큰, 리프레시 토큰 발급
//            String accessToken = principal.getUserInfo().getAccessToken();
//
//            OAuth2UserRequest userRequest = (OAuth2UserRequest) authentication.getDetails();
//            String refreshToken = userRequest.getAccessToken().getRefreshToken().getTokenValue();

            // TODO: 리프레시 토큰 DB 저장

            log.info("email={}, name={}, nickname={}, accessToken={}", principal.getUserInfo().getEmail(),
                    principal.getUserInfo().getName(),
                    principal.getUserInfo().getNickName(),
                    principal.getUserInfo().getAccessToken()
            );

            String accessToken = "test_access_token";
            String refreshToken = "test_refresh_token";

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString();

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            try {
                oAuth2UserUnlinkManager.unlink(provider, accessToken);
            } catch (OAuth2AuthenticationProcessingException e) {
                throw new RuntimeException(e);
            }

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

//        // 기본적으로 오류 메시지를 인코딩하여 변환
//        String errorMessage = UriUtils.encode("Login failed", StandardCharsets.UTF_8);
//        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("error", errorMessage) // 인코딩된 메시지 사용
//                .build().toUriString();
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();

    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}