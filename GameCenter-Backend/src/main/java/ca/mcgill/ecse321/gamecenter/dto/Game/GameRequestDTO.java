package ca.mcgill.ecse321.gamecenter.dto.Game;

import ca.mcgill.ecse321.gamecenter.model.Game;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GameRequestDTO {
    @NotNull(message = "Title is required")
    private String title;
    @Min(value = 0, message = "Price is required")
    private float price;
    @NotNull(message = "Description is required")
    private String description;
    @Min(value = 0, message = "Rating is required")
    private float rating;
    @Min(value = 0, message = "Remaining quantity is required")
    private int remainingQuantity;
    @NotNull(message = "Availability status is required")
    private boolean isOffered;
    @NotNull(message = "Public Opinion is required")
    private Game.GeneralFeeling publicOpinion;
    @Min(value = 1, message = "Category ID is required")
    private int categoryId;

    public GameRequestDTO(String title, float price, String description, float rating, int remainingQuantity, boolean isOffered, Game.GeneralFeeling publicOpinion, int categoryId) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.remainingQuantity = remainingQuantity;
        this.isOffered = isOffered;
        this.publicOpinion = publicOpinion;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return this.categoryId;
    }

}
