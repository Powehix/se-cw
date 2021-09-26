package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_department;

    private String title;

    public Integer getIdDepartment() {
        return id_department;
    }

    public void setIdDepartment(Integer id_department) {
        this.id_department = id_department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdDepartment() + "\" selected>" + getTitle() + "</option>";
        else
            return "<option value=\"" + getIdDepartment() + "\">" + getTitle() + "</option>";
    }
}