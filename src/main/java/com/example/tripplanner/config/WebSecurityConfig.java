package com.example.tripplanner.config;

import com.example.tripplanner.member.service.MemberDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final MemberDetailService memberService;

    @Bean
    public WebSecurityCustomizer configure(){

    return (web) -> web.ignoring()
            .requestMatchers(toH2Console())
            .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()  // CSRF 비활성화
                .cors(Customizer.withDefaults())  // CORS 기본 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .permitAll()  // 로그인 페이지는 모든 사용자에게 공개
                                .defaultSuccessUrl("/",true)  // 로그인 성공 후 기본 리디렉션 URL
                )
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/login","/signup").permitAll()  // /login 경로에 대한 접근 허용
                                .anyRequest().permitAll())
                .logout()
                .permitAll();

        return http.build();

        //.formLogin((formLogin) ->
        //                formLogin.loginPage("/login").permitAll().defaultSuccessUrl("/")
        // 위에 권한이 접근허용이 되지않은 비회원이 해당 페이지 들어갈 경우 /login 페이지로 이동

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
           BCryptPasswordEncoder bCryptPasswordEncoder, MemberDetailService memberDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
