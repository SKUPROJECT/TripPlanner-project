package com.example.tripplanner.run;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
    }
}
