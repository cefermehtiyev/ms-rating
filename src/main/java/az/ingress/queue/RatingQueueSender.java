package az.ingress.queue;

import az.ingress.configuration.QueueProperties;
import az.ingress.model.queue.RatingQueueDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static az.ingress.mapper.factory.ObjectMapperFactory.OBJECT_MAPPER_FACTORY;

@Component
@RequiredArgsConstructor
public class RatingQueueSender implements QueueSender<RatingQueueDto> {
    private final AmqpTemplate amqpTemplate;
    private final QueueProperties queueProperties;

    @Override
    @SneakyThrows
    public void sendToQueue(RatingQueueDto rating) {
        amqpTemplate.convertAndSend(queueProperties.getRatingQ(), OBJECT_MAPPER_FACTORY.createObjectMapper().writeValueAsString(rating));
    }
}
