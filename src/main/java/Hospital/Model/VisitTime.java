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

    public String getTimeFrame() {
        Doctor doctor = this.getDoctor();
        Client client = this.getClient();
            return "<div class=\"time-frame\">\n" +
                    "                    <div class=\"left-info-block\">\n" +
                    "                        <div class=\"date-time-info\">\n" +
                    "                            <span>" + this.getTime() + ":00 </span>\n" +
                    "                            <span>" + this.getDate() + "</span>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"doctor-info\">\n" +
                    "                            <span>" + doctor.getFullName() + "</span>\n" +
                    "                            <span>" + doctor.getPosition().getDepartment().getTitle() + " | " + doctor.getPosition().getTitle() + " | " + doctor.getRoom() + ".kab </span>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"right-info-block\">\n" +
                    "                        <div class=\"client-info\">\n" +
                    "                            <span>" + client.getFullName() + "</span>\n" +
                    "                            <span>" + client.getPersonalCode() + "</span>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"control-buttons-block\">\n" +
                    "                            <div></div>\n" +
                    "                            <button class=\"visit-cancel-button\" onclick=\"cancelVisit(" + doctor.getIdDoctor() + ",'" + this.getDate() + "','" + this.getTime() + "')\" >- Unassign</button>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                </div>";
    }

    public static String getEmptyTimeFrame(Doctor doctor, String date, String time) {
        return "<div class=\"time-frame\">\n" +
                "                    <div class=\"left-info-block\">\n" +
                "                        <div class=\"date-time-info\">\n" +
                "                            <span>" + time + ":00 </span>\n" +
                "                            <span>" + date + "</span>\n" +
                "                        </div>\n" +
                "                        <div class=\"doctor-info\">\n" +
                "                            <span>" + doctor.getFullName() + "</span>\n" +
                "                            <span>" + doctor.getPosition().getDepartment().getTitle() + " | " + doctor.getPosition().getTitle() + " | " + doctor.getRoom() + ".kab </span>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    <div class=\"right-info-block\">\n" +
                "                        <div class=\"control-buttons-block\">\n" +
                "                            <button class=\"visit-assign-button\" onclick=\"openAssignVisitPopUp(" + doctor.getIdDoctor() + ",'" + date + "','" + time + "')\">+ Assign</button>\n" +
                "                            <div></div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>";
    }
}
