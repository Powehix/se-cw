package Hospital.Model;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface PositionRepositoryCustom {
    public List<Position> findByDepartmentId(int id);
}
