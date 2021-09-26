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
public class EmployeeController {

	@Autowired
	public DoctorRepository doctorRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/employee")
	public String getEntityPage() {
		return "employee";
	}

	@GetMapping("/employee/print")
	public String getEntityPrintPage() {
		return "employeeprint";
	}

	@PostMapping("/employee/form")
	public @ResponseBody String loadEntityEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return employeeGetAddFormHtml();
		}

		Doctor entity = doctorRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return employeeGetEditFormHtml(entity);
	}

	@PostMapping("/employee/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (doctorRepository.existsById(id)) {
			doctorRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/employee/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute Doctor newEmployee)
	{
		//----- Saving by converted to object received params -----
		doctorRepository.save(newEmployee);
		return "Saved";
	}

	@PostMapping(path="/employee/all")
	public @ResponseBody String getAllUsers() {
		ArrayList<Doctor> allEntities = new ArrayList<>();
		doctorRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Doctor oneEntity : allEntities) {
			requestResult += employeeToHtmlBlock(oneEntity);
		}

		return requestResult;
	}

	@PostMapping(path="/employee/sql")
	public @ResponseBody
	String executeSql (String query)
	{
		RowMapper<Doctor> rm = (ResultSet result, int rowNum) -> {
			Doctor object = new Doctor();

			object.setIdEmployee(result.getInt("id_employee"));
			object.setIdDepartment(result.getInt("id_department"));
			object.setIdPosition(result.getInt("id_position"));
			object.setName(result.getString("name"));
			object.setSurname(result.getString("surname"));
			object.setEmail(result.getString("email"));
			object.setPhone(result.getString("phone"));

			return object;
		};

		List<Doctor> clients = jdbcTemplate.query(query, new Object[]{}, rm);

		String requestResult = "";
		for (Doctor oneClient : clients) {
			requestResult += employeeToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/employee/all/print")
	public @ResponseBody String getAllUsersToPrint() {
		ArrayList<Doctor> allEntities = new ArrayList<>();
		doctorRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Doctor oneEntity : allEntities) {
			requestResult += employeeToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	public String employeeToHtmlBlock(Doctor employee) {
		Position position = positionRepository.findById(employee.getIdPosition()).orElse(null);

		return "<div class=\"company-element entity\" onclick=\"openDataForm('"+ employee.getIdEmployee() +"')\">\n" +
				"<div class=\"company-image-container\">\n" +
				"<img src=\"/images/employee.png\" alt=\"Doctor image\">\n" +
				"</div>\n" +
				"<div class=\"company-info-container\">\n" +
				"<span>" + employee.getName() + " " + employee.getSurname() +"</span>\n" +
				"<span>" + position.getTitle() + "</span>\n" +
				"</div>\n" +
				"</div>";
	}

	public String employeeToHtmlFullBlock(Doctor employee) {
		Position position = positionRepository.findById(employee.getIdPosition()).orElse(null);
		Department department = departmentRepository.findById(employee.getIdDepartment()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Doctor</span><span>" + employee.getName() + " " + employee.getSurname() +"</span></p>\n" +
				"<p><span>Position</span><span>" + position.getTitle() + "</span></p>\n" +
				"<p><span>Department</span><span>"+ department.getTitle() +"</span></p>\n" +
				"<p><span>Email</span><span>"+ employee.getEmail() +"</span></p>\n" +
				"<p><span>Phone</span><span>" + employee.getPhone() + "</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String employeeGetEditFormHtml(Doctor employee) {
		return "<div id=\"form-edit-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"edit-entity-form\" action=\"/employee/save\" method=\"post\">\n" +
				"                            <p>Name: <input type=\"text\" name=\"name\" class=\"data\" value='"+ employee.getName() +"'/></p>\n" +
				"                            <p>Surname: <input type=\"text\" name=\"surname\" class=\"data\" value='"+ employee.getSurname() + "'/></p>\n" +
				"                            <p>Position: " +
				"<select name=\"idPosition\" form=\"edit-entity-form\">" +
				getPositionList(employee.getIdPosition()) +
				"</select></p>\n" +
				"                            <p>Department: " +
				"<select name=\"idTeam\" form=\"edit-entity-form\">" +
				getTeamList(employee.getIdDepartment()) +
				"</select></p>\n" +
				"                            <p>Phone: <input type=\"text\" name=\"phone\" class=\"data\" value='"+ employee.getPhone() + "'/></p>\n" +
				"                            <p>Email: <input type=\"text\" name=\"email\" class=\"data\" value='"+ employee.getEmail() + "'/></p>\n" +
				"                            <input type=\"hidden\" name=\"idEmployee\" class=\"data\" value='"+ employee.getIdEmployee() +"'/>"+
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('edit-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ employee.getIdEmployee() +"')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/delete.png\" class=\"form-menu-image\">\n" +
				"                                <span>REMOVE</span>\n" +
				"                            </a>\n" +
				"                        </div>\n" +
				"                        <a onclick=\"hideForm('edit-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                            <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                            <span>CANCEL</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                </div>";
	}

	public String getPositionList(Integer employeePositionId) {
		ArrayList<Position> positions = new ArrayList<>();
		positionRepository.findAll().forEach(positions::add);

		String selectTagOptions = "";
		for (Position position : positions) {
			selectTagOptions += position.getOptionHtml((position.getIdPosition().equals(employeePositionId)));
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

	public String employeeGetAddFormHtml() {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/employee/save\" method=\"post\">\n" +
				"                        <p>Name: <input type=\"text\" name=\"name\" class=\"data\"/></p>\n" +
				"                        <p>Surname: <input type=\"text\" name=\"surname\" class=\"data\"/></p>\n" +
				"<p>Position: " +
				"<select name=\"idPosition\" form=\"add-entity-form\">" +
				getPositionList(null) +
				"</select></p>\n" +
				"                            <p>Department: " +
				"<select name=\"idTeam\" form=\"add-entity-form\">" +
				getTeamList(null) +
				"</select></p>\n" +
				"                        <p>Email: <input type=\"text\" name=\"email\" class=\"data\"/></p>\n" +
				"                        <p>Phone: <input type=\"text\" name=\"phone\" class=\"data\"/></p>\n" +
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
}