package Hospital.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Position {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_position;

    private String title;

    public Integer getIdPosition() {
        return id_position;
    }

    public void setIdPosition(Integer id_position) {
        this.id_position = id_position;
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