package com.example.vitamate.config;

// 스프링 시큐리티와 OAuth2 설정 클래스
// CSRF 설정을 Disable 한다
// OAuth2 핸들러 및 서비스를 빈으로 등록

import com.example.vitamate.jwt.JwtAuthenticationFilter;
import com.example.vitamate.jwt.JwtTokenProvider;
import com.example.vitamate.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.vitamate.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.vitamate.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.example.vitamate.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // basic auth 및 csrf 보안 사용하지 않음
                .httpBasic(http -> http.disable())
                .csrf(csrf -> csrf.disable())
                // JWT 사용하기 때문에 세션 사용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 해당 API에 대해서는 모든 요청을 허가
                        .requestMatchers("/members/sign-in",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**").permitAll()
                        // USER 권한이 있어야 요청할 수 있음
                        .requestMatchers("/members/test").hasRole("USER")
                        // 이 밖에 모든 요청에 대해서 인증 필요
                        .anyRequest().authenticated()
                )
                // JWT 인증을 위해 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class).build();

//        http.csrf(AbstractHttpConfigurer::disable)
//                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 DB
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(antMatcher("/api/admin/**")).hasRole("ADMIN")
//                        .requestMatchers(antMatcher("/api/user/++")).hasRole("USER")
//                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
//                        .requestMatchers("/login/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2Login(configure ->
//                        configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
//                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
//                                .successHandler(oAuth2AuthenticationSuccessHandler)
//                                .failureHandler(oAuth2AuthenticationFailureHandler)
//                );
//        return http.build();
    }
}