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
public class DoctorController {

	@Autowired
	public DoctorRepository doctorRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/doctor")
	public String getEntityPage() {
		return "doctor";
	}

	@GetMapping("/doctor/print")
	public String getEntityPrintPage() {
		return "doctorprint";
	}

	@PostMapping("/doctor/form")
	public @ResponseBody String loadEntityEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return doctorGetAddFormHtml();
		}

		Doctor entity = doctorRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return doctorGetEditFormHtml(entity);
	}

	@PostMapping("/doctor/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (doctorRepository.existsById(id)) {
			doctorRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/doctor/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute Doctor newDoctor)
	{
		//----- Saving by converted to object received params -----
		doctorRepository.save(newDoctor);
		return "Saved";
	}

	@PostMapping(path="/doctor/all")
	public @ResponseBody String getAllUsers() {
		ArrayList<Doctor> allEntities = new ArrayList<>();
		doctorRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Doctor oneEntity : allEntities) {
			requestResult += doctorToHtmlBlock(oneEntity);
		}

		return requestResult;
	}

	@PostMapping(path="/doctor/sql")
	public @ResponseBody
	String executeSql (String query)
	{
		RowMapper<Doctor> rm = (ResultSet result, int rowNum) -> {
			Doctor object = new Doctor();

			object.setIdDoctor(result.getInt("id_doctor"));
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
			requestResult += doctorToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/doctor/all/print")
	public @ResponseBody String getAllUsersToPrint() {
		ArrayList<Doctor> allEntities = new ArrayList<>();
		doctorRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Doctor oneEntity : allEntities) {
			requestResult += doctorToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	public String doctorToHtmlBlock(Doctor doctor) {
		Position position = positionRepository.findById(doctor.getIdPosition()).orElse(null);

		return "<div class=\"company-element entity\" onclick=\"openDataForm('"+ doctor.getIdDoctor() +"')\">\n" +
				"<div class=\"company-image-container\">\n" +
				"<img src=\"/images/doctor.png\" alt=\"Doctor image\">\n" +
				"</div>\n" +
				"<div class=\"company-info-container\">\n" +
				"<span>" + doctor.getName() + " " + doctor.getSurname() +"</span>\n" +
				"<span>" + position.getTitle() + "</span>\n" +
				"</div>\n" +
				"</div>";
	}

	public String doctorToHtmlFullBlock(Doctor doctor) {
		Position position = positionRepository.findById(doctor.getIdPosition()).orElse(null);
		Department department = departmentRepository.findById(doctor.getIdDepartment()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Doctor</span><span>" + doctor.getName() + " " + doctor.getSurname() +"</span></p>\n" +
				"<p><span>Position</span><span>" + position.getTitle() + "</span></p>\n" +
				"<p><span>Department</span><span>"+ department.getTitle() +"</span></p>\n" +
				"<p><span>Email</span><span>"+ doctor.getEmail() +"</span></p>\n" +
				"<p><span>Phone</span><span>" + doctor.getPhone() + "</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String doctorGetEditFormHtml(Doctor doctor) {
		return "<div id=\"form-edit-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"edit-entity-form\" action=\"/doctor/save\" method=\"post\">\n" +
				"                            <p>Name: <input type=\"text\" name=\"name\" class=\"data\" value='"+ doctor.getName() +"'/></p>\n" +
				"                            <p>Surname: <input type=\"text\" name=\"surname\" class=\"data\" value='"+ doctor.getSurname() + "'/></p>\n" +
				"                            <p>Position: " +
				"<select name=\"idPosition\" form=\"edit-entity-form\">" +
				getPositionList(doctor.getIdPosition()) +
				"</select></p>\n" +
				"                            <p>Department: " +
				"<select name=\"idTeam\" form=\"edit-entity-form\">" +
				getTeamList(doctor.getIdDepartment()) +
				"</select></p>\n" +
				"                            <p>Phone: <input type=\"text\" name=\"phone\" class=\"data\" value='"+ doctor.getPhone() + "'/></p>\n" +
				"                            <p>Email: <input type=\"text\" name=\"email\" class=\"data\" value='"+ doctor.getEmail() + "'/></p>\n" +
				"                            <input type=\"hidden\" name=\"idDoctor\" class=\"data\" value='"+ doctor.getIdDoctor() +"'/>"+
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('edit-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ doctor.getIdDoctor() +"')\" class=\"button-a\">\n" +
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

	public String getPositionList(Integer doctorPositionId) {
		ArrayList<Position> positions = new ArrayList<>();
		positionRepository.findAll().forEach(positions::add);

		String selectTagOptions = "";
		for (Position position : positions) {
			selectTagOptions += position.getOptionHtml((position.getIdPosition().equals(doctorPositionId)));
		}

		return selectTagOptions;
	}

	public String getTeamList(Integer doctorDepartmentId) {
		ArrayList<Department> departments = new ArrayList<>();
		departmentRepository.findAll().forEach(departments::add);

		String selectTagOptions = "";
		for (Department department : departments) {
			selectTagOptions += department.getOptionHtml((department.getIdDepartment().equals(doctorDepartmentId)));
		}

		return selectTagOptions;
	}

	public String doctorGetAddFormHtml() {
		return "<div id=\"form-add-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"form-container\">\n" +
				"                    <form id=\"add-entity-form\" action=\"/doctor/save\" method=\"post\">\n" +
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