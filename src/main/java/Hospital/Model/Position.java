package Hospital.Model;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
public class Position {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_position;

    @Column(unique=true)
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "id_department", nullable = false)
    private Department department;

    public Position (String title, Department department) {
        this.title = title;
        this.department = department;
    }

    public Position () {

    }

    public Department getDepartment() {
        return department;
    }

    public void setIdDepartment(Department department) {
        this.department = department;
    }

    public Integer getIdPosition() {
        return id_position;
    }

    public void setIdPosition(Integer id_position) {
        this.id_position = id_position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdPosition() + "\" selected>" + getTitle() + "</option>";
        else
            return "<option value=\"" + getIdPosition() + "\">" + getTitle() + "</option>";
    }

}
