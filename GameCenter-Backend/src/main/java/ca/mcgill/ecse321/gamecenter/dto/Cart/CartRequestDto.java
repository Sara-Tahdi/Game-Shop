package ca.mcgill.ecse321.gamecenter.dto.Cart;

import jakarta.validation.constraints.Min;

public class CartRequestDto {
    @Min(value = 1, message = "GameId is required")
    private int gameId;

    @Min(value = 1, message = "Client is required.")
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
