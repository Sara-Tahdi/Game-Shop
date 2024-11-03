package ca.mcgill.ecse321.gamecenter.dto.Game;

import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.GameCategory;

public class GameResponseDTO {
    private int id;
    private String title;
    private float price;
    private String description;
    private float rating;
    private int remainingQuantity;
    private boolean isOffered;
    private Game.GeneralFeeling publicOpinion;
    private GameCategory category;

    public GameResponseDTO(int id, String title, float price, String description, float rating,
                           int remainingQuantity, boolean isOffered, Game.GeneralFeeling publicOpinion,
                           GameCategory category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.remainingQuantity = remainingQuantity;
        this.isOffered = isOffered;
        this.publicOpinion = publicOpinion;
        this.category = category;
    }

    public int getId() {
        return this.id;
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