package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Staff;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.repository.StaffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StaffTests {
    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    @AfterEach
    public void clear() {staffRepository.deleteAll();}

    @Test
    public void testPersistAndLoadOwner() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");

        owner = staffRepository.save(owner);
        assertNotNull(owner);
        assertTrue(owner.isIsActive());

        int id = owner.getId();
        String username = owner.getUsername();
        String email = owner.getEmail();
        String password = owner.getPassword();

        // Get by ID tests
        Staff ownerFromDb = staffRepository.findStaffById(id).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by username tests
        ownerFromDb = staffRepository.findStaffByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by email tests
        ownerFromDb = staffRepository.findStaffByEmail(email).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());
    }

    @Test
    public void testPersistAndLoadEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = staffRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        int id = employee.getId();
        String username = employee.getUsername();
        String email = employee.getEmail();
        String password = employee.getPassword();

        // Get by ID tests
        Staff employeeFromDb = staffRepository.findStaffById(id).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by username tests
        employeeFromDb = staffRepository.findStaffByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by email tests
        employeeFromDb = staffRepository.findStaffByEmail(email).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());
    }

    @Test
    public void testRemoveActivationFromOwner() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");

        owner = staffRepository.save(owner);
        assertNotNull(owner);
        assertTrue(owner.isIsActive());

        String username = owner.getUsername();

        staffRepository.updateByUsername(username);
        Staff ownerFromDb = staffRepository.findStaffByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assertFalse(ownerFromDb.isIsActive());
    }

    @Test
    public void testRemoveActivationFromEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = staffRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        String username = employee.getUsername();

        staffRepository.updateByUsername(username);
        Staff employeeFromDb = staffRepository.findStaffByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertFalse(employeeFromDb.isIsActive());
    }

    @Test
    public void testGetByUserType() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");
        owner = staffRepository.save(owner);
        assertNotNull(owner);

        Employee employee1 = new Employee();
        employee1.setEmail("bestintern@google.ca");
        employee1.setPassword("pushToProd");
        employee1.setUsername("Rotmaxx");
        employee1 = staffRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setEmail("better@employee.corp");
        employee2.setPassword("IAmTheBest");
        employee2.setUsername("ReplacingEmployee1");
        employee2 = staffRepository.save(employee2);
        assertNotNull(employee2);

        // Getting Staff by filter "OWNER"
        List<Staff> owner_list = staffRepository.findStaffByUserType(Owner.class).orElse(null);
        assertNotNull(owner_list);
        assertEquals(1, owner_list.size());

        for (Staff user: owner_list) {
            assertInstanceOf(Owner.class, user);
        }

        List<Staff> employee_list = staffRepository.findStaffByUserType(Employee.class).orElse(null);
        assertNotNull(employee_list);
        assertEquals(2, employee_list.size());

        for (Staff user: employee_list) {
            assertInstanceOf(Employee.class, user);
        }
    }
}
