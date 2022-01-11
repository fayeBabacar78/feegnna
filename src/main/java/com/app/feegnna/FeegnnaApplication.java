package com.app.feegnna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FeegnnaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeegnnaApplication.class, args);
    }

}
