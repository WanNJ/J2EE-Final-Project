// The recommended practice is to put every file under a root package.
package oleducation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// same as @Configuration @EnableAutoConfiguration @ComponentScan
@SpringBootApplication
public class Application {

    // Use "gradle bootRun" to run the application.
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
