package Hospital.Model;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public class PositionRepositoryImpl implements PositionRepositoryCustom {
    @Query("FROM position WHERE id_department = ?1")
    public List<Position> findByDepartmentId(int id) {
        return null;
    }
}
