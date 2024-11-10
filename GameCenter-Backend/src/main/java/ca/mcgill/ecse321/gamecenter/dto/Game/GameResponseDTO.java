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

    @SuppressWarnings("unused")
    private GameResponseDTO() {
    }

    public GameResponseDTO(Game g) {
        this.id = g.getId();
        this.title = g.getTitle();
        this.price = g.getPrice();
        this.description = g.getDescription();
        this.rating = g.getRating();
        this.remainingQuantity = g.getRemainingQuantity();
        this.isOffered = g.getIsOffered();
        this.publicOpinion = g.getPublicOpinion();
        this.category = g.getCategory();
    }

    public GameResponseDTO(){}

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