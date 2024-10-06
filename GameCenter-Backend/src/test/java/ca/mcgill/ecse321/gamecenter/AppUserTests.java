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
    public void testPersistAndLoadOwnerById() {
        Owner owner = new Owner();
        owner.setEmail("fakeemail@jmail.ca");
        owner.setPassword("strongpassword123");
        owner.setUsername("TheLegend27");

        owner = appUserRepository.save(owner);
        int id = owner.getId();
        String email = owner.getEmail();
        String password = owner.getPassword();

        AppUser ownerFromDb = appUserRepository.findAppUserById(id);

        assertNotNull(owner);
        assert((owner.getIsActive() == ownerFromDb.getIsActive()) == true);
        assertEquals(id, ownerFromDb.getId());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());
    }

    public void testPersistAndLoadEmployeeById() {
        Employee employee = new Employee();
        employee.setEmail("bestintern@google.ca");
        employee.setPassword("pushToProd");
        employee.setUsername("Rotmaxx");

        employee = appUserRepository.save(employee);
        int id = employee.getId();
        String email = employee.getEmail();
        String password = employee.getPassword();

        AppUser employeeFromDb = appUserRepository.findAppUserById(id);

        assertNotNull(employee);
        assert((employee.getIsActive() == employeeFromDb.getIsActive()) == true);
        assertEquals(id, employeeFromDb.getId());
        assertEquals(email, employeeFromDb.getEmail());
        assertEquals(password, employeeFromDb.getPassword());
    }

    @Test
    public void testPersistAndLoadClientById() {
        Client client = new Client();
        client.setEmail("bestintern@google.ca");
        client.setPassword("pushToProd");
        client.setUsername("Rotmaxx");

        client = appUserRepository.save(client);
        int id = client.getId();
        String email = client.getEmail();
        String password = client.getPassword();

        AppUser clientFromDb = appUserRepository.findAppUserById(id);

        assertNotNull(client);
        assert((client.getIsActive() == clientFromDb.getIsActive()) == true);
        assertEquals(id, clientFromDb.getId());
        assertEquals(email, clientFromDb.getEmail());
        assertEquals(password, clientFromDb.getPassword());
    }
}
