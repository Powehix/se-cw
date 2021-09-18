package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_project;

    private Integer id_team;

    private Integer id_client;

    private String title;

    private String start_date;

    private String deadline_date;

    private String finish_date;

    public Integer getIdProject() {
        return id_project;
    }

    public void setIdProject(Integer id_project) {
        this.id_project = id_project;
    }

    public Integer getIdClient() {
        return id_client;
    }

    public void setIdClient(Integer id_client) {
        this.id_client = id_client;
    }

    public Integer getIdTeam() {
        return id_team;
    }

    public void setIdTeam(Integer id_team) {
        this.id_team = id_team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public String getDeadlineDate() {
        return deadline_date;
    }

    public void setDeadlineDate(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getFinishDate() {
        return finish_date;
    }

    public void setFinishDate(String finish_date) {
        this.finish_date = finish_date;
    }
}