package Hospital.Components;

import Hospital.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class InitialData {
    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private PositionRepository positionRepo;

    @Autowired
    private VisitTimeRepository visitTimeRepo;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        // TODO: Add all classes sample data
        Department surgeryDepartment = new Department("Surgery");
//        Position surgeonPosition = new Position("Surgeon", surgeryDepartment);
//        Position anesthetistPosition = new Position("Anesthetist", surgeryDepartment);
//        Position сardiologistPosition = new Position("Cardiologist", surgeryDepartment);

        Map<String, Position> newPositions = new HashMap<String, Position>();
        newPositions.put("surgeonPosition", new Position("Surgeon", surgeryDepartment));
        newPositions.put("anesthetistPosition", new Position("Anesthetist", surgeryDepartment));
        newPositions.put("сardiologistPosition", new Position("Cardiologist", surgeryDepartment));

        positionRepo.saveAll(newPositions.values()); // saves child classes and linked parent class

        /*
        clientRepo.save(new Client(String name, String surname, String email, String phone, String personal_code));
        doctorRepo.save(new Doctor(String name, String surname, String email, String phone, String work_time, Position position));
        departmentRepo.save(new Department(String title));
        positionRepo.save(new Position(String title, Department department));
        visitTimeRepo.save(new VisitTime(String time, String date, Doctor doctor, Client client));
        */
    }
}