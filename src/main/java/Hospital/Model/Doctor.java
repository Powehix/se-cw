package Hospital.Model;

import javax.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_doctor;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String work_time;

    public Doctor (String name, String surname, String email, String phone, String work_time,  Position position) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.work_time = work_time;
        this.position = position;
    }

    public Doctor () {

    }

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "id_position", nullable = false)
    private Position position;

    public Position getIdPosition() {
        return position;
    }

    public void setIdPosition(Position position) {
        this.position = position;
    }

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