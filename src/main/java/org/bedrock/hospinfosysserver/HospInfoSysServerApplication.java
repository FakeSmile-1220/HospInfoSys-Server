package org.bedrock.hospinfosysserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HospInfoSysServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospInfoSysServerApplication.class, args);
    }

}
