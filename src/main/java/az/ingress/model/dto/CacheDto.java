package az.ingress.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheDto implements Serializable {
    private final static long serialVersionUID = 1L;
    private Integer voteCount;
    private BigDecimal averageRating;
}
