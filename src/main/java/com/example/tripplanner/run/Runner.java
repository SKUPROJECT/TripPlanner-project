package com.example.tripplanner.run;

import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
@Log4j2
class Runner implements ApplicationRunner {

    @Value("${boot.mode}")
    private String mode;

    @Autowired
    DataSource dataSource;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[시스템 모드 : {}]", mode);

        // hikaricp 기본 커넥션 풀
        try(Connection connection = dataSource.getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();

            log.info("[DB Product Name : {}]", metaData.getDatabaseProductName());
            log.info("[DB URL : {}]", metaData.getURL());
            log.info("[DB Username : {}]", metaData.getUserName());
        }

        if("DevMode".equals(mode)){ /* 개발환경일 때 세팅 */
            for (int i = 1; i <= 10; i++) {
                MemberEntity memberEntity = MemberEntity.builder()
                        .id("user"+i)
                        .pw(passwordEncoder.encode("1111"))
                        .name("USER"+i)
                        .email("user"+i+"@aaa.com")
                        .role(i <= 5 ? "USER" : "ADMIN")
                        .build();

                memberRepository.save(memberEntity);
            }
        }
    }
}
