package Hospital.Model;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_department;

    @Column(unique=true)
    private String title;

    public Department (String title) {
        this.title = title;
    }

    public Department () {

    }

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