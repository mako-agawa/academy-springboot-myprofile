package com.spring.springbootapplication;

import org.flywaydb.core.Flyway;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.spring.springbootapplication.dao")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

        // ★ 一時的に追加（repair & migrate 実行）
    @Bean
    CommandLineRunner flywayRepair(Flyway flyway) {
        return args -> {
            flyway.repair();
            flyway.migrate();
        };
    }

}