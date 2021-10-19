package Hospital.Controllers;

import Hospital.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
	private ClientRepository clientRepository;

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

	@PostMapping("/visit-time/cancel")
	public @ResponseBody String cancelVisitByDoctorAndDate(Integer doctorId, String date, String time) {
		Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

		VisitTime visit = visitTimeRepository.findByDoctorAndDateAndTime(doctor, date, time);

		visitTimeRepository.deleteById(visit.getIdVisitTime());

		return "Deleted";
	}

	@PostMapping("/visit-time/assign")
	public @ResponseBody String assignVisit(Integer doctorId, Integer clientId, String date, String time) {
		Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
		Client client = clientRepository.findById(clientId).orElse(null);

		VisitTime visit = new VisitTime(time, date, doctor, client);
		visitTimeRepository.save(visit);

		return "Assigned";
	}



	@PostMapping("/visit-time/assign-form")
	public @ResponseBody String assignVisitFormPopUp(Integer doctorId, String date, String time) {
		Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
		VisitTime visit = visitTimeRepository.findByDoctorAndDateAndTime(doctor, date, time);

		return "<div id=\"form-assign-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				" 						 <span style=\"margin: 20px auto; font-size: 19px\">Visit on " + date + " at " + time + ":00</span>" +
				"                        <form id=\"assign-entity-form\" action=\"/visit-time/assign\" method=\"post\">\n" +
				"							<input type=\"hidden\" name=\"doctorId\" class=\"data\" value='"+ doctorId +"'/>" +
				"							<input type=\"hidden\" name=\"date\" class=\"data\" value='"+ date +"'/>" +
				"							<input type=\"hidden\" name=\"time\" class=\"data\" value='"+ time +"'/>" +
				"							<select id=\"clientSelectTag\" name=\"clientId\" form=\"assign-entity-form\" style=\"width: 100%\">" +
												getAllClients(null) +
				"							</select></p>\n" +
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('assign-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>ASSIGN</span>\n" +
				"                            </a>\n" +
				"							 <a onclick=\"openAddNewClient()\" class=\"button-a\">" +
				"			                    <img src=\"/images/add.png\" class=\"left-menu-image\">" +
				"								<span>ADD NEW CLIENT</span>" +
				"						     </a>" +
				"                        </div>\n" +
				"                        <a onclick=\"hideForm('assign-entity-form');\" class=\"button-a form-cancel\">\n" +
				"                            <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"                            <span>CANCEL</span>\n" +
				"                        </a>\n" +
				"                    </div>\n" +
				"                </div>";
	}

	@PostMapping("/client/visit-time/options")
	public @ResponseBody String getClientOptions(Integer clientId) {
		String result = getAllClients(clientId);

		return result;
	}

	private String getAllClients(Integer clientId) {
			ArrayList<Client> allClients = new ArrayList<>();
			clientRepository.findAll().forEach(allClients::add);
			String result = "<option value=\"\"></option>";
			if (clientId == null) {
				for (Client client : allClients) {
					result += client.getOptionHtml(false);
				}
			} else {
				for (Client client : allClients) {
					if (client.getIdClient() == clientId)
						result += client.getOptionHtml(true);
					else
						result += client.getOptionHtml(false);
				}
			}
		return result;
	}

	@PostMapping("/doctor/date-time/all")
	public @ResponseBody String loadTimeFramesByDoctorAndDate(Integer doctorId, String date) {
		ArrayList<VisitTime> allEntities = new ArrayList<>();

		Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

		allEntities = visitTimeRepository.findByDoctorAndDate(doctor, date);

		SortedMap<String, VisitTime> timeMapForVisits = new TreeMap<>();

		int startTime = Integer.parseInt(doctor != null ? doctor.getWorkTimeStart() : null);
		int endTime = Integer.parseInt(doctor != null ? doctor.getWorkTimeEnd() : null);
		for (; startTime < endTime; startTime++) {
			timeMapForVisits.put(startTime / 10 + "" + startTime % 10, null);
		}

		for (int i = 0; i < allEntities.size(); i++) {
			VisitTime visit = allEntities.get(i);
			timeMapForVisits.put(visit.getTime(), visit);
		}

		String result = "";

		for (Map.Entry<String, VisitTime> possibleVisit : timeMapForVisits.entrySet()) {
			//System.out.println("Key : " + possibleVisit.getKey() + " Value : " + possibleVisit.getValue());
			if (possibleVisit.getValue() == null) {
				result += VisitTime.getEmptyTimeFrame(doctor, date, possibleVisit.getKey());
			} else {
				result += possibleVisit.getValue().getTimeFrame();
			}
		}

		return result;
	}

	@GetMapping("/visit-time")
	public String getEntityPage() {
		return "visittime";
	}
}
