package ca.mcgill.ecse321.gamecenter.dto.GameCategory;
public class GameCategoryRequestDTO {
    private String category;

    public GameCategoryRequestDTO() {}

    public GameCategoryRequestDTO(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
