package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 1. 코드 받기(인증)
 * 2. 엑세스 토큰(권한)
 * 3. 사용자 프로필 정보를 가져오고
 * 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
 * 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> 집주소, 백화점몰 -> vip 등급, 일반 등급
 */

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    PrincipalOAuth2UserService principalOAuth2UserService;

//    순환 참조 오류 발생 -> BCryptPasswordEncoder 전용 클래스를 별도로 생성하는걸로 해결.
//    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
//    public BCryptPasswordEncoder encodePwd(){
//        return new BCryptPasswordEncoder();
//    }

    /**
     * 기존 WebSecurityConfigurerAdapter 방식은 deprecated 처리되었다. 아래 블로그를 참고
     * https://blog.naver.com/PostView.naver?blogId=h850415&logNo=222755455272&parentCategoryNo=&categoryNo=37&viewDate=&isShowPopularPosts=true&from=search
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소 !
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리 필요 Tip. 코드 X,(엑세스토큰 + 사용자 프로필 정보 O)
                .userInfoEndpoint()
                .userService(principalOAuth2UserService);


        return http.build();
    }
}
