package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = employeeRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        int id = employee.getId();
        String username = employee.getUsername();
        String email = employee.getEmail();
        String password = employee.getPassword();

        // Get by ID tests
        AppUser employeeFromDb = employeeRepository.findEmployeeById(id).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by username tests
        employeeFromDb = employeeRepository.findEmployeeByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by email tests
        employeeFromDb = employeeRepository.findEmployeeByEmail(email).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());
    }
    
    @Test
    public void testRemoveActivationFromEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = employeeRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        String username = employee.getUsername();

        employeeRepository.updateByUsername(username);
        AppUser employeeFromDb = employeeRepository.findEmployeeByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertFalse(employeeFromDb.isIsActive());
    }
    
    @Test
    public void testGetByUserType() {
        Employee employee1 = new Employee();
        employee1.setEmail("bestintern@google.ca");
        employee1.setPassword("pushToProd");
        employee1.setUsername("Rotmaxx");
        employee1 = employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setEmail("better@employee.corp");
        employee2.setPassword("IAmTheBest");
        employee2.setUsername("ReplacingEmployee1");
        employee2 = employeeRepository.save(employee2);
        assertNotNull(employee2);

        List<Employee> employee_list = employeeRepository.findEmployeeByUserType(Employee.class).orElse(null);
        assertNotNull(employee_list);
        assertEquals(2, employee_list.size());

        for (Employee user: employee_list) {
            assertTrue(user instanceof Employee);
        }
    }
}
