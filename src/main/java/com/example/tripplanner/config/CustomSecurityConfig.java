package com.example.tripplanner.config;

import com.example.tripplanner.member.security.filter.JWTCheckFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableMethodSecurity
public class CustomSecurityConfig {

    private JWTCheckFilter jwtCheckFilter;

    @Autowired
    private void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter){
        this.jwtCheckFilter = jwtCheckFilter;
    }

    /* 1. Password 알고리즘
    동일한 패스워드라고 해도 매번 다른 결과로 만들어 지기 때문에 정보를 안전하게 보관
    (무차별 대입 공격 방지에 효과적)
    */
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        log.info("filter chain......");
        /* 무상태 API를 위한 설정 */
        httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
           httpSecurityFormLoginConfigurer.disable();
        });

        // 같은 도메인에서 요청할 때 Frame 로드 될 수 있도록 허용 (dev h2-console)
        httpSecurity.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        httpSecurity.logout(config->config.disable());

        // csrf 비활성화 (무상태 Stateless )
        httpSecurity.csrf(config->{config.disable();});

        // 세션 생성 정책을 설정하는 부분 (Spring 에서 새로운 세션을 생성하지 않도록 설정, 기존 세션만 사용)
        httpSecurity.sessionManagement(sessionManagementConfigurer->{
           sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        /* JWT 토큰 감지 필터를 앞에 지정 */
        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
