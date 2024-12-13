package az.ingress.controller;

import az.ingress.model.request.RatingRequest;
import az.ingress.model.response.RatingResponse;
import az.ingress.service.abstraction.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ratings")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(CREATED)
//    @PreAuthorize("@authServiceHandler.verify(#accessToken)")
//    @RequestHeader(AUTHORIZATION) String accessToken
    public void insertOrUpdateRating(@Valid @RequestBody RatingRequest ratingRequest) {
        ratingService.insertOrUpdateRating(ratingRequest);
    }


    @GetMapping
//    @PreAuthorize("@authServiceHandler.verify(#accessToken)")
    public RatingResponse getUserRating(@RequestParam Long productId, @RequestParam Long userId) {
        return ratingService.getUserRating(productId, userId);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
//    @PreAuthorize("@authServiceHandler.verify(#accessToken)")
    public void deleteRating(@RequestParam Long productId, @RequestParam Long userId) {
        ratingService.deleteRating(productId, userId);
    }


}
