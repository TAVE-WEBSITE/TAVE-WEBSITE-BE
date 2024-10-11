package com.tave.tavewebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaveWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaveWebsiteApplication.class, args);
    }

}
