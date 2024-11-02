package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.ClientResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.EmployeeRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.AppUsers.EmployeeResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserRestController {
    @Autowired
    private AppUserService appUserService;

    @PostMapping(value = "/users/createEmployee")
    public EmployeeResponseDTO createEmployeeAccount(@Validated @RequestBody EmployeeRequestDTO employeeToCreate) {
        Employee e = appUserService.createEmployeeAccount(
                employeeToCreate.getEmail(),
                employeeToCreate.getUsername(),
                employeeToCreate.getPassword()
        );

        return new EmployeeResponseDTO(e);
    }

    @PostMapping(value = "/users/createClient")
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
}
