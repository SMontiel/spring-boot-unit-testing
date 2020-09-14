package com.example.demo;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Salvador Montiel
 */

@RunWith(SpringRunner.class)
@AutoConfigureEmbeddedDatabase
@ContextConfiguration(classes = { StaffDao.class })
@DataJdbcTest
public class StaffDaoTest {
	@Autowired private StaffDao staffDao;

	/*@BeforeEach
	void setUp() {}

	@AfterEach
	void tearDown() {}*/

	@Test
	@FlywayTest(locationsForMigrate = { "db/staff/insert" })
	public void checkRowCountIsOne() throws Exception {
		assertEquals(staffDao.integer(staffDao.getCountQuery()), 1);
	}

	@Test
	@FlywayTest
	public void createOneStaffMember() {
		int actualId = 1;

		Staff s = new Staff();
		s.setName("Test name");
		s.setEmail("Email");
		int foundId = staffDao.create(s);

		s.setId(1);
		List<Staff> actualList = Collections.singletonList(s);

		assertEquals(foundId, actualId);
		assertEquals(staffDao.obtenerLista(), actualList);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "db/staff/insert" })
	public void updateStaffMember() throws Exception {
		Staff staff = staffDao.obtenerPorId(1);

		assertEquals(staff.getName(), "John Doe");

		staff.setName("Juan Pérez");
		staff.setEmail("juan.perez@hotmail.com");
		staffDao.update(staff);

		Staff actualStaff = staffDao.obtenerPorId(1);
		assertEquals("Juan Pérez", actualStaff.getName());
		assertEquals("juan.perez@hotmail.com", actualStaff.getEmail());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "db/staff/insert" })
	public void removeStaffMember() throws Exception {
		Staff staff = staffDao.obtenerPorId(1);

		assertEquals(staff.getName(), "John Doe");

		staffDao.remove(1);

		assertNull(staffDao.obtenerPorId(1));
	}

	@Test
	@FlywayTest
	public void checkIfStaffRowCountQueryIsValid() {
		// given
		String actual = "select count(e.*) as count from public.staff as e";

		// when
		String found = staffDao.getCountQuery().getSql();

		// then
		assertEquals(found, actual);
	}
}