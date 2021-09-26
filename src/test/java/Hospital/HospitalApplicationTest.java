package Hospital;

import Hospital.Model.Doctor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HospitalApplicationTest {
	@Test
	public void testGetShortTitle() {
//		Task newTask = new Task();
//
//		newTask.setTitle("Short task title");
//		assertEquals(newTask.getShortTitle(), "Short task title", "Task title is enough short to be visible completely!");
//
//		newTask.setTitle("Long task title that will not be shown");
//		assertNotEquals(newTask.getShortTitle(), "Long task title that will not be shown", "Task title is too long to be presented completely!");
//
//		newTask.setTitle("");
//		assertEquals(newTask.getShortTitle(), "", "Task title should be empty!");
	}

	@Test
	public void testGetOptionHtmlSelected() {
		/*Task newTask = new Task();
		newTask.setTitle("Some title");
		newTask.setIdTask(1);

		String result = newTask.getOptionHtml(true);
		assertTrue(result.contains("selected"), "It is case when task should be selected in <select> tag, but it doesn't contain 'selected' attribute");

		result = newTask.getOptionHtml(false);
		assertFalse(result.contains("selected"), "It is case when task should not be selected in <select> tag, but it contains 'selected' attribute");*/
	}

	@Test
	public void testGetEmployeeName() {
		Doctor newEmployee = new Doctor();
		newEmployee.setName("Ilja");

		assertEquals(newEmployee.getName(), "Ilja", "Class returned not the same name, as was specified: Ilja");
		assertNotEquals(newEmployee.getName(), "Not Ilja", "Class returned the same name, but it shouldn't");
	}
}
