package com.example.tripplanner.member.security.filter;

import com.example.tripplanner.member.security.auth.CustomUserPrincipal;
import com.example.tripplanner.member.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        if(request.getServletPath().startsWith("/api/v1/token")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWTCheckFilter doFilter........");
        log.info("requestURI:"+request.getRequestURI());
        String headerStr = request.getHeader("Authorization");
        log.info("headerStr:"+headerStr);

        if(headerStr == null || !headerStr.startsWith("Bearer ")){
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));

            return;
        }

        String accessToken = headerStr.substring(7);

        try{
            java.util.Map<String, Object> tokenMap = jwtUtil.validateToken(accessToken);
            // 토큰 검증 결과
            log.info("tokenMap: " + tokenMap);

            String mid = tokenMap.get("mid").toString();
            String[] roles = tokenMap.get("role").toString().split(",");

            // 토큰 검증 결과를 이용해서 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    new CustomUserPrincipal(mid),null, Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toList())
            );

            // Authentication 객체 저장 (사용자의 인증을 기록)
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);


        } catch(Exception e){
            handleException(response, e);
        }

    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage()+"\"})");

    }
}
