package Hospital.Model;

import javax.persistence.*;

@Entity
public class VisitTime {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_visit_time;

    private String time;

    private String date;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    public VisitTime (String time, String date, Doctor doctor, Client client) {
        this.time = time;
        this.date = date;
        this.doctor = doctor;
        this.client = client;
    }

    public VisitTime () {

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Integer getIdVisitTime() {
        return id_visit_time;
    }

    public void setIdVisitTime(Integer id_visit_time) {
        this.id_visit_time = id_visit_time;
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
