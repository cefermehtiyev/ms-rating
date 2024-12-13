package az.ingress.service.abstraction;

public interface AuthService {
    void verify(String token);
}