package ca.mcgill.ecse321.gamecenter.dto.Game;

import jakarta.validation.constraints.NotNull;

public class GameUpdateRequestDTO {
    @NotNull(message = "Title is required")
    private String title;
    @NotNull(message = "Price is required")
    private float price;
    @NotNull(message = "Description is required")
    private String description;
    @NotNull(message = "Rating is required")
    private float rating;
    @NotNull(message = "Remaining quantity is required")
    private int remainingQuantity;
    @NotNull(message = "Availability status is required")
    private boolean isOffered;
    @NotNull(message = "Category ID is required")
    private int categoryId;

    public GameUpdateRequestDTO() {}

    public GameUpdateRequestDTO(String title, float price, String description, float rating, int remainingQuantity, boolean isOffered, int categoryId) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.remainingQuantity = remainingQuantity;
        this.isOffered = isOffered;
        this.categoryId = categoryId;
    }

    public String getTitle() { return this.title; }
    public float getPrice() { return this.price; }
    public String getDescription() { return this.description; }
    public float getRating() { return this.rating; }
    public int getRemainingQuantity() { return this.remainingQuantity; }
    public boolean getIsOffered() { return this.isOffered; }
    public int getCategoryId() { return this.categoryId; }
}
