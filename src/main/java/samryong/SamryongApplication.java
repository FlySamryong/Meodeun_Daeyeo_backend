package samryong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SamryongApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamryongApplication.class, args);
    }
}
