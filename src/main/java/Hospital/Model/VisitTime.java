package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VisitTime {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_visit_time;

    private Integer id_doctor;

    private Integer id_client;

    private String time;

    private String date;

    public Integer getIdVisitTime() {
        return id_visit_time;
    }

    public void setIdVisitTime(Integer id_visit_time) {
        this.id_visit_time = id_visit_time;
    }

    public Integer getIdDoctor() {
        return id_doctor;
    }

    public void setIdDoctor(Integer id_doctor) {
        this.id_doctor = id_doctor;
    }

    public Integer getIdClient() {
        return id_client;
    }

    public void setIdClient(Integer id_client) {
        this.id_client = id_client;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}