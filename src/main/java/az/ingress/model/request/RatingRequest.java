package az.ingress.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static az.ingress.model.constants.ValidationMessages.FIELD_CANNOT_BE_NULL;
import static az.ingress.model.constants.ValidationMessages.RATING_MAX_VALUE;
import static az.ingress.model.constants.ValidationMessages.RATING_MIN_VALUE;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @NotNull(message = FIELD_CANNOT_BE_NULL)
    private Long userId;

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    private Long productId;

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    @Min(value = 1, message = RATING_MIN_VALUE)
    @Max(value = 5, message = RATING_MAX_VALUE)
    private Integer score;
}
