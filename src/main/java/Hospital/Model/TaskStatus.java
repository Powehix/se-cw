package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TaskStatus {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_status;

    private String title;

    public Integer getIdStatus() {
        return id_status;
    }

    public void setIdStatus(Integer id_status) {
        this.id_status = id_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdStatus() + "\" selected>" + getTitle() + "</option>";
        else
            return "<option value=\"" + getIdStatus() + "\">" + getTitle() + "</option>";
    }
}