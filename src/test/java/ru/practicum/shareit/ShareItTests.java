package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItTests {

    // static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1");

    // @BeforeAll
    // static void beforeAll() {
    //     postgres.start();
    // }

    // @DynamicPropertySource
    // static void configureProperties(DynamicPropertyRegistry registry) {
    //     registry.add("spring.datasource.url", postgres::getJdbcUrl);
    //     registry.add("spring.datasource.username", postgres::getUsername);
    //     registry.add("spring.datasource.password", postgres::getPassword);
    // }

    @Test
    void contextLoads() {

    }
}
