package ca.mcgill.ecse321.gamecenter.dto.Cart;

import ca.mcgill.ecse321.gamecenter.model.Cart;

public class CartResponseDto {

    private int id;
    private int gameId;
    private int clientId;

    public CartResponseDto() {
    }

    public CartResponseDto(Cart cart) {
        this.id = cart.getId();
        this.gameId = cart.getGame().getId();
        this.clientId = cart.getClient().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
