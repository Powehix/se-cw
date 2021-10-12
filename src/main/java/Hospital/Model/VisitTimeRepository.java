package Hospital.Model;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface VisitTimeRepository extends CrudRepository<VisitTime, Integer> {
    ArrayList<VisitTime> findByDoctorAndDate(Doctor doctor, String date);
}
