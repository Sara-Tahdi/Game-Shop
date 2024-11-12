package ca.mcgill.ecse321.gamecenter.dto.Wishlist;

import ca.mcgill.ecse321.gamecenter.model.Wishlist;

public class WishlistResponseDto {

    private int id;
    private Integer gameId;
    private Integer clientId;

    protected WishlistResponseDto() {}

    public WishlistResponseDto(Wishlist wishlist) {
        this.id = wishlist.getId();
        this.gameId = wishlist.getGame().getId();
        this.clientId = wishlist.getClient().getId();
    }

    public int getId() {
        return id;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getClientId() {
        return clientId;
    }
}