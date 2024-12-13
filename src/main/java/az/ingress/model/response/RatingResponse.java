package az.ingress.model.response;

import az.ingress.model.enums.RatingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer score;
    private RatingStatus status;
}
