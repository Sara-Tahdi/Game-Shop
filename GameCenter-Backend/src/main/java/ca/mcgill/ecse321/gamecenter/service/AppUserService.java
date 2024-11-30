package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.*;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.utilities.Encryption;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    private boolean isUserActive(AppUser a) { return a.isIsActive(); }

    public Client getClientById(int id) {
        AppUser a = appUserRepository.findAppUserById(id).orElse(null);
        if (!(a instanceof Client)) {
            throw new IllegalArgumentException("There is no Client with id: " + id);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Client) a;
    }

    public Employee getEmployeeById(int id) {
        AppUser a = appUserRepository.findAppUserById(id).orElse(null);
        if (!(a instanceof Employee)) {
            throw new IllegalArgumentException("There is no Employee with id: " + id);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Employee) a;
    }

    public Client getClientByEmail(String email) {
        AppUser a = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (!(a instanceof Client)) {
            throw new IllegalArgumentException("There is no Client with email: " + email);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Client) a;
    }

    public Employee getEmployeeByEmail(String email) {
        AppUser a = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (!(a instanceof Employee)) {
            throw new IllegalArgumentException("There is no Employee with email: " + email);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Employee) a;
    }

    public Client getClientByUsername(String username) {
        AppUser a = appUserRepository.findAppUserByUsername(username).orElse(null);
        if (!(a instanceof Client)) {
            throw new IllegalArgumentException("There is no Client with username: " + username);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Client) a;
    }

    public Employee getEmployeeByUsername(String username) {
        AppUser a = appUserRepository.findAppUserByUsername(username).orElse(null);
        if (!(a instanceof Employee)) {
            throw new IllegalArgumentException("There is no Employee with username: " + username);
        }
        a.setPassword(Encryption.encryptDecrypt(a.getPassword()));
        return (Employee) a;
    }

    @Transactional
    public Client createClientAccount(String aEmail, String aUsername, String aPassword, String aPhoneNumber, String aDeliveryAddress) {
        AppUser a = appUserRepository.findAppUserByUsername(aUsername).orElse(null);
        if (a != null) {
            throw new IllegalArgumentException("User already exists with username: " + aUsername);
        }

        a = appUserRepository.findAppUserByEmail(aEmail).orElse(null);
        if (a != null) {
            throw new IllegalArgumentException("User already exists with email: " + aEmail);
        }

        if (aPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }

        Client c = new Client(aEmail, aUsername, Encryption.encryptDecrypt(aPassword), aPhoneNumber, aDeliveryAddress);
        return appUserRepository.save(c);
    }

    @Transactional
    public Client updateClientAccount(String email, String newUsername, String newPassword, String newPhoneNumber, String newDeliveryAddress, String oldPassword) {
        AppUser a = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There is no User with email: " + email);
        }

        if (!isUserActive(a)) {
            throw new IllegalArgumentException("This Client is not active");
        }

        AppUser testEmail = appUserRepository.findAppUserByUsername(newUsername).orElse(null);
        if (testEmail != null && testEmail.getId() != a.getId()) {
            throw new IllegalArgumentException("There already exists a User with username: " + newUsername);
        }

        if (!oldPassword.equals(Encryption.encryptDecrypt(a.getPassword()))) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }

        Client c = (Client) a;

        c.setUsername(newUsername);
        c.setPassword(Encryption.encryptDecrypt(newPassword));
        c.setPhoneNumber(newPhoneNumber);
        c.setDeliveryAddress(newDeliveryAddress);

        return appUserRepository.save(c);
    }

    @Transactional
    public Client deactivateClientAccountByUsername(String username) {
        Client c = (Client) appUserRepository.findAppUserByUsername(username).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("There is no Client with username: " + username);
        }
        c.setIsActive(false);
        return appUserRepository.save(c);
    }

    public List<AppUser> findAllClients() {
        return appUserRepository.findAppUserByUserType(Client.class).orElse(null);
    }

    @Transactional
    public Owner createOwnerAccount(String aEmail, String aUsername, String aPassword) {
        List<AppUser> check = findAllOwners();
        if (check != null && check.size() >= 1) {
            throw new IllegalArgumentException("Only 1 Owner account permitted");
        }

        if (aPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        Owner o = new Owner(aEmail, aUsername, Encryption.encryptDecrypt(aPassword));
        return appUserRepository.save(o);
    }

    @Transactional
    public Owner updateOwnerAccount(String email, String newUsername, String newPassword, String oldPassword) {
        AppUser a = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There is no User with email: " + email);
        }

        AppUser testUsername = appUserRepository.findAppUserByUsername(newUsername).orElse(null);
        if (testUsername != null && testUsername.getId() != a.getId()) {
            throw new IllegalArgumentException("There already exists a User with username: " + newUsername);
        }

        System.out.println(oldPassword);
        System.out.println(Encryption.encryptDecrypt(a.getPassword()));

        if (!oldPassword.equals(Encryption.encryptDecrypt(a.getPassword()))) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }

        Owner o = (Owner) a;
        o.setUsername(newUsername);
        o.setPassword(Encryption.encryptDecrypt(newPassword));
        return appUserRepository.save(o);
    }

    public List<AppUser> findAllOwners() {
        return appUserRepository.findAppUserByUserType(Owner.class).orElse(null);
    }

    @Transactional
    public Employee createEmployeeAccount(String aEmail, String aUsername, String aPassword) {
        Employee ref = (Employee) appUserRepository.findAppUserByUsername(aUsername).orElse(null);
        if (ref != null) {
            throw new IllegalArgumentException("User already exists with username: " + aUsername);
        }

        ref = (Employee) appUserRepository.findAppUserByEmail(aEmail).orElse(null);
        if (ref != null) {
            throw new IllegalArgumentException("User already exists with email: " + aEmail);
        }

        if (aPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        Employee e = new Employee(aEmail, aUsername, Encryption.encryptDecrypt(aPassword));
        return appUserRepository.save(e);
    }

    @Transactional
    public Employee updateEmployeeAccount(String email, String newUsername, String newPassword, String oldPassword) {
        AppUser a = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("There is no User with email: " + email);
        }

        if (!isUserActive(a)) {
            throw new IllegalArgumentException("This Employee is not active");
        }

        AppUser testUsername = appUserRepository.findAppUserByUsername(newUsername).orElse(null);
        if (testUsername != null && testUsername.getId() != a.getId()) {
            throw new IllegalArgumentException("There already exists a User with username: " + newUsername);
        }

        if (!oldPassword.equals(Encryption.encryptDecrypt(a.getPassword()))) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }

        Employee e = (Employee) a;
        e.setUsername(newUsername);
        e.setPassword(Encryption.encryptDecrypt(newPassword));
        return appUserRepository.save(e);
    }

    @Transactional
    public Employee deactivateEmployeeAccount(String username) {
        Employee e = (Employee) appUserRepository.findAppUserByUsername(username).orElse(null);
        if (e == null) {
            throw new IllegalArgumentException("There is no Employee with username: " + username);
        }
        e.setIsActive(false);
        return appUserRepository.save(e);
    }

    public List<AppUser> getAllEmployee() {
        return appUserRepository.findAppUserByUserType(Employee.class).orElse(null);
    }

    @Transactional
    public AppUser loginUser(String email, String password) {
        AppUser user = appUserRepository.findAppUserByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email and/or password.");
        }
    
        if (!password.equals(Encryption.encryptDecrypt(user.getPassword()))) {
            throw new IllegalArgumentException("Invalid email and/or password.");
        }
    
        return user;
    }
}
