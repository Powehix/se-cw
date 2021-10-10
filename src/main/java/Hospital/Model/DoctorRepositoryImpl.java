package Hospital.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public class DoctorRepositoryImpl implements DoctorRepositoryCustom {
    @Query("FROM doctor WHERE id_position = ?1")
    public List<Doctor> findByPositionId(int id) {
        return null;
    }
}
