package com.example.Profee.service.social;

import org.springframework.stereotype.Component;

// 공통 interface를 구현할 소셜 로그인 각 타입별 Class 생성 (Naver)
@Component
public class NaverOauth implements SocialOauth {
    @Override
    public String getOauthRedirectURL() {
        return "";
    }

    @Override
    public String requestAccessToken(String code) {
        return null;
    }
}