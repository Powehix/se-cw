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
	private DoctorRepository doctorRepository;

	@Autowired
	private VisitTimeRepository visitTimeRepository;

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

		VisitTime entity = visitTimeRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return workTimeGetEditFormHtml(entity);
	}

	@PostMapping("/work-time/filter")
	public @ResponseBody String loadVisitTimeFilterForm() {
		return workTimeGetFilterFormHtml();
	}

	@PostMapping("/work-time/remove")
	public @ResponseBody String deleteSelectedEntity(Integer id) {
		if (visitTimeRepository.existsById(id)) {
			visitTimeRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/work-time/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute VisitTime visitTimeData)
	{
		//----- Saving by converted to object received params -----
		visitTimeRepository.save(visitTimeData);
		return "Saved";
	}

	@PostMapping(path="/work-time/all")
	public @ResponseBody String getAllClientWorkTime(Integer employeeId) {
		if (employeeId == null) {
			return "";
		}

		ArrayList<VisitTime> allEntities = new ArrayList<>();
		visitTimeRepository.findAll().forEach(allEntities::add);

		Doctor client = doctorRepository.findById(employeeId).orElse(null);

		String requestResult = "";
		if (client != null) {
			requestResult = "<span id=\"filter-name\">Work time of: " + client.getName() + " " + client.getSurname() + "</span><div id=\"time-history\">";

			for (VisitTime visitTime : allEntities) {
				if (visitTime.getIdClient() == employeeId)
					requestResult += workTimeToHtmlBlock(visitTime);
			}

			requestResult += "</div>";
		}

		return requestResult;
	}

	@PostMapping(path="/work-time/sql")
	public @ResponseBody
	String executeSql (String query)
	{
		RowMapper<VisitTime> rm = (ResultSet result, int rowNum) -> {
			VisitTime object = new VisitTime();

			object.setIdVisitTime(result.getInt("id_work_time"));
			object.setIdDoctor(result.getInt("id_task"));
			object.setIdClient(result.getInt("id_employee"));
			object.setTime(result.getString("time"));
			object.setDate(result.getString("date"));

			return object;
		};

		List<VisitTime> clients = jdbcTemplate.query(query, new Object[]{}, rm);

		String requestResult = "";
		for (VisitTime oneClient : clients) {
			requestResult += workTimeToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/work-time/all/print")
	public @ResponseBody String getAllClientsWorkTime() {
		ArrayList<VisitTime> allEntities = new ArrayList<>();
		visitTimeRepository.findAll().forEach(allEntities::add);

		String requestResult = "";
		for (VisitTime oneEntity : allEntities) {
			requestResult += workTimeToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	public String workTimeToHtmlBlock(VisitTime time) {
//		Doctor task = taskRepository.findById(time.getIdDoctor()).orElse(null);

		return "test"/*"<div id=\"work-time-" + time.getIdVisitTime() + "\" class=\"company-element entity\" onclick=\"openDataForm('"+ time.getIdVisitTime() +"')\">\n" +
				"<div class=\"time-info-container\">\n" +
				"<span>Doctor "+ task.getIdDoctor() +"</span>\n" +
				"<span>"+ task.getShortTitle() +"</span>\n" +
				"<span>Worked: " + time.getTime() + "</span>\n" +
				"<span>At: " + time.getDate() + "</span>\n" +
				"</div>\n" +
				"</div>"*/;
	}

	public String workTimeToHtmlFullBlock(VisitTime time) {
//		Doctor task = taskRepository.findById(time.getIdDoctor()).orElse(null);
		Doctor client = doctorRepository.findById(time.getIdClient()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Doctor</span><span>" + client.getName() + " " + client.getSurname() +"</span></p>\n" +
				"<p><span>Doctor</span><span>["+ time.getIdDoctor() + "]</span></p>\n" +
				"<p><span>Worked</span><span>"+ time.getTime() +"</span></p>\n" +
				"<p><span>Date</span><span>" + time.getDate() + "</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String workTimeGetEditFormHtml(VisitTime time) {
		return "<div id=\"timelog-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"timelog-form\" action=\"/work-time/save\" method=\"post\">\n" +
				"                            <p>Doctor " +
				"<select name=\"idDoctor\" form=\"timelog-form\">" +
				getDoctorList(time.getIdDoctor()) +
				"</select></p>\n" +
				"                            <p>Doctor " +
				"<select name=\"idClient\" form=\"timelog-form\">" +
				getClientList(time.getIdClient()) +
				"</select></p>\n" +
				"                        <input type=\"hidden\" name=\"idVisitTime\" value=\""+ time.getIdVisitTime() +"\"/>\n" +
				"                        <p>Time <input type=\"text\" name=\"time\" class=\"data\" value=\""+ time.getTime() +"\"/></p>\n" +
				"                        <p>Date <input type=\"date\" name=\"date\" class=\"data\" value=\"" + time.getDate() + "\"/></p>\n" +
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('timelog-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ time.getIdVisitTime() +"')\" class=\"button-a\">\n" +
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
				"                            <p>Doctor " +
				"<select name=\"idClient\" form=\"timelog-form\">" +
				getClientList(null) +
				"</select></p>\n" +
				"                        <input type=\"hidden\" name=\"idDoctor\" value=\""+ taskId +"\"/>\n" +
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
				"<p>Doctor: " +
				"<select name=\"employeeId\" form=\"add-entity-form\">" +
				getClientList(null) +
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

	public String getClientList(Integer employeeId) {
		ArrayList<Doctor> employees = new ArrayList<>();
		doctorRepository.findAll().forEach(employees::add);

		String selectTagOptions = "test3";
		/*for (Doctor doctor : employees) {
			selectTagOptions += doctor.getOptionHtml((doctor.getIdClient().equals(employeeId)));
		}*/

		return selectTagOptions;
	}

	public String getDoctorList(Integer taskId) {
//		ArrayList<Doctor> tasks = new ArrayList<>();
//		taskRepository.findAll().forEach(tasks::add);
//
//		String selectTagOptions = "";
//		for (Doctor task : tasks) {
//			selectTagOptions += task.getOptionHtml((task.getIdDoctor().equals(taskId)));
//		}
//
//		return selectTagOptions;
		return "";
	}
}