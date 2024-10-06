package ca.mcgill.ecse321.gamecenter.model;
import java.util.*;
import jakarta.persistence.*;

// line 93 "../../../../../../GameCenter.ump"
@Entity
@DiscriminatorValue("USER_REQUEST")
public class UserRequest extends Request {

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // UserRequest Associations
    @OneToOne // TODO Check other attributes
    private Client userFacingJudgement;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------
    public UserRequest() {
        super();
    }

    public UserRequest(Status aStatus, Staff aCreatedRequest, Client aUserFacingJudgement) {
        super(aStatus, aCreatedRequest);
        if (!setUserFacingJudgement(aUserFacingJudgement)) {
            throw new RuntimeException(
                    "Unable to create UserRequest due to aUserFacingJudgement. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    // ------------------------
    // INTERFACE
    // ------------------------
    /* Code from template association_GetOne */
    public Client getUserFacingJudgement() {
        return userFacingJudgement;
    }

    /* Code from template association_SetUnidirectionalOne */
    public boolean setUserFacingJudgement(Client aNewUserFacingJudgement) {
        boolean wasSet = false;
        if (aNewUserFacingJudgement != null) {
            userFacingJudgement = aNewUserFacingJudgement;
            wasSet = true;
        }
        return wasSet;
    }

    public void delete() {
        userFacingJudgement = null;
        super.delete();
    }

}