package az.ingress.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.ingress.exception.ErrorMessage.BIND_EXCEPTION;
import static az.ingress.exception.ErrorMessage.CACHE_RETRIEVAL_EXCEPTION;
import static az.ingress.exception.ErrorMessage.CUSTOM_FEIGN_EXCEPTION;
import static az.ingress.exception.ErrorMessage.JSON_FORMAT_EXCEPTION;
import static az.ingress.exception.ErrorMessage.UNEXPECTED_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception ex) {
        log.error("Exception: ", ex);
        return new ErrorResponse(UNEXPECTED_ERROR.getMessage());

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: ", ex);
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(BindException ex) {
        log.error("BindException: ", ex);
        return new ErrorResponse(BIND_EXCEPTION.getMessage());
    }

    @ExceptionHandler(CacheRetrievalException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ErrorResponse handle(CacheRetrievalException ex) {
        log.error("CacheRetrievalException: ", ex);
        return new ErrorResponse(CACHE_RETRIEVAL_EXCEPTION.getMessage());
    }


    @ExceptionHandler(CustomFeignException.class)
    public ErrorResponse handle(CustomFeignException ex) {
        log.error("CustomFeignException: ", ex);
        return new ErrorResponse(CUSTOM_FEIGN_EXCEPTION.getMessage());
    }


}