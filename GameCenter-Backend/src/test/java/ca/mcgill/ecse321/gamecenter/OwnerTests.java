package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Owner;
import ca.mcgill.ecse321.gamecenter.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OwnerTests {
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        ownerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner() {
        Owner owner = new Owner();
        owner.setEmail("bestowner@google.ca");
        owner.setPassword("pushToProd");
        owner.setUsername("Rotmaxx");

        owner = ownerRepository.save(owner);
        assertNotNull(owner);
        assertTrue(owner.isIsActive());

        int id = owner.getId();
        String username = owner.getUsername();
        String email = owner.getEmail();
        String password = owner.getPassword();

        // Get by ID tests
        AppUser ownerFromDb = ownerRepository.findOwnerById(id).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by username tests
        ownerFromDb = ownerRepository.findOwnerByUsername(username).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());

        // Get by email tests
        ownerFromDb = ownerRepository.findOwnerByEmail(email).orElse(null);
        assertNotNull(ownerFromDb);

        assertTrue(ownerFromDb.getIsActive());
        assertEquals(id, ownerFromDb.getId());
        assertEquals(username, ownerFromDb.getUsername());
        assertEquals(email, ownerFromDb.getEmail());
        assertEquals(password, ownerFromDb.getPassword());
    }

    @Test
    public void testGetByUserType() {
        Owner owner1 = new Owner();
        owner1.setEmail("bestowner@google.ca");
        owner1.setPassword("pushToProd");
        owner1.setUsername("Rotmaxx");
        owner1 = ownerRepository.save(owner1);

        List<Owner> owner_list = ownerRepository.findOwnerByUserType(Owner.class).orElse(null);
        assertNotNull(owner_list);
        assertEquals(1, owner_list.size());

        for (Owner user: owner_list) {
            assertInstanceOf(Owner.class, user);
        }
    }
}
