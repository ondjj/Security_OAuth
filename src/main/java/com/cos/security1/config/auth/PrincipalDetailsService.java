package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행 -> 규칙이다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    // loginForm에 있는 input 태그 name 속성인 username 과 일치해야한다.
    // 만약 name 속성과 loadUserByUsername의 매개 변수를 다르게 설정하고 싶다면 SecurityConfig에 추가 설정이 필요함
    // .usernameParameter("name 속성 설정 이름")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            // 시큐리티_session(Authentication(UserDetails))
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
