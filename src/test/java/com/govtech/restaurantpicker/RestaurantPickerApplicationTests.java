package com.govtech.restaurantpicker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class RestaurantPickerApplicationTests {

	@Test
	void contextLoads() {
		// Verifies that the Spring application context loads successfully
	}

}
