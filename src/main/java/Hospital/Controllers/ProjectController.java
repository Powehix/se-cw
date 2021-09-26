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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProjectController {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/project")
	public String getEntityPage() {
		return "project";
	}

	@GetMapping("/project/board")
	public String getProjectBoardPage() {
		return "projectboard";
	}

	@GetMapping("/project/print")
	public String getEntityPrintPage() {
		return "projectprint";
	}

	@GetMapping("/task/print")
	public String getAllTaskPrintPage() {
		return "taskprint";
	}

	public String getClientList(Integer clientId) {
		ArrayList<Client> clients = new ArrayList<>();
		clientRepository.findAll().forEach(clients::add);

		String selectTagOptions = "";
		for (Client client : clients) {
			selectTagOptions += client.getOptionHtml((client.getIdClient().equals(clientId)));
		}

		return selectTagOptions;
	}

	public String getTeamList(Integer employeeTeamId) {
		ArrayList<Department> departments = new ArrayList<>();
		departmentRepository.findAll().forEach(departments::add);

		String selectTagOptions = "";
		for (Department department : departments) {
			selectTagOptions += department.getOptionHtml((department.getIdDepartment().equals(employeeTeamId)));
		}

		return selectTagOptions;
	}

	public String projectGetAddFormHtml() {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/project/save\" method=\"post\">\n" +
				"<p>Title: <input type=\"text\" name=\"title\" class=\"data\" value=''/></p>\n" +
				"                            <p>Client: " +
				"<select name=\"idClient\" form=\"add-entity-form\">" +
				getClientList(0) +
				"</select></p>\n" +
				"                            <p>Department: " +
				"<select name=\"idTeam\" form=\"add-entity-form\">" +
				getTeamList(0) +
				"</select></p>\n" +
				"                            <p>Start date: <input type=\"date\" name=\"startDate\" class=\"data\" value=''/></p>\n" +
				"                            <p>Deadline date: <input type=\"date\" name=\"deadlineDate\" class=\"data\" value=''/></p>\n" +
				"                    </form>\n" +
				"                    <div class=\"form-navigation\">\n" +
				"                        <a onclick=\"submitDataForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                            <span>SAVE</span>\n" +
				"                        </a>\n" +
				"                        <a onclick=\"resetForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/repeat.png\" class=\"form-menu-image\">\n" +
				"                            <span>RESET</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                    <a onclick=\"hideForm('add-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                        <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                        <span>CANCEL</span>\n" +
				"                    </a>\n" +
				"                </div>\n" +
				"            </div>";
	}

	public String taskGetAddFormHtml(Integer projectId) {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"task-form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/task/save\" method=\"post\">\n" +
				"<div class=\"left-part\">" +
				"<p>Title: <input type=\"text\" name=\"title\" class=\"data\" value=''/></p>\n" +
				"<p>Description <textarea name=\"description\" form=\"add-entity-form\" class=\"data large-input\"></textarea></p>\n" +
				"</div>" +
				"<div class=\"right-part\">" +
				"<select name=\"idStatus\" form=\"add-entity-form\">test2" +
//				getStatusList(0) +
				"</select>" +
				"<p>Assignee " +
				"<select name=\"idEmployeeAssignee\" form=\"add-entity-form\">" +
				getEmployeeList(0) +
				"</select></p>\n" +
				"<p>Reporter " +
				"<select name=\"idEmployeeReporter\" form=\"add-entity-form\">" +
				getEmployeeList(0) +
				"</select></p>\n" +
				"<p>Estimate time <input type=\"text\" data-time=\"true\" name=\"estimatedTime\" class=\"data\" value=''/></p>\n" +
				"                    <div class=\"task-form-navigation\">\n" +
				"                        <a onclick=\"submitDataForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                            <span>SAVE</span>\n" +
				"                        </a>\n" +
				"                        <a onclick=\"resetForm('add-entity-form')\" class=\"button-a\">\n" +
				"                            <img src=\"/images/repeat.png\" class=\"form-menu-image\">\n" +
				"                            <span>RESET</span>\n" +
				"                        </a>\n" +
				"                    <a onclick=\"hideForm('add-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                        <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                        <span>CANCEL</span>\n" +
				"                    </a>\n" +
				"                    </div>\n" +
				"</div>" +
				"<input type=\"hidden\" name=\"idProject\" value='"+ projectId +"'/>"+
				"                    </form>\n" +
				"                </div>\n" +
				"            </div>";
	}

	public String getEmployeeList(Integer employeeId) {
		ArrayList<Doctor> employees = new ArrayList<>();
		doctorRepository.findAll().forEach(employees::add);

		String selectTagOptions = "";
		for (Doctor employee : employees) {
			selectTagOptions += employee.getOptionHtml((employee.getIdEmployee().equals(employeeId)));
		}

		return selectTagOptions;
	}
}