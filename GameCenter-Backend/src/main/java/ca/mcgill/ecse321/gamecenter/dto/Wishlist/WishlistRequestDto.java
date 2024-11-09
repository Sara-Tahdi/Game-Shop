package ca.mcgill.ecse321.gamecenter.dto.Wishlist;

import jakarta.validation.constraints.NotNull;

public class WishlistRequestDto {

    @NotNull(message = "Game ID is required.")
    private Integer gameId;

    @NotNull(message = "Client ID is required.")
    private Integer clientId;

    public WishlistRequestDto(Integer gameId, Integer clientId) {
        this.gameId = gameId;
        this.clientId = clientId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
