package com.demo.DBPBackend.review.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.review.dto.ReviewRequestDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.infrastructure.ReviewRepository;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public List<ReviewResponseDto> getReviewByRestaurantId(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        return reviews.stream()
                .map(this::getReviewResponseDto)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto getReviewById(Long id) {
        if (!authUtils.isAdmin()) {
            throw new UnauthorizedOperationException("Only an admin can access this review");
        }
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return getReviewResponseDto(review);
    }

    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        return reviews.stream()
                .map(this::getReviewResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByCurrentUser() {
        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream()
                .map(this::getReviewResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(this::getReviewResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createReview(ReviewRequestDto reviewRequestDto) {
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

        reviewRepository.save(review);
    }

    @Transactional
    public void changeContent(Long id, String content) {
        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!authUtils.isAdminOrResourceOwner(review.getUser().getId())) {
            throw new UnauthorizedOperationException("Only the owner can change the content of this review");
        }

        review.setContent(content);
        reviewRepository.save(review);
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
}
