package com.italloramon.apicepcorreios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApiCepCorreiosApplication {
    private static ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
        SpringApplication.run(ApiCepCorreiosApplication.class, args);
    }

    public static void close(int code) {
        SpringApplication.exit(ctx, () -> code);
    }

}
