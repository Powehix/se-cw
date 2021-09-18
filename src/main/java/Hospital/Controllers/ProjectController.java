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
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskStatusRepository taskStatusRepository;

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

	@PostMapping("/project-board")
	public @ResponseBody String loadProjectTasks(Integer id) {
		ArrayList<Task> tasks = new ArrayList<>();
		taskRepository.findAll().forEach(tasks::add);

		ArrayList<TaskStatus> statuses = new ArrayList<>();
		taskStatusRepository.findAll().forEach(statuses::add);

		ArrayList<Task> projectTasks = new ArrayList<>();

		for (Task task : tasks) {
			if (task.getIdProject().equals(id)) {
				projectTasks.add(task);
			}
		}

		String requestResult = "";
		for (TaskStatus status : statuses) {
			requestResult += "<div class=\"task-column\">\n" +
					"<div class=\"task-column-title\">\n" +
					"    <span>" + status.getTitle() + "</span>\n" +
					"</div>\n" +
					"<div class=\"task-column-content\">\n";
			for (Task task : projectTasks) {
				if (task.getIdStatus() == status.getIdStatus()) {
					requestResult += "<div class=\"task-block\" onclick=\"openDataForm('" + task.getIdTask() + "', false);\">\n" +
							"   <span>" + task.getShortTitle() + "</span>\n" +
							"   <span>" + task.getIdTask() + "</span>\n" +
							"</div>\n";
				}

			}
					requestResult += "</div></div>";
		}

		return requestResult;
	}


	@GetMapping("/project/print")
	public String getEntityPrintPage() {
		return "projectprint";
	}

	@GetMapping("/task/print")
	public String getAllTaskPrintPage() {
		return "taskprint";
	}

	@PostMapping("/project/form")
	public @ResponseBody String loadEntityEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return projectGetAddFormHtml();
		}

		Project entity = projectRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return projectGetEditFormHtml(entity);
	}

	@PostMapping("/project/remove")
	public @ResponseBody String deleteSelectedProject(Integer id) {
		if (projectRepository.existsById(id)) {
			projectRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/project/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute Project newData)
	{
		//----- Saving by converted to object received params -----
		projectRepository.save(newData);
		return "Saved";
	}

	@PostMapping("/task/form")
	public @ResponseBody String loadTaskEditFormWithData(Integer id, Boolean adding) {
		if (adding) {
			return taskGetAddFormHtml(id);
		}

		Task entity = taskRepository.findById(id).orElse(null);

		if (entity == null) {
			return "Not found!";
		}

		return taskGetEditFormHtml(entity);
	}

	@PostMapping(path="/task/save")
	public @ResponseBody
	String addNewEntity (@ModelAttribute Task newData)
	{
		if (newData.getDate() == null) {
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int day = localDate.getDayOfMonth();
			int month = localDate.getMonthValue();
			int year = localDate.getYear();

			newData.setDate(Integer.toString(day) + '.' + Integer.toString(month) + '.' + Integer.toString(year));
		}

		//----- Saving by converted to object received params -----
		taskRepository.save(newData);
		return "Saved";
	}

	@PostMapping("/task/remove")
	public @ResponseBody String deleteSelectedTask(Integer id) {
		if (taskRepository.existsById(id)) {
			taskRepository.deleteById(id);
			return "Deleted";
		} else {
			return "Not found!";
		}
	}

	@PostMapping(path="/project/all")
	public @ResponseBody String getAllUsers() {
		ArrayList<Project> allEntities = new ArrayList<>();
		projectRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Project oneEntity : allEntities) {
			requestResult += projectToHtmlBlock(oneEntity);
		}

		return requestResult;
	}

	@PostMapping(path="/project/sql")
	public @ResponseBody
	String executeProjectSql (String query)
	{
		RowMapper<Project> rm = (ResultSet result, int rowNum) -> {
			Project object = new Project();

			object.setIdProject(result.getInt("id_project"));
			object.setIdTeam(result.getInt("id_team"));
			object.setIdClient(result.getInt("id_client"));
			object.setTitle(result.getString("title"));
			object.setStartDate(result.getString("start_date"));
			object.setDeadlineDate(result.getString("deadline_date"));
			object.setFinishDate(result.getString("finish_date"));

			return object;
		};

		List<Project> clients = jdbcTemplate.query(query, new Object[]{}, rm);

		String requestResult = "";
		for (Project oneClient : clients) {
			requestResult += projectToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/task/sql")
	public @ResponseBody
	String executeTaskSql (String query)
	{
		RowMapper<Task> rm = (ResultSet result, int rowNum) -> {
			Task object = new Task();

			object.setIdProject(result.getInt("id_task"));
			object.setIdProject(result.getInt("id_project"));
			object.setTitle(result.getString("title"));
			object.setDescription(result.getString("description"));
			object.setIdEmployeeReporter(result.getInt("id_employee_reporter"));
			object.setIdEmployeeAssignee(result.getInt("id_employee_assignee"));
			object.setEstimatedTime(result.getString("estimated_time"));
			object.setDate(result.getString("date"));
			object.setIdStatus(result.getInt("id_status"));

			return object;
		};

		List<Task> clients = jdbcTemplate.query(query, new Object[]{}, rm);

		String requestResult = "";
		for (Task oneClient : clients) {
			requestResult += taskToHtmlFullBlock(oneClient);
		}

		return requestResult;
	}

	@PostMapping(path="/project/all/print")
	public @ResponseBody String getAllProjectsToPrint() {
		ArrayList<Project> allEntities = new ArrayList<>();
		projectRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Project oneEntity : allEntities) {
			requestResult += projectToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	@PostMapping(path="/task/all/print")
	public @ResponseBody String getAllTaskToPrint() {
		ArrayList<Task> allEntities = new ArrayList<>();
		taskRepository.findAll().forEach(allEntities::add);
		String requestResult = "";
		for (Task oneEntity : allEntities) {
			requestResult += taskToHtmlFullBlock(oneEntity);
		}

		return requestResult;
	}

	public String projectToHtmlBlock(Project project) {
		Team team = teamRepository.findById(project.getIdTeam()).orElse(null);

		return "<div class=\"company-element entity\" onclick=\"openProjectBoard('"+ project.getIdProject() +"','" + project.getTitle() + "')\">\n" +
				"<div class=\"company-image-container\">\n" +
				"<img src=\"/images/project.png\" alt=\"Project image\">\n" +
				"</div>\n" +
				"<div class=\"company-info-container\">\n" +
				"<span>" + project.getTitle() + "</span>\n" +
				"<span>" + team.getTitle() + "</span>\n" +
				"</div>\n" +
				"</div>";
	}

	public String projectToHtmlFullBlock(Project project) {
		Client client = clientRepository.findById(project.getIdClient()).orElse(null);
		Team team = teamRepository.findById(project.getIdTeam()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Project</span><span>" + project.getTitle() + "</span></p>\n" +
				"<p><span>Client</span><span>" + client.getTitle() + "</span></p>\n" +
				"<p><span>Team</span><span>"+ team.getTitle() +"</span></p>\n" +
				"<p><span>Start date</span><span>"+ project.getStartDate() +"</span></p>\n" +
				"<p><span>Deadline date</span><span>"+ project.getDeadlineDate() +"</span></p>\n" +
				"<p><span>Finish date</span><span>"+ project.getFinishDate() +"</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String taskToHtmlFullBlock(Task task) {
		Employee reporter = employeeRepository.findById(task.getIdEmployeeReporter()).orElse(null);
		Employee assignee = employeeRepository.findById(task.getIdEmployeeAssignee()).orElse(null);
		TaskStatus status = taskStatusRepository.findById(task.getIdStatus()).orElse(null);
		Project project = projectRepository.findById(task.getIdProject()).orElse(null);

		return "<div class=\"company-element entity\">\n" +
				"<div class=\"company-info-container\">\n" +
				"<p><span>Project</span><span>" + project.getTitle() + "</span></p>\n" +
				"<p><span>Title</span><span>" + task.getTitle() + "</span></p>\n" +
				"<p><span>Description</span><span>" + task.getDescription() + "</span></p>\n" +
				"<p><span>Status</span><span>" + status.getTitle() + "</span></p>\n" +
				"<p><span>Reporter</span><span>"+ reporter.getName() + ' ' + reporter.getSurname() +"</span></p>\n" +
				"<p><span>Assignee</span><span>"+ assignee.getName() + ' ' + assignee.getSurname() +"</span></p>\n" +
				"<p><span>Estimated time</span><span>"+ task.getEstimatedTime() +"</span></p>\n" +
				"<p><span>Created at</span><span>"+ task.getDate() +"</span></p>\n" +
				"</div>\n" +
				"</div>";
	}

	public String projectGetEditFormHtml(Project project) {
		return "<div id=\"form-edit-container\" class=\"form-place-holder\">\n" +
				"                    <div class=\"form-container\">\n" +
				"                        <form id=\"edit-entity-form\" action=\"/project/save\" method=\"post\">\n" +
				"                            <p>Title: <input type=\"text\" name=\"title\" class=\"data\" value='"+ project.getTitle() +"'/></p>\n" +
				"                            <p>Client: " +
				"<select name=\"idClient\" form=\"edit-entity-form\">" +
				getClientList(project.getIdClient()) +
				"</select></p>\n" +
				"                            <p>Team: " +
				"<select name=\"idTeam\" form=\"edit-entity-form\">" +
				getTeamList(project.getIdTeam()) +
				"</select></p>\n" +
				"                            <p>Start date: <input type=\"date\" name=\"startDate\" class=\"data\" value='"+ project.getStartDate() + "'/></p>\n" +
				"                            <p>Deadline date: <input type=\"date\" name=\"deadlineDate\" class=\"data\" value='"+ project.getDeadlineDate() + "'/></p>\n" +
				"                            <p>Finish date: <input type=\"date\" data-default=\"empty\" name=\"finishDate\" class=\"data\" value='"+ project.getFinishDate() + "'/></p>\n" +
				"                            <input type=\"hidden\" name=\"idProject\" value='"+ project.getIdProject() +"'/>"+
				"                        </form>\n" +
				"                        <div class=\"form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('edit-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ project.getIdProject() +"', 'project')\" class=\"button-a\">\n" +
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
		ArrayList<Team> teams = new ArrayList<>();
		teamRepository.findAll().forEach(teams::add);

		String selectTagOptions = "";
		for (Team team : teams) {
			selectTagOptions += team.getOptionHtml((team.getIdTeam().equals(employeeTeamId)));
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
				"                            <p>Team: " +
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
				"<select name=\"idStatus\" form=\"add-entity-form\">" +
				getStatusList(0) +
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
		ArrayList<Employee> employees = new ArrayList<>();
		employeeRepository.findAll().forEach(employees::add);

		String selectTagOptions = "";
		for (Employee employee : employees) {
			selectTagOptions += employee.getOptionHtml((employee.getIdEmployee().equals(employeeId)));
		}

		return selectTagOptions;
	}

	public String getStatusList(Integer statusId) {
		ArrayList<TaskStatus> statuses = new ArrayList<>();
		taskStatusRepository.findAll().forEach(statuses::add);

		String selectTagOptions = "";
		for (TaskStatus status : statuses) {
			selectTagOptions += status.getOptionHtml((status.getIdStatus().equals(statusId)));
		}

		return selectTagOptions;
	}

	public String taskGetEditFormHtml(Task task) {
		return "<div id=\"form-edit-container\" class=\"form-place-holder\">\n" +
				"                <div class=\"task-form-container\">\n" +
				"                    <form id=\"edit-entity-form\" action=\"/task/save\" method=\"post\">\n" +
				"<div class=\"left-part\">" +
					"<p><span>Task " +  task.getIdTask() + "</span></p>\n" +
					"<p><input type=\"text\" name=\"title\" class=\"data no-border-input\" value='" + task.getTitle() + "'/></p>\n" +
					"<p>Description <textarea name=\"description\" form=\"edit-entity-form\" class=\"data large-input\">" + task.getDescription() + "</textarea></p>\n" +
					"<a onclick=\"openDataForm('" + task.getIdTask() + "', true, 'work-time')\" class=\"button-a form-cancel\">\n" +
					"	<img src=\"/images/time.png\" class=\"form-menu-image\">\n" +
					"	<span>LOG WORK TIME</span>\n" +
					"</a>\n" +
				"</div>" +
				"<div class=\"right-part\">" +
					"<select name=\"idStatus\" form=\"edit-entity-form\">" +
						getStatusList(task.getIdStatus()) +
					"</select>" +
					"<p>Assignee " +
					"<select name=\"idEmployeeAssignee\" form=\"edit-entity-form\">" +
						getEmployeeList(task.getIdEmployeeAssignee()) +
					"</select></p>\n" +
					"<p>Reporter " +
					"<select name=\"idEmployeeReporter\" form=\"edit-entity-form\">" +
						getEmployeeList(task.getIdEmployeeReporter()) +
					"</select></p>\n" +
					"<p>Estimate time <input type=\"text\" data-time=\"true\" name=\"estimatedTime\" class=\"data\" value='" + task.getEstimatedTime() + "'/></p>\n" +
				"                    <div class=\"task-form-navigation\">\n" +
				"                            <a onclick=\"submitDataForm('edit-entity-form')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/save.png\" class=\"form-menu-image\">\n" +
				"                                <span>UPDATE</span>\n" +
				"                            </a>\n" +
				"                            <a onclick=\"entityRemoving('"+ task.getIdTask() +"')\" class=\"button-a\">\n" +
				"                                <img src=\"/images/delete.png\" class=\"form-menu-image\">\n" +
				"                                <span>REMOVE</span>\n" +
				"                            </a>\n" +
				"<a onclick=\"hideForm('edit-entity-form');\" class=\"button-a form-cancel\">\n" +
				"   <img src=\"/images/cancel.png\" class=\"form-menu-image\">\n" +
				"   <span>CANCEL</span>\n" +
				"</a>\n" +
				"                        </div>\n" +
				"</div>" +
				"<input type=\"hidden\" name=\"idProject\" value='"+ task.getIdProject() +"'/>"+
				"<input type=\"hidden\" name=\"date\" value='"+ task.getDate() +"'/>"+
				"<input type=\"hidden\" name=\"idTask\" value='"+ task.getIdTask() +"'/>"+
				"                    </form>\n" +
				"                </div>\n" +
				"            </div>";
	}
}