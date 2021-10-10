package Hospital.Model;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
public class Position {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idPosition;

    @Column(unique=true)
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "idDepartment", nullable = false)
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
        return idPosition;
    }

    public void setIdPosition(Integer idPosition) {
        this.idPosition = idPosition;
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
