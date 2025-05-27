package com.demo.DBPBackend.review.application;

import com.demo.DBPBackend.review.domain.ReviewService;
import com.demo.DBPBackend.review.dto.ReviewRequestDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.dto.ReviewUpdateContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewByRestaurantId(restaurantId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<ReviewResponseDto> response = reviewService.getAllReviews();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByCurrentUser() {
        List<ReviewResponseDto> response = reviewService.getReviewsByCurrentUser();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewResponseDto> response = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Void> createReview(@ModelAttribute ReviewRequestDto reviewRequestDto){
        reviewService.createReview(reviewRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/content/{id}")
    public ResponseEntity<Void> updateReviewContent(@PathVariable Long id, @RequestBody ReviewUpdateContentDto content) {
        reviewService.changeContent(id, content.getContent());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/dislike/{id}")
    public ResponseEntity<Void> dislikeReview(@PathVariable Long id) {
        reviewService.dislikeReview(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likeReview(@PathVariable Long id) {
        reviewService.likeReview(id);
        return ResponseEntity.noContent().build();
    }
    

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    
}
