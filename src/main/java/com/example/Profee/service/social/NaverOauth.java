package com.example.Profee.service.social;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// 공통 interface를 구현할 소셜 로그인 각 타입별 Class 생성 (Naver)
@Component
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {
    @Value("${sns.naver.url}")
    private String NAVER_SNS_BASE_URL;
    @Value("${sns.naver.client.id}")
    private String NAVER_SNS_CLIENT_ID;
    @Value("${sns.naver.callback.url}")
    private String NAVER_SNS_CALLBACK_URL;
    @Value("${sns.naver.client.secret}")
    private String NAVER_SNS_CLIENT_SECRET;
    @Value("${sns.naver.token.url}")
    private String NAVER_SNS_TOKEN_BASE_URL;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", NAVER_SNS_CLIENT_ID);
        params.put("redirect_uri", NAVER_SNS_CALLBACK_URL);
        params.put("state", "random_state_string"); // CSRF 방지를 위한 state 값 추가

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return NAVER_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", NAVER_SNS_CLIENT_ID);
        params.put("client_secret", NAVER_SNS_CLIENT_SECRET);
        params.put("redirect_uri", NAVER_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");
        params.put("state", "random_state_string"); // 앞서 전송한 state 값과 일치해야 함

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(NAVER_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 콘솔에 받은 토큰 출력
            String tokenResponse = responseEntity.getBody();
            System.out.println("Received Access Token: " + tokenResponse);

            return responseEntity.getBody();
        }
        return "네이버 로그인 요청 처리 실패";
    }

    public String requestAccessTokenUsingURL(String code) {
        try {
            URL url = new URL(NAVER_SNS_TOKEN_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", NAVER_SNS_CLIENT_ID);
            params.put("client_secret", NAVER_SNS_CLIENT_SECRET);
            params.put("redirect_uri", NAVER_SNS_CALLBACK_URL);
            params.put("grant_type", "authorization_code");
            params.put("state", "random_state_string");

            String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
            bous.write(parameterString.getBytes());
            bous.flush();
            bous.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if (conn.getResponseCode() == 200) {
                // 콘솔에 받은 토큰 출력
                String tokenResponse = sb.toString();
                System.out.println("Received Access Token: " + tokenResponse);

                return tokenResponse;
            }
            return "네이버 로그인 요청 처리 실패";
        } catch (IOException e) {
            throw new IllegalArgumentException("알 수 없는 네이버 로그인 Access Token 요청 URL 입니다 :: " + NAVER_SNS_TOKEN_BASE_URL);
        }
    }
}
