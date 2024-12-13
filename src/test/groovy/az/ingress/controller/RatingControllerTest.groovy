package az.ingress.controller


import az.ingress.exception.ErrorHandler
import az.ingress.model.request.RatingRequest
import az.ingress.service.abstraction.RatingService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


class RatingControllerTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    private RatingService ratingService
    private MockMvc mockMvc

    void setup() {
        ratingService = Mock()
        def ratingController = new RatingController(ratingService)
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "TestInsertOrUpdateRating"() {
        given:
        def url = "/v1/ratings"
        def accessToken = random.nextObject(String)
        def ratingRequest = new RatingRequest(1L, 3L, 3)
        def requestBody = """   
                                {
                                 "userId": "$ratingRequest.userId",
                                 "productId": "$ratingRequest.productId",
                                 "score": "$ratingRequest.score"
                                 }                                
                                """

        when:
        def result = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn()


        then:
        1 * ratingService.insertOrUpdateRating(ratingRequest)

        def response = result.response
        response.status == HttpStatus.CREATED.value()

    }

    def "TestGetUserRating"() {
        given:
        def productId = random.nextObject(Long)
        def accessToken = random.nextObject(String)
        def userId = random.nextObject(Long)
        def url = "/v1/ratings"

        when:
         mockMvc.perform(get(url)
                .header("Authorization", accessToken)
                .param("productId", productId.toString())
                .param("userId", userId.toString()))
                 .andReturn()
        then:
        1 * ratingService.getUserRating(productId, userId)


    }

    def "TestDeleteRating"(){
        given:
        def productId = random.nextObject(Long)
        def accessToken = random.nextObject(String)
        def userId = random.nextObject(Long)
        def url = "/v1/ratings"

        when:
        def result = mockMvc.perform(delete(url)
                .header("Authorization", accessToken)
                .param("productId", productId.toString())
                .param("userId", userId.toString()))
                .andReturn()

        then:
        1 * ratingService.deleteRating(productId,userId)
        def response = result.response
        response.status == HttpStatus.NO_CONTENT.value()
    }


}
