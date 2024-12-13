package az.ingress.decoder;

import az.ingress.exception.CustomFeignException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import static az.ingress.decoder.JsonNodeFieldName.MESSAGE;
import static az.ingress.exception.ErrorMessage.CLIENT_EXCEPTION;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String message = CLIENT_EXCEPTION.getMessage();
        int status = response.status();

        JsonNode node;
        try (var body = response.body().asInputStream()) {
            node = new ObjectMapper().readValue(body, JsonNode.class);

        } catch (Exception e) {
            throw new CustomFeignException(message, status);
        }

        if (node.has(MESSAGE.getValue())) {
            message = node.get(MESSAGE.getValue()).asText();
        }

        return new CustomFeignException(message, status);
    }
}
