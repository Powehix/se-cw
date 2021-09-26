package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_doctor;

    private Integer id_department;

    private Integer id_position;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String work_time;

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

    public Integer getIdDepartment() {
        return id_department;
    }

    public void setIdDepartment(Integer id_department) {
        this.id_department = id_department;
    }

    public Integer getIdDoctor() {
        return id_doctor;
    }

    public void setIdDoctor(Integer id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdDoctor() + "\" selected>" + getName() + " " + getSurname() + "</option>";
        else
            return "<option value=\"" + getIdDoctor() + "\">" + getName() + " " + getSurname() + "</option>";
    }

    public String getWorkTime() {
        return work_time;
    }

    public void setWorkTime(String work_time) {
        this.work_time = work_time;
    }
}