package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.Assert.assertEquals;

@WebMvcTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		assertEquals(4, 2 + 2);
	}
}
