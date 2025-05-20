package ong.desi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("ong.desi.entity")
@EnableJpaRepositories("ong.desi.repository")

public class DesiTp3Application {	
	
	public static void main(String[] args) {
		SpringApplication.run(DesiTp3Application.class, args);
		
	
	}

}
