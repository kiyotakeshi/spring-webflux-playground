package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebfluxTutorialApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebfluxTutorialApplication.class, args);
        GreetingClient greetingClient = context.getBean(GreetingClient.class);
        System.out.println(">> message = " + greetingClient.getMessage().block());
    }
}
