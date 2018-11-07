package test.org.iii.ideas.foodsafety.rest;

import org.iii.ideas.foodsafety.rest.Schedule_Service;
import org.junit.After;
import org.junit.Before;

public class Schedule_ServiceTest {
	Schedule_Service ss;

	@Before
	public void setUp() throws Exception {
		ss = new Schedule_Service();
	}

	@After
	public void tearDown() throws Exception {
		ss = null;
	}

	// @Test
	// public void test_dailyFoodSaftyFromGmail() {
	// assertEquals(true, ss.dailyFoodSaftyFromGmail());
	// }

}
