package az.ingress.exception;

import liquibase.pro.packaged.R;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    UNEXPECTED_ERROR("Unexpected error occurred"),
    RATING_NOT_FOUND("Rating not found"),
    BIND_EXCEPTION("Validation failed"),
    CACHE_NOT_FOUND("Cache not found"),
    CACHE_RETRIEVAL_EXCEPTION("Cache retrieval failed"),
    VALIDATION_EXCEPTION("Rating must be between 1 and 5"),
    JSON_FORMAT_EXCEPTION("The data is not in JSON format"),
    CUSTOM_FEIGN_EXCEPTION("Custom Feign Exception"),
    CLIENT_EXCEPTION("CLIENT_EXCEPTION");

    private final String message;
}