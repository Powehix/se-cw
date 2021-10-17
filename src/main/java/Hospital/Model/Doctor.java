package Hospital.Model;

import javax.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idDoctor;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String work_time_start;

    private String work_time_end;

    private String room;

    public Doctor (String name, String surname, String email, String phone, String work_time_start, String work_time_end,  Position position, String room) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.work_time_start = work_time_start;
        this.work_time_end = work_time_end;
        this.position = position;
        this.room = room;
    }

    public Doctor () {

    }

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "idPosition", nullable = false)
    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
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
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdDoctor() + "\" selected>" + getName() + " " + getSurname() + "</option>";
        else
            return "<option value=\"" + getIdDoctor() + "\">" + getName() + " " + getSurname() + "</option>";
    }

    public String getWorkTimeStart() {
        return work_time_start;
    }

    public void setWorkTimeStart(String work_time_start) {
        this.work_time_start = work_time_start;
    }

    public String getWorkTimeEnd() {
        return work_time_start;
    }

    public void setWorkTimeEnd(String work_time_end) {
        this.work_time_start = work_time_end;
    }

    public String getFullName() {
        return this.name + " " + this.surname;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
