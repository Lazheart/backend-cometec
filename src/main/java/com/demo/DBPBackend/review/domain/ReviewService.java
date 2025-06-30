package com.demo.DBPBackend.review.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.review.dto.ReviewRequestDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.dto.ReviewUpdateContentDto;
import com.demo.DBPBackend.review.infrastructure.ReviewRepository;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthUtils authUtils;

    public Page<ReviewResponseDto> getReviewByRestaurantId(Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable);
        return reviews.map(this::getReviewResponseDto);
    }

    public ReviewResponseDto getReviewById(Long id) {
        if (!authUtils.isAdmin()) {
            throw new UnauthorizedOperationException("Only an admin can access this review");
        }
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return getReviewResponseDto(review);
    }

    public Page<ReviewResponseDto> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        return reviews.map(this::getReviewResponseDto);
    }

    public Page<ReviewResponseDto> getAllReviewsOrderedByLikes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findAllByOrderByLikesDesc(pageable);
        return reviews.map(this::getReviewResponseDto);
    }

    public Page<ReviewResponseDto> getReviewsByCurrentUser(int page, int size) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        return reviews.map(this::getReviewResponseDto);
    }

    public Page<ReviewResponseDto> getReviewsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return reviews.map(this::getReviewResponseDto);
    }

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        if (reviewRequestDto.getUserId() == null) {
            throw new ResourceNotFoundException("User ID is required");
        }

        if (reviewRequestDto.getRestaurantId() == null) {
            throw new ResourceNotFoundException("Restaurant ID is required");
        }

        if (reviewRequestDto.getContent() == null || reviewRequestDto.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Review content is required");
        }

        User user = userRepository.findById(reviewRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(reviewRequestDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Review review = new Review();
        review.setContent(reviewRequestDto.getContent());
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setCreatedAt(LocalDateTime.now());
        review.setLikes(0);

        Review savedReview = reviewRepository.save(review);
        return getReviewResponseDto(savedReview);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewUpdateContentDto reviewUpdateContentDto) {
        if (reviewUpdateContentDto.getContent() == null || reviewUpdateContentDto.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Review content is required");
        }

        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!authUtils.isAdminOrResourceOwner(review.getUser().getId())) {
            throw new UnauthorizedOperationException("Only the owner can change the content of this review");
        }

        review.setContent(reviewUpdateContentDto.getContent());
        Review savedReview = reviewRepository.save(review);
        return getReviewResponseDto(savedReview);
    }

    @Transactional
    public void likeReview(Long id) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getLikedBy().contains(user)) {
            review.getLikedBy().add(user);
            review.setLikes(review.getLikes() + 1);
            reviewRepository.save(review);
            userRepository.save(user);
        }
    }

    @Transactional
    public void dislikeReview(Long id) {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (review.getLikedBy().contains(user)) {
            review.getLikedBy().remove(user);
            review.setLikes(review.getLikes() - 1);
            reviewRepository.save(review);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteReview(Long id) {
        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!authUtils.isAdminOrResourceOwner(review.getUser().getId())) {
            throw new UnauthorizedOperationException("Only the owner can delete this review");
        }

        reviewRepository.delete(review);
    }

    private ReviewResponseDto getReviewResponseDto(Review review) {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setId(review.getId());
        reviewResponseDto.setOwner(review.getUser().getName());
        reviewResponseDto.setOwnerId(review.getUser().getId());
        reviewResponseDto.setContent(review.getContent());
        reviewResponseDto.setLikes(review.getLikes());
        reviewResponseDto.setCreatedAt(review.getCreatedAt());
        reviewResponseDto.setLikedByUserIds(review.getLikedBy().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));
        return reviewResponseDto;
    }
}
