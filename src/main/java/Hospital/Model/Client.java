package Hospital.Model;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_client;

    private String name;

    private String surname;

    private String phone;

    private String email;

    @Column(unique=true)
    private String personal_code;

    public Client (String name, String surname, String email, String phone, String personal_code) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.personal_code = personal_code;
    }

    public Client () {

    }

    public Integer getIdClient() {
        return id_client;
    }

    public void setIdClient(Integer id_client) {
        this.id_client = id_client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOptionHtml(Boolean isSelected) {
        if (isSelected)
            return "<option value=\"" + getIdClient() + "\" selected>" + getName() + " " + getSurname() + "</option>";
        else
            return "<option value=\"" + getIdClient() + "\">" + getName() + " " + getSurname() + "</option>";
    }

    public String getPersonalCode() {
        return personal_code;
    }

    public void setPersonalCode(String personalCode) {
        this.personal_code = personalCode;
    }
}