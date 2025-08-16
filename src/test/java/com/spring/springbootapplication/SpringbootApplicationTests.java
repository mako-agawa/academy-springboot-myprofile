package com.spring.springbootapplication;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.example.demo.dao") // 実際のパッケージに変更
@SpringBootApplication
class SpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

}
