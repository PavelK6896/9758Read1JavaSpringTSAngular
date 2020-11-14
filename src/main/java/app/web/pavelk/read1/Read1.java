package app.web.pavelk.read1;


import app.web.pavelk.read1.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // асинхроная обработка аннотаций
@Import(SwaggerConfiguration.class)
public class Read1 {

    public static void main(String[] args) {
        SpringApplication.run(Read1.class, args);
    }

}
