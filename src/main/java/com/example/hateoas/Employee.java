package com.example.hateoas;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Domain object representing a company employee. Project Lombok keeps actual code at a minimum. {@code @Data} -
 * Generates getters, setters, toString, hash, and equals functions {@code @Entity} - JPA annotation to flag this class
 * for DB persistence {@code @NoArgsConstructor} - Create a constructor with no args to support JPA
 * {@code @AllArgsConstructor} - Create a constructor with all args to support testing
 * {@code @JsonIgnoreProperties(ignoreUnknow=true)} When converting JSON to Java, ignore any unrecognized attributes.
 * This is critical for REST because it encourages adding new fields in later versions that won't break. It also allows
 * things like _links to be ignore as well, meaning HAL documents can be fetched and later posted to the server without
 * adjustment.
 *
 */
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
class Employee {

	@Id @GeneratedValue private Long id;
	private String firstName;
	private String lastName;
	private String role;

	/**
	 * Useful constructor when id is not yet known.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param role
	 */
	Employee(String firstName, String lastName, String role) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
}
