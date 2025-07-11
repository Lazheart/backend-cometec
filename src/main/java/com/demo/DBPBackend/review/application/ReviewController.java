package com.demo.DBPBackend.review.application;

import com.demo.DBPBackend.review.domain.ReviewService;
import com.demo.DBPBackend.review.dto.ReviewRequestDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.dto.ReviewUpdateContentDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.comment.domain.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getAllReviews(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByReview(@PathVariable Long reviewId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewRequestDto));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/content/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long id,
                                                          @RequestBody ReviewUpdateContentDto reviewUpdateContentDto) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewUpdateContentDto));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
