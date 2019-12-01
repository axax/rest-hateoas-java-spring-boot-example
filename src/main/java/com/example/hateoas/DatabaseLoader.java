package com.example.hateoas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Pre-load some data using a Spring Boot {@link CommandLineRunner}.
 */
@Component
class DatabaseLoader {

	/**
	 * Use Spring to inject a {@link EmployeeRepository} that can then load data. Since this will run only after the app
	 * is operational, the database will be up.
	 *
	 * @param repository
	 */
	@Bean
	CommandLineRunner init(EmployeeRepository repository) {

		return args -> {
			repository.save(new Employee("Jon", "Doe", "Salesman"));
			repository.save(new Employee("Richard", "Roe", "Software engineer"));
			repository.save(new Employee("Janie", "Doe", "Designer"));
		};
	}

}
