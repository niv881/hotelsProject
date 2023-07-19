package dev.nhason;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class HotelsProjectApplication{
    public static void main(String[] args) {
        SpringApplication.run(HotelsProjectApplication.class, args);
    }

}
