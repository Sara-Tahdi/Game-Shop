package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.*;
import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AppUserRestController {
    @Autowired
    private AppUserService appUserService;

    @PostMapping(value = "/users/owner/create")
    public ResponseEntity<?> createOwnerAccount(@Validated @RequestBody OwnerCreateRequestDTO ownerToCreate) {
        try {
            Owner o = appUserService.createOwnerAccount(
                    ownerToCreate.getEmail(),
                    ownerToCreate.getUsername(),
                    ownerToCreate.getPassword()
            );
            return ResponseEntity.ok().body(new OwnerResponseDTO(o));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/users/owner/update")
    public ResponseEntity<?> updateOwnerAccount(@Validated @RequestBody OwnerRequestDTO ownerToUpdate) {
        try {
            Owner o = appUserService.updateOwnerAccount(
                    ownerToUpdate.getEmail(),
                    ownerToUpdate.getUsername(),
                    ownerToUpdate.getNewPassword(),
                    ownerToUpdate.getPassword()
            );
            return ResponseEntity.ok().body(new OwnerResponseDTO(o));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/users/employee/create")
    public ResponseEntity<?> createEmployeeAccount(@Validated @RequestBody EmployeeRequestDTO employeeToCreate) {
        try {
            Employee e = appUserService.createEmployeeAccount(
                    employeeToCreate.getEmail(),
                    employeeToCreate.getUsername(),
                    employeeToCreate.getPassword()
            );
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/users/employee/update")
    public ResponseEntity<?> updateEmployee(@Validated @RequestBody EmployeeRequestDTO employeeToUpdate) {
        try {
            Employee e = appUserService.updateEmployeeAccount(
                    employeeToUpdate.getEmail(),
                    employeeToUpdate.getUsername(),
                    employeeToUpdate.getPassword(),
                    employeeToUpdate.getOldPassword()
            );
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/users/employee/fire/{employeeUsername}")
    public ResponseEntity<?> fireEmployee(@PathVariable String employeeUsername) {
        try {
            Employee e = appUserService.deactivateEmployeeAccount(employeeUsername);
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/employee")
    public ResponseEntity<?> getEmployees() {
        try {
            List<AppUser> employees = appUserService.getAllEmployee();
            return ResponseEntity.ok().body(employees.stream()
                    .map(a -> new EmployeeResponseDTO((Employee) a))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/employee/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
        try {
            Employee e = appUserService.getEmployeeById(id);
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/employee/username/{username}")
    public ResponseEntity<?> getEmployeeByUsername(@PathVariable String username) {
        try {
            Employee e = appUserService.getEmployeeByUsername(username);
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/employee/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        try {
            Employee e = appUserService.getEmployeeByEmail(email);
            return ResponseEntity.ok().body(new EmployeeResponseDTO(e));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/users/client/create")
    public ResponseEntity<?> createClientAccount(@Validated @RequestBody ClientRequestDTO clientToCreate) {
        try {
            Client c = appUserService.createClientAccount(
                    clientToCreate.getEmail(),
                    clientToCreate.getUsername(),
                    clientToCreate.getPassword(),
                    clientToCreate.getPhoneNumber(),
                    clientToCreate.getDeliveryAddress()
            );
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/users/client/update")
    public ResponseEntity<?> updateClientAccount(@Validated @RequestBody ClientRequestDTO clientToUpdate) {
        try {
            Client c = appUserService.updateClientAccount(
                    clientToUpdate.getEmail(),
                    clientToUpdate.getUsername(),
                    clientToUpdate.getPassword(),
                    clientToUpdate.getPhoneNumber(),
                    clientToUpdate.getDeliveryAddress(),
                    clientToUpdate.getOldPassword()
            );
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/users/client/ban/{username}")
    public ResponseEntity<?> banClient(@PathVariable String username) {
        try {
            Client c = appUserService.deactivateClientAccountByUsername(username);
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/client")
    public ResponseEntity<?> getClients() {
        try {
            List<AppUser> clients = appUserService.findAllClients();
            return ResponseEntity.ok().body(clients.stream()
                    .map(c -> new ClientResponseDTO((Client) c))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/client/{id}")
    public ResponseEntity<?> getClientById(@PathVariable int id) {
        try {
            Client c = appUserService.getClientById(id);
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/client/username/{username}")
    public ResponseEntity<?> getClientByUsername(@PathVariable String username) {
        try {
            Client c = appUserService.getClientByUsername(username);
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/users/client/email/{email}")
    public ResponseEntity<?> getClientByEmail(@PathVariable String email) {
        try {
            Client c = appUserService.getClientByEmail(email);
            return ResponseEntity.ok().body(new ClientResponseDTO(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/users/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequestDTO loginRequest) {
        try {
            AppUser user = appUserService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok().body(new AppUserResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
