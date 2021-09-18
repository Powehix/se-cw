package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WorkTime {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_work_time;

    private Integer id_task;

    private Integer id_employee;

    private String time;

    private String date;

    public Integer getIdWorkTime() {
        return id_work_time;
    }

    public void setIdWorkTime(Integer id_work_time) {
        this.id_work_time = id_work_time;
    }

    public Integer getIdTask() {
        return id_task;
    }

    public void setIdTask(Integer id_task) {
        this.id_task = id_task;
    }

    public Integer getIdEmployee() {
        return id_employee;
    }

    public void setIdEmployee(Integer id_employee) {
        this.id_employee = id_employee;
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