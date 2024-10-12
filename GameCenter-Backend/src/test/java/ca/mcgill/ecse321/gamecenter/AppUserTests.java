package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AppUserTests {
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        appUserRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");

        owner = appUserRepository.save(owner);
        assertNotNull(owner);
        assertTrue(owner.isIsActive());

        int id = owner.getId();
        String username = owner.getUsername();
        String email = owner.getEmail();
        String password = owner.getPassword();

        // Get by ID tests
        AppUser ownerFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by username tests
        ownerFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by email tests
        ownerFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
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

        employee = appUserRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        int id = employee.getId();
        String username = employee.getUsername();
        String email = employee.getEmail();
        String password = employee.getPassword();

        // Get by ID tests
        AppUser employeeFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by username tests
        employeeFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by email tests
        employeeFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
        assertNotNull(employeeFromDb);

        assertTrue(employeeFromDb.getIsActive());        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());
    }

    @Test
    public void testPersistAndLoadClient() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");

        client = appUserRepository.save(client);
        assertNotNull(client);
        assertTrue(client.isIsActive());

        int id = client.getId();
        String username = client.getUsername();
        String email = client.getEmail();
        String password = client.getPassword();

        // Get by ID tests
        AppUser clientFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
        assertNotNull(clientFromDb);

        assertTrue(clientFromDb.getIsActive());
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());
    }

    @Test
    public void testRemoveActivationFromOwner() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");

        owner = appUserRepository.save(owner);
        assertNotNull(owner);
        assertTrue(owner.isIsActive());

        String username = owner.getUsername();

        appUserRepository.updateByUsername(username);
        AppUser ownerFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assertFalse(ownerFromDb.isIsActive());
    }

    @Test
    public void testRemoveActivationFromEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = appUserRepository.save(employee);
        assertNotNull(employee);
        assertTrue(employee.isIsActive());

        String username = employee.getUsername();

        appUserRepository.updateByUsername(username);
        AppUser employeeFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assertFalse(employeeFromDb.isIsActive());
    }

    @Test
    public void testRemoveActivationFromClient() {
        Client client = new Client();
        client.setEmail("progamer@hai.ca");
        client.setPassword("mariobros");
        client.setUsername("Bowser");

        client = appUserRepository.save(client);
        assertNotNull(client);
        assertTrue(client.isIsActive());

        String username = client.getUsername();

        appUserRepository.updateByUsername(username);
        AppUser clientFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(clientFromDb);

        assertFalse(clientFromDb.isIsActive());
    }

    @Test
    public void testGetByUserType() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");
        owner = appUserRepository.save(owner);
        assertNotNull(owner);

        Employee employee1 = new Employee();
        employee1.setEmail("bestintern@google.ca");
        employee1.setPassword("pushToProd");
        employee1.setUsername("Rotmaxx");
        employee1 = appUserRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setEmail("better@employee.corp");
        employee2.setPassword("IAmTheBest");
        employee2.setUsername("ReplacingEmployee1");
        employee2 = appUserRepository.save(employee2);
        assertNotNull(employee2);

        Client client1 = new Client();
        client1.setEmail("progamer@hai.ca");
        client1.setPassword("mariobros");
        client1.setUsername("Bowser");
        client1 = appUserRepository.save(client1);
        assertNotNull(client1);

        Client client2 = new Client();
        client2.setEmail("justin@mail.mail");
        client2.setPassword("JustinJus");
        client2.setUsername("IamJustin");
        client2 = appUserRepository.save(client2);
        assertNotNull(client2);

        // Getting AppUser by filter "OWNER"
        List<AppUser> owner_list = appUserRepository.findAppUserByUserType(Owner.class).orElse(null);
        assertNotNull(owner_list);
        assertEquals(1, owner_list.size());

        for (AppUser user: owner_list) {
            assertInstanceOf(Owner.class, user);
        }

        List<AppUser> employee_list = appUserRepository.findAppUserByUserType(Employee.class).orElse(null);
        assertNotNull(employee_list);
        assertEquals(2, employee_list.size());

        for (AppUser user: employee_list) {
            assertInstanceOf(Employee.class, user);
        }

        List<AppUser> client_list = appUserRepository.findAppUserByUserType(Client.class).orElse(null);
        assertNotNull(client_list);
        assertEquals(2, client_list.size());

        for (AppUser user: client_list) {
            assertInstanceOf(Client.class, user);
        }
    }
}
