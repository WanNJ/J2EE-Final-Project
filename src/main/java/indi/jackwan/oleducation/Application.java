// The recommended practice is to put every file under a root package.
package indi.jackwan.oleducation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// same as @Configuration @EnableAutoConfiguration @ComponentScan
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    // Use "gradle bootRun" to run the application.
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
