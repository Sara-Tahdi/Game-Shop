package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.utilities.Encryption;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppUserServiceTests {
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private AppUserService appUserService;

    /* Getter tests */
    @Test
    public void testGetEmployeeById() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Employee e = new Employee(email, username, password);
        e.setId(23);
        e.setPassword(Encryption.encryptDecrypt(e.getPassword()));

        when(appUserRepository.findAppUserById(e.getId())).thenReturn(Optional.of(e));
        AppUser a = appUserService.getEmployeeById(e.getId());

        assertInstanceOf(Employee.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetEmployeeByIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getEmployeeById(2));
        assertEquals("There is no Employee with id: " + 2, e.getMessage());
    }

    @Test
    public void testGetEmployeeByUsername() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Employee e = new Employee(email, username, password);
        e.setId(23);
        e.setPassword(Encryption.encryptDecrypt(e.getPassword()));

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(e));
        AppUser a = appUserService.getEmployeeByUsername(username);

        assertInstanceOf(Employee.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetEmployeeByUsernameFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getEmployeeByUsername("realUser"));
        assertEquals("There is no Employee with username: realUser", e.getMessage());
    }

    @Test
    public void testGetEmployeeByEmail() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Employee e = new Employee(email, username, password);
        e.setId(23);
        e.setPassword(Encryption.encryptDecrypt(e.getPassword()));

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(e));
        AppUser a = appUserService.getEmployeeByEmail(email);

        assertInstanceOf(Employee.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetEmployeeByEmailFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getEmployeeByEmail("inthesystem@google.com"));
        assertEquals("There is no Employee with email: inthesystem@google.com", e.getMessage());
    }

    @Test
    public void testGetClientById() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Client c = new Client();
        c.setEmail(email);
        c.setUsername(username);
        c.setPassword(password);
        c.setId(23);
        c.setPassword(Encryption.encryptDecrypt(c.getPassword()));

        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        AppUser a = appUserService.getClientById(c.getId());

        assertInstanceOf(Client.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetClientByIdFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getClientById(2));
        assertEquals("There is no Client with id: " + 2, e.getMessage());
    }

    @Test
    public void testGetClientByUsername() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Client c = new Client();
        c.setEmail(email);
        c.setUsername(username);
        c.setPassword(password);
        c.setId(23);
        c.setPassword(Encryption.encryptDecrypt(c.getPassword()));

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(c));
        AppUser a = appUserService.getClientByUsername(username);

        assertInstanceOf(Client.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetClientByUsernameFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getClientByUsername("realUser"));
        assertEquals("There is no Client with username: realUser", e.getMessage());
    }

    @Test
    public void testGetClientByEmail() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Client c = new Client();
        c.setEmail(email);
        c.setUsername(username);
        c.setPassword(password);
        c.setId(23);
        c.setPassword(Encryption.encryptDecrypt(c.getPassword()));

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(c));
        AppUser a = appUserService.getClientByEmail(email);

        assertInstanceOf(Client.class, a);
        assertEquals(email, a.getEmail());
        assertEquals(23, a.getId());
        assertEquals(username, a.getUsername());
        assertEquals(password, a.getPassword());
    }

    @Test
    public void testGetClientByEmailFail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.getClientByEmail("inthesystem@google.com"));
        assertEquals("There is no Client with email: inthesystem@google.com", e.getMessage());
    }

    /* All Owner Tests */
    @Test
    public void testCreateValidOwner() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);

        Owner createdOwner = appUserService.createOwnerAccount(email, username, password);

        assertNotNull(createdOwner);
        assertEquals(email, createdOwner.getEmail());
        assertEquals(username, createdOwner.getUsername());
        assertEquals(password, createdOwner.getPassword());
    }

    @Test
    public void testCreateInvalidOwner() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "short";
        when(appUserRepository.save(any(Owner.class))).thenThrow(IllegalArgumentException.class);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createOwnerAccount(email, username, password));
        assertEquals("Password too short", e.getMessage());
    }

    @Test
    public void testCreateSecondOwner() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);

        appUserService.createOwnerAccount(email, username, password);

        String email2 = "betterboss@gamecente.net";
        String username2 = "IJustGotPaid";
        String password2 = "WeBringTheBoom!";
        List<AppUser> defaultOwner = new ArrayList<AppUser>();
        defaultOwner.add(o);
        when(appUserRepository.findAppUserByUserType(Owner.class)).thenReturn(Optional.of(defaultOwner));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createOwnerAccount(email2, username2, password2));
        assertEquals("Only 1 Owner account permitted", e.getMessage());
    }

    @Test
    public void testUpdateOwnerValid() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);

        Owner createdOwner = appUserService.createOwnerAccount(email, username, password);

        String newUsername = "TheLegend27";
        Owner updatedOwner = new Owner(email, newUsername, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(updatedOwner);
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(o));

        createdOwner = appUserService.updateOwnerAccount(createdOwner.getEmail(), newUsername, createdOwner.getPassword());

        assertEquals(newUsername, createdOwner.getUsername());
        assertNotEquals(username, createdOwner.getUsername());
        assertEquals(email, createdOwner.getEmail());
        assertEquals(password, createdOwner.getPassword());
    }

    @Test
    public void testUpdateOwnerBadUsage() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);

        Owner createdOwner = appUserService.createOwnerAccount(email, username, password);
        String unknownEmail = "maliciousCode@bad.com";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateOwnerAccount(unknownEmail, createdOwner.getUsername(), createdOwner.getPassword()));
        assertEquals("There is no User with email: " + unknownEmail, e.getMessage());
    }

    @Test
    public void testUpdateOwnerInvalidUsername() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        o.setId(3);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);
        appUserService.createOwnerAccount(email, username, password);

        String email2 = "5booms@boommeter.com";
        String username2 = "BigAJ";
        String password2 = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email2, username2, password2);
        e.setId(5);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(o));
        when(appUserRepository.findAppUserByUsername(username2)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateOwnerAccount(email, username2, password));
        assertEquals("There already exists a User with username: " + username2, err.getMessage());
    }

    @Test
    public void testUpdateOwnerInvalidPassword() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        o.setId(3);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);
        appUserService.createOwnerAccount(email, username, password);

        String newPassword = "small";
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(o));
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(o));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateOwnerAccount(email, username, newPassword));
        assertEquals("Password too short", e.getMessage());
    }

    @Test
    public void testFindAllOwners() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);

        appUserService.createOwnerAccount(email, username, password);

        List<AppUser> defaultOwner = new ArrayList<AppUser>();
        defaultOwner.add(o);
        when(appUserRepository.findAppUserByUserType(Owner.class)).thenReturn(Optional.of(defaultOwner));

        List<AppUser> owners = appUserService.findAllOwners();

        assertEquals(1, owners.size());
    }

    /* All Employee Tests */
    @Test
    public void testCreateValidEmployee() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        assertEquals(email, createdEmployee.getEmail());
        assertEquals(username, createdEmployee.getUsername());
        assertEquals(password, createdEmployee.getPassword());
    }

    @Test
    public void testCreateInvalidEmployeeDuplicateUsername() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        appUserService.createEmployeeAccount(email, username, password);

        String email2 = "gamer@mario.ca";
        String password2 = "heahhadaaaaa";
        when(appUserRepository.save(any(Employee.class))).thenThrow(IllegalArgumentException.class);
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createEmployeeAccount(email2, username, password2));
        assertEquals("User already exists with username: " + username, err.getMessage());
    }

    @Test
    public void testCreateInvalidEmployeeDuplicateEmail() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        appUserService.createEmployeeAccount(email, username, password);

        String username2 = "bestemployee";
        String password2 = "heahhadaaaaa";
        when(appUserRepository.save(any(Employee.class))).thenThrow(IllegalArgumentException.class);
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createEmployeeAccount(email, username2, password2));
        assertEquals("User already exists with email: " + email, err.getMessage());
    }

    @Test
    public void testCreateInvalidEmployeePassword() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "!!";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createEmployeeAccount(email, username, password));
        assertEquals("Password too short", e.getMessage());
    }

    @Test
    public void testUpdateEmployeeValid() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String newUsername = "chickenbake";
        Employee updatedEmployee = new Employee(email, newUsername, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(e));

        createdEmployee = appUserService.updateEmployeeAccount(email, newUsername, createdEmployee.getPassword());

        assertEquals(email, createdEmployee.getEmail());
        assertEquals(newUsername, createdEmployee.getUsername());
        assertEquals(password, createdEmployee.getPassword());
    }

    @Test
    public void testUpdateEmployeeBadUsage() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);
        String unknownUser = "ItotallyExist@real.ca";

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateEmployeeAccount(unknownUser, createdEmployee.getUsername(), createdEmployee.getPassword()));
        assertEquals("There is no User with email: " + unknownUser, err.getMessage());
    }

    @Test
    public void testUpdateEmployeeInvalidUsername() {
        String email = "bigboss@gamecenter.net";
        String username = "biggestboss";
        String password = "GameCenter!!";
        Owner o = new Owner(email, username, password);
        o.setId(3);
        when(appUserRepository.save(any(Owner.class))).thenReturn(o);
        appUserService.createOwnerAccount(email, username, password);

        String email2 = "5booms@boommeter.com";
        String username2 = "BigAJ";
        String password2 = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email2, username2, password2);
        e.setId(5);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email2, username2, password2);

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(o));
        when(appUserRepository.findAppUserByEmail(email2)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateEmployeeAccount(email2, username, password2));
        assertEquals("There already exists a User with username: " + username, err.getMessage());
    }

    @Test
    public void testUpdateEmployeeInvalidPassword() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        appUserService.createEmployeeAccount(email, username, password);
        String newPassword = "chicken";
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateEmployeeAccount(email, username, newPassword));
        assertEquals("Password too short", err.getMessage());
    }

    @Test
    public void testDeactivateEmployeeValid() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(e));
        Employee employeeDeactivated = appUserService.deactivateEmployeeAccount(username);

        assertFalse(employeeDeactivated.getIsActive());
    }

    @Test
    public void testDeactivateEmployeeInvalid() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email, username, password);

        String fakeEmployee = "DaBoss";

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.deactivateEmployeeAccount(fakeEmployee));
        assertEquals("There is no Employee with username: " + fakeEmployee, err.getMessage());
    }

    @Test
    public void testGetAllEmployees() {
        String email = "5booms@boommeter.com";
        String username = "BigAJ";
        String password = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email, username, password);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);

        Employee createdEmployee = appUserService.createEmployeeAccount(email, username, password);

        String email2 = "test@tester.lol";
        String username2 = "BigJustice";
        String password2 = "farmmerge";
        Employee e2 = new Employee(email2, username2, password2);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e2);

        Employee createdEmployee2 = appUserService.createEmployeeAccount(email2, username2, password2);

        when(appUserRepository.findAppUserByUserType(Employee.class)).thenReturn(Optional.of(List.of(createdEmployee, createdEmployee2)));

        List<AppUser> employees = appUserService.getAllEmployee();

        assertNotNull(employees);
        assertEquals(2, employees.size());
    }

    /* All Client Tests */
    @Test
    public void testCreateValidClient() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);

        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        assertEquals(email, createdClient.getEmail());
        assertEquals(username, createdClient.getUsername());
        assertEquals(password, createdClient.getPassword());
        assertEquals(phoneNumber, createdClient.getPhoneNumber());
        assertEquals(deliveryAddress, createdClient.getDeliveryAddress());
    }

    @Test
    public void testCreateInvalidClientUsername() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String email2 = "tricky@yahoo.woo";
        String password2 = "QuiteHandsome";
        String phoneNumber2 = "5143419319";
        String deliveryAddress2 = "123 Jane Street";
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(c));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createClientAccount(email2, username, password2, phoneNumber2, deliveryAddress2));

        assertEquals("User already exists with username: " + username, e.getMessage());
    }

    @Test
    public void testCreateInvalidClientEmail() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String username2 = "tricky";
        String password2 = "QuiteHandsome";
        String phoneNumber2 = "5143419319";
        String deliveryAddress2 = "123 Jane Street";
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(c));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createClientAccount(email, username2, password2, phoneNumber2, deliveryAddress2));

        assertEquals("User already exists with email: " + email, e.getMessage());
    }

    @Test
    public void testCreateInvalidClientPassword() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "a";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress));
        assertEquals("Password too short", e.getMessage());
    }

    @Test
    public void testUpdateValidClient() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);

        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String newUsername = "DaveDavo";
        String newPassword = "MuchRicherThanBefore";
        Client newC = new Client(email, newUsername, newPassword, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(newC);
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(c));

        createdClient = appUserService.updateClientAccount(email, newUsername, newPassword, phoneNumber, deliveryAddress);

        assertEquals(newUsername, createdClient.getUsername());
        assertEquals(newPassword, createdClient.getPassword());
    }

    @Test
    public void testUpdateClientBadUsage() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);

        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);
        String unknownUser = "Davi@Dave.dave";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateClientAccount(unknownUser, username, password, phoneNumber, deliveryAddress));
        assertEquals("There is no User with email: " + unknownUser, e.getMessage());
    }

    @Test
    public void testUpdateClientInvalidUsername() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(9);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String email2 = "5booms@boommeter.com";
        String username2 = "BigAJ";
        String password2 = "DoubleChunkChocolateCookie";
        Employee e = new Employee(email2, username2, password2);
        e.setId(2941);
        when(appUserRepository.save(any(Employee.class))).thenReturn(e);
        appUserService.createEmployeeAccount(email2, username2, password2);

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(c));
        when(appUserRepository.findAppUserByUsername(username2)).thenReturn(Optional.of(e));

        IllegalArgumentException err = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateClientAccount(email, username2, password, phoneNumber, deliveryAddress));
        assertEquals("There already exists a User with username: " + username2, err.getMessage());
    }

    @Test
    public void testUpdateClientInvalidPassword() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String newPassword = "longpwd";
        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(c));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.updateClientAccount(email, username, newPassword, phoneNumber, deliveryAddress));
        assertEquals("Password too short", e.getMessage());
    }

    @Test
    public void testDeactivateClientValid() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        when(appUserRepository.findAppUserByUsername(username)).thenReturn(Optional.of(c));
        Client clientFromDB = appUserService.deactivateClientAccountByUsername(username);

        assertFalse(clientFromDB.getIsActive());
    }

    @Test
    public void testDeactivateClientInvalid() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String fakeUser = "MariBRos";

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.deactivateClientAccountByUsername(fakeUser));
        assertEquals("There is no Client with username: " + fakeUser, e.getMessage());
    }

    @Test
    public void testGetAllClient() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);

        Client createdClient1 = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String email2 = "tricky@yahoo.woo";
        String username2 = "JerryPig";
        String password2 = "QuiteHandsome";
        String phoneNumber2 = "5143419319";
        String deliveryAddress2 = "123 Jane Street";
        Client c2 = new Client(email2, username2, password2, phoneNumber2, deliveryAddress2);
        when(appUserRepository.save(any(Client.class))).thenReturn(c2);

        Client createdClient2 = appUserService.createClientAccount(email2, username2, password2, phoneNumber2, deliveryAddress2);

        when(appUserRepository.findAppUserByUserType(Client.class)).thenReturn(Optional.of(List.of(createdClient1, createdClient2)));

        List<AppUser> clients = appUserService.findAllClients();

        assertNotNull(clients);
        assertEquals(2, clients.size());
    }

    @Test
    public void testLoginUser() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient1 = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(createdClient1));
        AppUser loggedInUser = appUserService.loginUser(email, password);

        assertInstanceOf(Client.class, loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
        assertEquals(23, loggedInUser.getId());
        assertEquals(username, loggedInUser.getUsername());
        assertEquals(password, loggedInUser.getPassword());
    }

    @Test
    public void testLoginUserFailWrongEmail() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.loginUser("inthesystem@google.com", "VeryRich"));
        assertEquals("Invalid email and/or password.", e.getMessage());
    }

    @Test
    public void testLoginUserFailWrongPassword() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        Client createdClient1 = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        when(appUserRepository.findAppUserByEmail(email)).thenReturn(Optional.of(createdClient1));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                appUserService.loginUser("user1@gma.ca", "VeryRich"));
        assertEquals("Invalid email and/or password.", e.getMessage());
    }

}
