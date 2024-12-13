package az.ingress.exception;

public class CustomFeignException extends RuntimeException{
    int status;

    public CustomFeignException(String message, int status) {
        super(message);
        this.status = status;
    }
}
