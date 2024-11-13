package ca.mcgill.ecse321.gamecenter.dto.Promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PromotionUpdateRequestDTO {
    @NotNull(message = "New Price is required")
    @Min(0)
    private float newPrice;
    @NotNull(message = "Start Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")  // Add format annotation
    private LocalDate startDate;
    @NotNull(message = "End Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")  // Add format annotation
    private LocalDate endDate;
    @NotNull(message = "Game ID is required")
    @Min(1)
    private int gameId;
}
