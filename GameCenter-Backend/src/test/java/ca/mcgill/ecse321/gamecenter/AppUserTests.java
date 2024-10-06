package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AppUserTests {
    @Autowired
    private AppUserRepository appUserRepository;

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

        int id = owner.getId();
        String username = owner.getUsername();
        String email = owner.getEmail();
        String password = owner.getPassword();

        // Get by ID tests
        AppUser ownerFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(ownerFromDb);

        assert((owner.getIsActive() == ownerFromDb.getIsActive()) == true);
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by username tests
        ownerFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assert((owner.getIsActive() == ownerFromDb.getIsActive()) == true);
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by email tests
        ownerFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
        assertNotNull(ownerFromDb);

        assert((owner.getIsActive() == ownerFromDb.getIsActive()) == true);
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());
    }

    public void testPersistAndLoadEmployee() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = appUserRepository.save(employee);
        assertNotNull(employee);

        int id = employee.getId();
        String username = employee.getUsername();
        String email = employee.getEmail();
        String password = employee.getPassword();

        // Get by ID tests
        AppUser employeeFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(employeeFromDb);

        assert((employee.getIsActive() == employeeFromDb.getIsActive()) == true);
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by username tests
        employeeFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(employeeFromDb);

        assert((employee.getIsActive() == employeeFromDb.getIsActive()) == true);
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());

        // Get by email tests
        employeeFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
        assertNotNull(employeeFromDb);

        assert((employee.getIsActive() == employeeFromDb.getIsActive()) == true);
        assertEquals(id, employeeFromDb.getId());
        assertEquals(username, employeeFromDb.getUsername());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());
    }

    @Test
    public void testPersistAndLoadClient() {
        Client client = new Client();
        client.setEmail("bestintern@google.ca");
        client.setPassword("pushToProd");
        client.setUsername("Rotmaxx");

        client = appUserRepository.save(client);
        assertNotNull(client);

        int id = client.getId();
        String username = client.getUsername();
        String email = client.getEmail();
        String password = client.getPassword();

        // Get by ID tests
        AppUser clientFromDb = appUserRepository.findAppUserById(id).orElse(null);
        assertNotNull(clientFromDb);

        assert((client.getIsActive() == clientFromDb.getIsActive()) == true);
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = appUserRepository.findAppUserByUsername(username).orElse(null);
        assertNotNull(clientFromDb);

        assert((client.getIsActive() == clientFromDb.getIsActive()) == true);
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());

        // Get by username tests
        clientFromDb = appUserRepository.findAppUserByEmail(email).orElse(null);
        assertNotNull(clientFromDb);

        assert((client.getIsActive() == clientFromDb.getIsActive()) == true);
        assertEquals(id, clientFromDb.getId());
        assertEquals(username, clientFromDb.getUsername());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());
    }


}
