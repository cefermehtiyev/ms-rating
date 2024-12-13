package az.ingress.client;


import az.ingress.decoder.CustomErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "ms-auth", url = "${client.urls.ms-auth}", configuration = CustomErrorDecoder.class)
public interface AuthClient {

    @PostMapping("internal/v1/auth/verify")
    void verify(@RequestHeader(AUTHORIZATION) String accessToken);
}