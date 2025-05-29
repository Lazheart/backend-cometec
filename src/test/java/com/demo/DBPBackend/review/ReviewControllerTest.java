package com.demo.DBPBackend.review;
import com.demo.DBPBackend.review.application.ReviewController;
import com.demo.DBPBackend.review.domain.ReviewService;
import com.demo.DBPBackend.review.dto.ReviewRequestDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.dto.ReviewUpdateContentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewResponseDto reviewResponseDto;

    @BeforeEach
    void setUp() {
        reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setId(1L);
        reviewResponseDto.setOwner("John");
        reviewResponseDto.setOwnerId(100L);
        reviewResponseDto.setCreatedAt(LocalDateTime.now());
        reviewResponseDto.setLikes(3);
        reviewResponseDto.setContent("Great place!");
        reviewResponseDto.setLikedByUserIds(Set.of(200L, 201L));
    }

    @Test
    void testGetReviewByRestaurantId() {
        Long restaurantId = 1L;
        when(reviewService.getReviewByRestaurantId(restaurantId))
                .thenReturn(List.of(reviewResponseDto));

        ResponseEntity<List<ReviewResponseDto>> response =
                reviewController.getReviewByRestaurantId(restaurantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(reviewService).getReviewByRestaurantId(restaurantId);
    }

    @Test
    void testCreateReview() {
        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setUserId(1L);
        dto.setRestaurantId(2L);
        dto.setContent("Amazing food!");

        ResponseEntity<Void> response = reviewController.createReview(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(reviewService).createReview(dto);
    }

    @Test
    void testLikeReview() {
        Long reviewId = 1L;

        ResponseEntity<Void> response = reviewController.likeReview(reviewId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reviewService).likeReview(reviewId);
    }

    @Test
    void testDislikeReview() {
        Long reviewId = 1L;

        ResponseEntity<Void> response = reviewController.dislikeReview(reviewId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reviewService).dislikeReview(reviewId);
    }

    @Test
    void testUpdateReviewContent() {
        Long reviewId = 1L;
        ReviewUpdateContentDto contentDto = new ReviewUpdateContentDto();
        contentDto.setContent("Updated content");

        ResponseEntity<Void> response = reviewController.updateReviewContent(reviewId, contentDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reviewService).changeContent(reviewId, "Updated content");
    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L;

        ResponseEntity<Void> response = reviewController.deleteReview(reviewId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reviewService).deleteReview(reviewId);
    }
}
