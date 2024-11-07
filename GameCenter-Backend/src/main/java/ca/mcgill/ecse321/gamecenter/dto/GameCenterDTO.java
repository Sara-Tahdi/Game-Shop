package ca.mcgill.ecse321.gamecenter.dto;
import java.sql.Time;
import ca.mcgill.ecse321.gamecenter.model.GameCenter;

public class GameCenterDTO {
    private String name;
    private Time open;
    private Time close;
    private String storePolicy;

    @SuppressWarnings("unused")
    public GameCenterDTO() {
    }

    public GameCenterDTO(GameCenter gameCenter) {
        this.name = gameCenter.getName();
        this.open = gameCenter.getOpen();
        this.close = gameCenter.getClose();
        this.storePolicy = gameCenter.getStorePolicy();
    }

    public String getName() {
        return name;
    }

    public Time getOpen() {
        return open;
    }

    public Time getClose() {
        return close;
    }

    public String getStorePolicy() {
        return storePolicy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpen(Time open) {
        this.open = open;
    }

    public void setClose(Time close) {
        this.close = close;
    }

    public void setStorePolicy(String storePolicy) {
        this.storePolicy = storePolicy;
    }

}
