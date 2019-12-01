package com.example.hateoas;

import org.springframework.data.repository.CrudRepository;

/**
 * A simple Spring Data {@link CrudRepository} for storing {@link Employee}s.
 *
 * @author Greg Turnquist
 */
interface EmployeeRepository extends CrudRepository<Employee, Long> {}
