package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan({ "controller", "rest_repo" })
@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean(name="props")
    public Properties getBdProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
            System.out.println("bd.config found");
        }
        catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        return props;
    }
}
