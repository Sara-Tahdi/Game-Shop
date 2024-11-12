package ca.mcgill.ecse321.gamecenter.dto.Cart;

import jakarta.validation.constraints.NotNull;

public class CartRequestDto {

    @NotNull(message = "Game is required.")
    private int gameId;

    @NotNull(message = "Client is required.")
    private int clientId;

    public CartRequestDto(int gameId, int clientId) {
        this.gameId = gameId;
        this.clientId = clientId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getClientId() {
        return clientId;
    }
}
