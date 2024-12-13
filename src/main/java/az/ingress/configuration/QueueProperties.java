package az.ingress.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("queue")
public class QueueProperties {
    private String ratingQ;
}
