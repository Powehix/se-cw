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
public class VisitTimeController {
	@Autowired
	private VisitTimeRepository visitTimeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/departments/all")
	public @ResponseBody String loadAllDepartments() {
		ArrayList<Department> allEntities = new ArrayList<>();
		departmentRepository.findAll().forEach(allEntities::add);

		String result = "<option value=\"\"></option>";
		for (int i = 0; i < allEntities.size(); i++) {
			result += allEntities.get(i).getOptionHtml(false);
		}

		return result;
	}

	@PostMapping("/positions/all")
	public @ResponseBody String loadAllPositions(Integer departmentId) {
		ArrayList<Position> allEntities = new ArrayList<>();
		if (departmentId == null) {
			positionRepository.findAll().forEach(allEntities::add);
		} else {
			Department department = departmentRepository.findById(departmentId).orElse(null);
			allEntities = positionRepository.findByDepartment(department);
		}
		String result = "<option value=\"\"></option>";
		for (int i = 0; i < allEntities.size(); i++) {
			result += allEntities.get(i).getOptionHtml(false);
		}

		return result;
	}

	@PostMapping("/doctors/all")
	public @ResponseBody String loadAllDoctors(Integer departmentId, Integer positionId) {
		ArrayList<Position> allPositions;
		ArrayList<Doctor> allEntities = new ArrayList<>();
		if (departmentId != null) {
			Department department = departmentRepository.findById(departmentId).orElse(null);
			allPositions = positionRepository.findByDepartment(department);
			for (int i = 0; i < allPositions.size(); i++) {
				doctorRepository.findByPosition(allPositions.get(i)).forEach(allEntities::add);
			}
		} else {
			if (positionId != null) {
				Position position = positionRepository.findById(positionId).orElse(null);
				doctorRepository.findByPosition(position).forEach(allEntities::add);
			} else {
				doctorRepository.findAll().forEach(allEntities::add);
			}
		}

		String result = "<option value=\"\"></option>";
		for (int i = 0; i < allEntities.size(); i++) {
			result += allEntities.get(i).getOptionHtml(false);
		}

		return result;
	}

	@PostMapping("/doctor/date-time/all")
	public @ResponseBody String loadTimeFramesByDoctorAndDate(Integer doctorId, String date) {
		ArrayList<VisitTime> allEntities = new ArrayList<>();

		Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

		allEntities = visitTimeRepository.findByDoctorAndDate(doctor, date);

		String result = "";
		for (int i = 0; i < allEntities.size(); i++) {
			result += allEntities.get(i).getTimeFrame();
		}

		return result;
	}



	@GetMapping("/visit-time")
	public String getEntityPage() {
		return "visittime";
	}

	@GetMapping("/visit-time/print")
	public String getEntityPrintPage() {
		return "visittimeprint";
	}

	@PostMapping("/visit-time/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (visitTimeRepository.existsById(id)) {
			visitTimeRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/visit-time/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute VisitTime visitTimeData)
	{
		//----- Saving by converted to object received params -----
		visitTimeRepository.save(visitTimeData);
		return "Saved";
	}

	@PostMapping(path="/visit-time/all")
	public @ResponseBody String getAllClientWorkTime(Integer employeeId) {
		if (employeeId == null) {
			return "";
		}

		ArrayList<VisitTime> allEntities = new ArrayList<>();
		visitTimeRepository.findAll().forEach(allEntities::add);

		Doctor client = doctorRepository.findById(employeeId).orElse(null);

		String requestResult = "";
		if (client != null) {
			requestResult = "<span id=\"filter-name\">Visit time of: " + client.getName() + " " + client.getSurname() + "</span><div id=\"time-history\">";

			for (VisitTime visitTime : allEntities) {
//				if (visitTime.getClient() == employeeId) //todo
//					requestResult += workTimeToHtmlBlock(visitTime);
			}

			requestResult += "</div>";
		}

		return requestResult;
	}
}
