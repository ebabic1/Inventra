package ba.unsa.etf.nwt.inventra.auth_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {

			Dotenv dotenv = Dotenv.configure().load();

			dotenv.entries().forEach(entry ->
					System.setProperty(entry.getKey(), entry.getValue())
			);

			SpringApplication.run(AuthServiceApplication.class, args);
	}

}
