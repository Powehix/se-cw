package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_team;

    private String title;

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

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdTeam() + "\" selected>" + getTitle() + "</option>";
        else
            return "<option value=\"" + getIdTeam() + "\">" + getTitle() + "</option>";
    }
}