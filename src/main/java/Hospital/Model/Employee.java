package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_employee;

    private Integer id_team;

    private Integer id_position;

    private String name;

    private String surname;

    private String email;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdPosition() {
        return id_position;
    }

    public void setIdPosition(Integer id_position) {
        this.id_position = id_position;
    }

    public Integer getIdTeam() {
        return id_team;
    }

    public void setIdTeam(Integer id_team) {
        this.id_team = id_team;
    }

    public Integer getIdEmployee() {
        return id_employee;
    }

    public void setIdEmployee(Integer id_employee) {
        this.id_employee = id_employee;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdEmployee() + "\" selected>" + getName() + " " + getSurname() + "</option>";
        else
            return "<option value=\"" + getIdEmployee() + "\">" + getName() + " " + getSurname() + "</option>";
    }
}