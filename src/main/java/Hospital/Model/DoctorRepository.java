package Hospital.Model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Integer> {
    /*@Query("FROM doctor WHERE id_position = ?1")
    Map<Integer, Doctor> findByPositionId(int id);*/
}
