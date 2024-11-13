package ca.mcgill.ecse321.gamecenter.dto.Review;

import jakarta.validation.constraints.NotBlank;

public class ManagerReplyRequestDTO {
    @NotBlank(message = "Manager reply is required")
    private String reply;

    public ManagerReplyRequestDTO() {}

    public ManagerReplyRequestDTO(String reply) {
        this.reply = reply;
    }

    public String getReply() { return reply; }
}
