package com.example.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 */
@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	/**
	 * Look up all employees, and transform them into a REST collection resource. Then return them through Spring Web's
	 * {@link ResponseEntity} fluent API.
	 */
	@GetMapping("/employees")
	ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {

		List<EntityModel<Employee>> employees = StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(employee -> new EntityModel<>(employee, //
						linkTo(methodOn(EmployeeController.class).findOne(employee.getId())).withSelfRel(), //
						linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"))) //
				.collect(Collectors.toList());

		return ResponseEntity.ok( //
				new CollectionModel<>(employees, //
						linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel()));
	}

	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee employee) {

		try {
			Employee savedEmployee = repository.save(employee);

			EntityModel<Employee> employeeResource = new EntityModel<>(savedEmployee, //
					linkTo(methodOn(EmployeeController.class).findOne(savedEmployee.getId())).withSelfRel());

			return ResponseEntity //
					.created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref())) //
					.body(employeeResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + employee);
		}
	}

	/**
	 * Look up a single {@link Employee} and transform it into a REST resource. Then return it through Spring Web's
	 * {@link ResponseEntity} fluent API.
	 *
	 * @param id
	 */
	@GetMapping("/employees/{id}")
	ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {

		return repository.findById(id) //
				.map(employee -> new EntityModel<>(employee, //
						linkTo(methodOn(EmployeeController.class).findOne(employee.getId())).withSelfRel(), //
						linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"))) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Update existing employee then return a Location header.
	 * 
	 * @param employee
	 * @param id
	 * @return
	 */
	@PutMapping("/employees/{id}")
	ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable long id) {

		Employee employeeToUpdate = employee;
		employeeToUpdate.setId(id);
		repository.save(employeeToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(EmployeeController.class).findOne(id)).withSelfRel();

		try {
			return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + employeeToUpdate);
		}
	}

	/**
	 * Delete existing employee then return a Location header.
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable long id) {

		Optional<Employee> employeeOptional = repository.findById(id);

		if( employeeOptional.isPresent()){
			repository.delete(employeeOptional.get());

			return employeeOptional.map(emp -> new EntityModel<>(emp, //
							linkTo(methodOn(EmployeeController.class).findOne(emp.getId())).withSelfRel(), //
							linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"))) //
					.map(ResponseEntity::ok) //
					.orElse(ResponseEntity.notFound().build());

		}else {
			return ResponseEntity.notFound().build();
		}
	}

}
