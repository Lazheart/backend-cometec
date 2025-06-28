package com.demo.DBPBackend.review.infrastructure;

import com.demo.DBPBackend.review.domain.Review;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantId(Long restaurantId);

    List<Review> findByUserId(Long userId);

    List<Review> findAllByOrderByCreatedAtDesc();

    Page<Review> findAll(Pageable pageable);
    
    Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
    
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Review> findAllByOrderByLikesDesc(Pageable pageable);
    
    Page<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);
    Page<Review> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
