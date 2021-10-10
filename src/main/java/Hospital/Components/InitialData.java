package Hospital.Components;

import Hospital.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        // TODO: Add all classes sample data
        Map<String, Department> newDepartments = new HashMap<String, Department>();

        Department surgeryDepartment = new Department("Surgery");
        Department сlinicDepartment = new Department("Clinic");

        newDepartments.put("1", surgeryDepartment);
        newDepartments.put("2", сlinicDepartment);
        departmentRepo.saveAll(newDepartments.values());

        Map<String, Position> newPositions = new HashMap<String, Position>();

        Position Surgeon = new Position("Surgeon", surgeryDepartment);
        Position Anesthetist = new Position("Anesthetist", surgeryDepartment);
        Position Cardiologist = new Position("Cardiologist", surgeryDepartment);
        Position FamilyDoctor = new Position("Family doctor", сlinicDepartment);
        Position Pediatrician = new Position("Pediatrician", сlinicDepartment);
        Position Optometrist = new Position("Optometrist", сlinicDepartment);

        newPositions.put("surgeonPosition", Surgeon);
        newPositions.put("anesthetistPosition", Anesthetist);
        newPositions.put("сardiologistPosition", Cardiologist);

        newPositions.put("familyDoctorPosition", FamilyDoctor);
        newPositions.put("pediatricianPosition", Pediatrician);
        newPositions.put("optometristPosition", Optometrist);
        positionRepo.saveAll(newPositions.values()); // saves child classes and linked parent class


        //(String name, String surname, String email, String phone, String work_time,  Position position)
        Map<String, Doctor> newDoctors = new HashMap<String, Doctor>();
        Doctor Zane1 = new Doctor("Zane", "Ilmane", "Zane.I@hospital.lv", "+371 78451236", "09", "17", FamilyDoctor);
        Doctor Grule1 = new Doctor("Grule", "Mune", "Grule.M@hospital.lv", "+371 58451231", "10", "18", FamilyDoctor);
        Doctor Monika1 = new Doctor("Monika", "Tomika", "Monika.T@hospital.lv", "+371 82151236", "08", "16", Surgeon);
        Doctor Barbara1 = new Doctor("Barbara", "Baule", "Barbara.B@hospital.lv", "+371 83051231", "12", "20", Surgeon);
        Doctor Rufus1 = new Doctor("Rufus", "Baltimor", "Rufus.B@hospital.lv", "+371 76151236", "08", "16", Surgeon);
        Doctor Toms1 = new Doctor("Toms", "Fishman", "Toms.F@hospital.lv", "+371 11056230", "09", "17", Anesthetist);
        Doctor Daniels1 = new Doctor("Daniels", "Krumins", "Daniels.K@hospital.lv", "+371 70151236", "09", "17", Cardiologist);
        Doctor Viktors1 = new Doctor("Viktors", "Godmunds", "Viktors.G@hospital.lv", "+371 21951231", "10", "18", Cardiologist);
        Doctor Vavite = new Doctor("Vavite", "Paus", "Vavite.P@hospital.lv", "+371 16186236", "08", "16", Anesthetist);
        Doctor Pauls = new Doctor("Pauls", "Kreksons", "Pauls.K@hospital.lv", "+371 81052030", "09", "17", Optometrist);
        Doctor Dame = new Doctor("Dame", "Ludzite", "Dame.L@hospital.lv", "+371 42151236", "09", "17", Optometrist);
        Doctor Nortons = new Doctor("Nortons", "Etiksons", "Nortons.E@hospital.lv", "+371 95951281", "11", "19", Pediatrician);
        Doctor Laura = new Doctor("Laura", "Dumite", "Laura.D@hospital.lv", "+371 76186230", "09", "17", Pediatrician);
        Doctor Ivans = new Doctor("Ivans", "Ivanovichs", "Ivans.I@hospital.lv", "+371 92052041", "10", "18", Pediatrician);

        newDoctors.put("1", Zane1);
        newDoctors.put("2", Grule1);
        newDoctors.put("3", Monika1);
        newDoctors.put("4", Barbara1);
        newDoctors.put("5", Rufus1);
        newDoctors.put("6", Toms1);
        newDoctors.put("7", Daniels1);
        newDoctors.put("8", Viktors1);
        newDoctors.put("9", Vavite);
        newDoctors.put("10", Pauls);
        newDoctors.put("11", Dame);
        newDoctors.put("12", Nortons);
        newDoctors.put("13", Laura);
        newDoctors.put("14", Ivans);

        doctorRepo.saveAll(newDoctors.values());

        Map<String, Client> newClients = new HashMap<String, Client>();

        Client IvanIvanich = new Client("Ivan", "Ivanich", "Ivan@yahoo.com", "+371 29784511", "172691-90806");
        Client KatjaUmikova = new Client("Katja", "Umikova", "Katja@gmail.com", "+371 25784510", "051201-13909");
        Client VasilijsLazersons = new Client("Vasilijs", "Lazersons", "Vasilijs@mail.ru", "+371 79084512", "180600-81202");
        Client EvijaBlaumane = new Client("Evija", "Blaumane", "Evija@gmail.com", "+371 29084583", "280780-76811");
        Client TomsCirulis = new Client("Toms", "Cirulis", "Toms@gmail.com", "+371 277254514", "210302-13801");

        newClients.put("1", IvanIvanich);
        newClients.put("2", KatjaUmikova);
        newClients.put("3", VasilijsLazersons);
        newClients.put("4", EvijaBlaumane);
        newClients.put("5", TomsCirulis);

        clientRepo.saveAll(newClients.values());
    }
}
