package qubiqule.cheqr.cheQR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheQrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheQrApplication.class, args);
	}

}
