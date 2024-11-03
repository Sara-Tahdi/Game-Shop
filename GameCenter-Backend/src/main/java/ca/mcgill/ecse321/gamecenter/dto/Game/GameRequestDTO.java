package ca.mcgill.ecse321.gamecenter.dto.Game;

import ca.mcgill.ecse321.gamecenter.model.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class GameRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Price is required")
    private float price;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Rating is required")
    private float rating;
    @NotBlank(message = "Remaining quantity is required")
    private int remainingQuantity;
    @NotBlank(message = "Availability status is required")
    private boolean isOffered;
    @NotBlank(message = "Public Opinion is required")
    private Game.GeneralFeeling publicOpinion;
    @NotBlank(message = "Category is required")
    private GameCategory category;

    public GameRequestDTO(String title, float price, String description, float rating, int remainingQuantity, boolean isOffered, Game.GeneralFeeling publicOpinion, GameCategory category) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.remainingQuantity = remainingQuantity;
        this.isOffered = isOffered;
        this.publicOpinion = publicOpinion;
        this.category = category;
    }

    public String getTitle() {
        return this.title;
    }

    public float getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public float getRating() {
        return this.rating;
    }

    public int getRemainingQuantity() {
        return this.remainingQuantity;
    }

    public boolean getIsOffered() {
        return this.isOffered;
    }

    public Game.GeneralFeeling getPublicOpinion() {
        return this.publicOpinion;
    }

    public GameCategory getCategory() {
        return this.category;
    }

}
