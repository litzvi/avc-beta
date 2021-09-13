package com.avc.mis.beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.security.AuditorAwareImpl;

/**
 * Version: 20.9
 * issues 571, 574, 575, 578, 579, 584, 585
 * 
 * @author zvi
 *
 */
@SpringBootApplication
//@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BetaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BetaApplication.class, args);
	}

	@Bean
    public AuditorAware<UserEntity> auditorAware() {
        return new AuditorAwareImpl();
    }

}
