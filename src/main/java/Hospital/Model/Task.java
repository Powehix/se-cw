package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_task;

    private Integer id_project;

    private String title;

    private String description;

    private Integer id_employee_reporter;

    private Integer id_employee_assignee;

    private String estimated_time;

    private String date;

    private Integer id_status;

    public Integer getIdTask() {
        return id_task;
    }

    public void setIdTask(Integer id_task) {
        this.id_task = id_task;
    }

    public Integer getIdProject() {
        return id_project;
    }

    public void setIdProject(Integer id_project) {
        this.id_project = id_project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdEmployeeReporter() {
        return id_employee_reporter;
    }

    public void setIdEmployeeReporter(Integer id_employee_reporter) {
        this.id_employee_reporter = id_employee_reporter;
    }

    public Integer getIdEmployeeAssignee() {
        return id_employee_assignee;
    }

    public void setIdEmployeeAssignee(Integer id_employee_assignee) {
        this.id_employee_assignee = id_employee_assignee;
    }

    public String getEstimatedTime() {
        return estimated_time;
    }

    public void setEstimatedTime(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIdStatus() {
        return id_status;
    }

    public void setIdStatus(Integer id_status) {
        this.id_status = id_status;
    }

    public String getShortTitle() {
        String shortTitle = "";
        if (title.length() > 27)
            shortTitle = title.substring(0, 27) + "...";
        else
            shortTitle = title;

        return shortTitle;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdTask() + "\" selected>" + getShortTitle() + "</option>";
        else
            return "<option value=\"" + getIdTask() + "\">" + getShortTitle() + "</option>";
    }
}