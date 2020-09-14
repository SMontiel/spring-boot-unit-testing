package com.example.demo;

import java.util.Objects;

/**
 * @author Salvador Montiel
 */
public class Staff {
	private int id;
	private String name;
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Staff staff = (Staff) o;
		return id == staff.id &&
				Objects.equals(name, staff.name) &&
				Objects.equals(email, staff.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, email);
	}

	@Override
	public String toString() {
		return "Staff{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
