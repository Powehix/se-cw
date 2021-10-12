package Hospital.Model;

import javax.persistence.*;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idDepartment;

    @Column(unique=true)
    private String title;

    public Department (String title) {
        this.title = title;
    }

    public Department () {

    }

    public Integer getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(Integer idDepartment) {
        this.idDepartment = idDepartment;
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
