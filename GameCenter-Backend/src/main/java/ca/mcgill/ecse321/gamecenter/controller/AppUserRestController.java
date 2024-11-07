package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.*;
import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppUserRestController {
    @Autowired
    private AppUserService appUserService;

    @PutMapping(value = "/users/owner/update/{oldPassword}")
    public OwnerResponseDTO updateOwnerAccount(@Validated @RequestBody OwnerRequestDTO ownerToUpdate, @PathVariable String oldPassword) {
        Owner o = appUserService.updateOwnerAccount(
                ownerToUpdate.getEmail(),
                ownerToUpdate.getUsername(),
                ownerToUpdate.getPassword(),
                oldPassword
        );
        return new OwnerResponseDTO(o);
    }

    @PostMapping(value = "/users/employee/create")
    public EmployeeResponseDTO createEmployeeAccount(@Validated @RequestBody EmployeeRequestDTO employeeToCreate) {
        Employee e = appUserService.createEmployeeAccount(
                employeeToCreate.getEmail(),
                employeeToCreate.getUsername(),
                employeeToCreate.getPassword()
        );
        return new EmployeeResponseDTO(e);
    }

    @PutMapping(value = "/users/employee/update/{oldPassword}")
    public EmployeeResponseDTO updateEmployee(@Validated @RequestBody EmployeeRequestDTO employeeToUpdate, @PathVariable String oldPassword) {
        Employee e = appUserService.updateEmployeeAccount(
                employeeToUpdate.getEmail(),
                employeeToUpdate.getUsername(),
                employeeToUpdate.getPassword(),
                oldPassword
        );
        return new EmployeeResponseDTO(e);
    }

    @PutMapping(value = "/users/employee/fire/{employeeUsername}")
    public EmployeeResponseDTO fireEmployee(@PathVariable String employeeUsername) {
        Employee e = appUserService.deactivateEmployeeAccount(employeeUsername);
        return new EmployeeResponseDTO(e);
    }

    @GetMapping(value = "/users/employee")
    public List<EmployeeResponseDTO> getEmployees() {
        List<AppUser> employees = appUserService.getAllEmployee();
        return employees.stream()
                .map(a -> new EmployeeResponseDTO((Employee) a))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users/employee/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable int id) {
        Employee e = appUserService.getEmployeeById(id);
        return new EmployeeResponseDTO(e);
    }

    @GetMapping(value = "/users/employee/username/{username}")
    public EmployeeResponseDTO getEmployeeByUsername(@PathVariable String username) {
        Employee e = appUserService.getEmployeeByUsername(username);
        return new EmployeeResponseDTO(e);
    }

    @GetMapping(value = "/users/employee/email/{email}")
    public EmployeeResponseDTO getEmployeeByEmail(@PathVariable String email) {
        Employee e = appUserService.getEmployeeByEmail(email);
        return new EmployeeResponseDTO(e);
    }

    @PostMapping(value = "/users/client/create")
    public ClientResponseDTO createClientAccount(@Validated @RequestBody ClientRequestDTO clientToCreate) {
        Client c = appUserService.createClientAccount(
                clientToCreate.getEmail(),
                clientToCreate.getUsername(),
                clientToCreate.getPassword(),
                clientToCreate.getPhoneNumber(),
                clientToCreate.getDeliveryAddress()
        );
        return new ClientResponseDTO(c);
    }

    @PutMapping(value = "/users/client/update/{oldPassword}")
    public ClientResponseDTO updateClientAccount(@Validated @RequestBody ClientRequestDTO clientToUpdate, @PathVariable String oldPassword) {
        Client c = appUserService.updateClientAccount(
                clientToUpdate.getEmail(),
                clientToUpdate.getUsername(),
                clientToUpdate.getPassword(),
                clientToUpdate.getPhoneNumber(),
                clientToUpdate.getDeliveryAddress(),
                oldPassword
        );
        return new ClientResponseDTO(c);
    }

    @PutMapping(value = "/users/client/ban/{username}")
    public ClientResponseDTO banClient(@PathVariable String username) {
        Client c = appUserService.deactivateClientAccountByUsername(username);
        return new ClientResponseDTO(c);
    }

    @GetMapping(value = "/users/client")
    public List<ClientResponseDTO> getClients() {
        List<AppUser> clients = appUserService.findAllClients();
        return clients.stream()
                .map(c -> new ClientResponseDTO((Client) c))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users/client/{id}")
    public ClientResponseDTO getClientById(@PathVariable int id) {
        Client c = appUserService.getClientById(id);
        return new ClientResponseDTO(c);
    }

    @GetMapping(value = "/users/client/username/{username}")
    public ClientResponseDTO getClientByUsername(@PathVariable String username) {
        Client c = appUserService.getClientByUsername(username);
        return new ClientResponseDTO(c);
    }

    @GetMapping(value = "/users/client/email/{email}")
    public ClientResponseDTO getClientByEmail(@PathVariable String email) {
        Client c = appUserService.getClientByEmail(email);
        return new ClientResponseDTO(c);
    }
}
