package ca.mcgill.ecse321.gamecenter.dto.GameCategory;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;

public class GameCategoryResponseDTO {
    private int id;
    private String category;

    public GameCategoryResponseDTO() {}

    public GameCategoryResponseDTO(GameCategory gameCategory) {
        if (gameCategory != null) {
            this.id = gameCategory.getId();
            this.category = gameCategory.getCategory();
        }
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
