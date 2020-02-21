package com.avc.mis.beta;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.Suppliers;
import com.avc.mis.beta.dataobjects.Supplier;

@SpringBootApplication
public class BetaApplication {
	
	
	@Autowired
	Suppliers suppliers;

	public static void main(String[] args) {
		SpringApplication.run(BetaApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			/*
			 * Supplier supplier = new Supplier(); supplier.setName("medium supplier1");
			 * suppliers.addSupplier(supplier);
			 */

		};
	}

}
