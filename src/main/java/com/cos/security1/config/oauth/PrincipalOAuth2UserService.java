package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /**
         * registrationId -> 어떤 OAuth로 로그인 했는지 확인 가능.
         */
        System.out.println("userRequest = " + userRequest.getClientRegistration());
        System.out.println("getAccessToken = " + userRequest.getAccessToken());

        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴 받음(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> 회원 프로필 받아야함 -> loadUser 함수 -> 구글로부터 회원 프로필을 받아준다.
        System.out.println("getAttribute = " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원 가입 강제로 진행 예정
        return super.loadUser(userRequest);
    }
}
