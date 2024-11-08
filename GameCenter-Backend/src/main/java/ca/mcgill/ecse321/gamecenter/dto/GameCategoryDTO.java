package ca.mcgill.ecse321.gamecenter.dto;

import ca.mcgill.ecse321.gamecenter.model.GameCategory;

public class GameCategoryDTO {
    @SuppressWarnings("unused")
    private int id;
    private String category;

    public GameCategoryDTO() {
    }

    public GameCategoryDTO(GameCategory gameCategory) {
        if (gameCategory != null) {

            this.category = gameCategory.getCategory();
            this.id = gameCategory.getId();

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
