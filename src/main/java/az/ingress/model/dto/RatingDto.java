package az.ingress.model.dto;

import az.ingress.model.enums.RatingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {
    private Long userId;
    private Long productId;
    private Integer score;
    private RatingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
