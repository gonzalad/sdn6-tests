package com.example.sdn6;

import com.example.sdn6.container.Neo4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class Sdn6ApplicationTests {

	@Test
	void contextLoads() {
	}

	@DynamicPropertySource
	static void neo4jProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.neo4j.uri", () -> Neo4j.database().getBoltUrl());
	}

}
