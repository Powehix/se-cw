package Hospital.Controllers;

import Hospital.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkTimeController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private WorkTimeRepository workTimeRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/work-time")
	public String getEntityPage() {
		return "worktime";
	}

	@GetMapping("/work-time/print")
	public String getEntityPrintPage() {
		return "worktimeprint";
	}

	@PostMapping("/work-time/form")
	public @ResponseBody String loadEntityEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return workTimeGetAddFormHtml(id);
		}

		WorkTime entity = workTimeRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return workTimeGetEditFormHtml(entity);
	}

	@PostMapping("/work-time/filter")
	public @ResponseBody String loadWorkTimeFilterForm() {
		return workTimeGetFilterFormHtml();
	}

	@PostMapping("/work-time/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (workTimeRepository.existsById(id)) {
			workTimeRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/work-time/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute WorkTime workTimeData)
	{
		//----- Saving by converted to object received params -----
		workTimeRepository.save(workTimeData);
		return "Saved";
	}

	@PostMapping(path="/work-time/all")
	public @ResponseBody String getAllEmployeeWorkTime(Integer employeeId) {
		if (employeeId == null) {
			return "";
		}

		ArrayList<WorkTime> allEntities = new ArrayList<>();
		workTimeRepository.findAll().forEach(allEntities::add);

		Employee employee = employeeRepository.findById(employeeId).orElse(null);

		String requestResult = "";
		if (employee != null) {
			requestResult = "<span id=\"filter-name\">Work time of: " + employee.getName() + " " + employee.getSurname() + "</span><div id=\"time-history\">";

			for (WorkTime workTime : allEntities) {
				if (workTime.getIdEmployee() == employeeId)
					requestResult += workTimeToHtmlBlock(workTime);
			}

			requestResult += "</div>";
		}

		return requestResult;
	}

	@PostMapping(path="/work-time/sql")
	public @ResponseBody
	String executeSql (String query)
	{
		RowMapper<WorkTime> rm = (ResultSet result, int rowNum) -> {
			WorkTime object = new WorkTime();

			object.setIdWorkTime(result.getInt("id_work_time"));
			object.setIdTask(result.getInt("id_task"));
			object.setIdEmployee(result.getInt("id_employee"));
			object.setTime(result.getString("time"));
			object.setDate(result.getString("date"));

			return object;
		};

		List<WorkTime> clients = jdbcTemplate.query(query, new Object[]{}, rm);

		String requestResult = "";
		for (WorkTime oneClient : clients) {
			requestResult += workTimeToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/work-time/all/print")
	public @ResponseBody String getAllEmployeesWorkTime() {
		ArrayList<WorkTime> allEntities = new ArrayList<>();
		workTimeRepository.findAll().forEach(allEntities::add);

		String requestResult = "";
		for (WorkTime oneEntity : allEntities) {
			requestResult += workTimeToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	public String workTimeToHtmlBlock(WorkTime time) {
		Task task = taskRepository.findById(time.getIdTask()).orElse(null);

		return "<div id=\"work-time-" + time.getIdWorkTime() + "\" class=\"company-element entity\" onclick=\"openDataForm('"+ time.getIdWorkTime() +"')\">\n" +
				"<div class=\"time-info-container\">\n" +
				"<span>Task "+ task.getIdTask() +"</span>\n" +
				"<span>"+ task.getShortTitle() +"</span>\n" +
				"<span>Worked: " + time.getTime() + "</span>\n" +
				"<span>At: " + time.getDate() + "</span>\n" +
				"</div>\n" +
				"</div>";
	}

	public String workTimeToHtmlFullBlock(WorkTime time) {
		Task task = taskRepository.findById(time.getIdTask()).orElse(null);
		Employee employee = employeeRepository.findById(time.getIdEmployee()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Employee</span><span>" + employee.getName() + " " + employee.getSurname() +"</span></p>\n" +
				"<p><span>Task</span><span>["+ time.getIdTask() + "] " + task.getTitle() +"</span></p>\n" +
				"<p><span>Worked</span><span>"+ time.getTime() +"</span></p>\n" +
				"<p><span>Date</span><span>" + time.getDate() + "</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String workTimeGetEditFormHtml(WorkTime time) {
		return "<div id=\"timelog-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"timelog-form\" action=\"/work-time/save\" method=\"post\">\n" +
				"                            <p>Task " +
				"<select name=\"idTask\" form=\"timelog-form\">" +
				getTaskList(time.getIdTask()) +
				"</select></p>\n" +
				"                            <p>Employee " +
				"<select name=\"idEmployee\" form=\"timelog-form\">" +
				getEmployeeList(time.getIdEmployee()) +
				"</select></p>\n" +
				"                        <input type=\"hidden\" name=\"idWorkTime\" value=\""+ time.getIdWorkTime() +"\"/>\n" +
				"                        <p>Time <input type=\"text\" name=\"time\" class=\"data\" value=\""+ time.getTime() +"\"/></p>\n" +
				"                        <p>Date <input type=\"date\" name=\"date\" class=\"data\" value=\"" + time.getDate() + "\"/></p>\n" +
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('timelog-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ time.getIdWorkTime() +"')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/delete.png\" class=\"form-menu-image\">\n" +
				"                                <span>REMOVE</span>\n" +
				"                            </a>\n" +
				"                        </div>\n" +
				"                        <a onclick=\"hideForm('timelog-form');\" class=\"button-a form-cancel\">\n" +
				"                            <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                            <span>CANCEL</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                </div>";
	}

	public String workTimeGetAddFormHtml(Integer taskId) {
		return "<div id=\"timelog-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"timelog-form\" action=\"/work-time/save\" method=\"post\">\n" +
				"                            <p>Employee " +
				"<select name=\"idEmployee\" form=\"timelog-form\">" +
				getEmployeeList(null) +
				"</select></p>\n" +
				"                        <input type=\"hidden\" name=\"idTask\" value=\""+ taskId +"\"/>\n" +
				"                        <p>Time <input type=\"text\" name=\"time\" class=\"data\"/></p>\n" +
				"                        <p>Date <input type=\"date\" name=\"date\" class=\"data\"/></p>\n" +
				"                    </form>\n" +
				"                    <div class=\"form-navigation\">\n" +
				"                        <a onclick=\"submitDataForm('timelog-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                            <span>SAVE</span>\n" +
				"                        </a>\n" +
				"                        <a onclick=\"resetForm('timelog-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/repeat.png\" class=\"form-menu-image\">\n" +
				"                            <span>RESET</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                    <a onclick=\"hideForm('timelog-form');\" class=\"button-a form-cancel\">\n" +
				"                        <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                        <span>CANCEL</span>\n" +
				"                    </a>\n" +
				"                </div>\n" +
				"            </div>";
	}

	public String workTimeGetFilterFormHtml() {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/work-time/all\" method=\"post\">\n" +
				"<p>Employee: " +
				"<select name=\"employeeId\" form=\"add-entity-form\">" +
				getEmployeeList(null) +
				"</select></p>\n" +
				"                    </form>\n" +
				"                    <div class=\"form-navigation\">\n" +
				"                        <a onclick=\"submitFilter('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                            <span>SELECT</span>\n" +
				"                        </a>\n" +
				"                    <a onclick=\"hideForm('add-entity-form');\" class=\"button-a\">\n" +
				"                        <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                        <span>CANCEL</span>\n" +
				"                    </a>\n" +
				"                    </div>\n" +

				"                </div>\n" +
				"            </div>";
	}

	public String getEmployeeList(Integer employeeId) {
		ArrayList<Employee> employees = new ArrayList<>();
		employeeRepository.findAll().forEach(employees::add);

		String selectTagOptions = "";
		for (Employee employee : employees) {
			selectTagOptions += employee.getOptionHtml((employee.getIdEmployee().equals(employeeId)));
		}

		return selectTagOptions;
	}

	public String getTaskList(Integer taskId) {
		ArrayList<Task> tasks = new ArrayList<>();
		taskRepository.findAll().forEach(tasks::add);

		String selectTagOptions = "";
		for (Task task : tasks) {
			selectTagOptions += task.getOptionHtml((task.getIdTask().equals(taskId)));
		}

		return selectTagOptions;
	}
}