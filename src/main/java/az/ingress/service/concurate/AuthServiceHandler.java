package az.ingress.service.concurate;

import az.ingress.client.AuthClient;
import az.ingress.service.abstraction.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "authServiceHandler")
@RequiredArgsConstructor
public class AuthServiceHandler implements AuthService {
    private final AuthClient authClient;

    @Override
    public void verify(String token) {
        log.info("ActionLog.User.Token: {}", token);
        authClient.verify(token);
    }

}
