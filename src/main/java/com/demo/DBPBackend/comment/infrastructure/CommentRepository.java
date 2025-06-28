package com.demo.DBPBackend.comment.infrastructure;

import com.demo.DBPBackend.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewId(Long reviewId);
    List<Comment> findByUserId(Long userId);

    Page<Comment> findAll(Pageable pageable);
    
    Page<Comment> findByReviewId(Long reviewId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    Page<Comment> findByContentContaining(String content, Pageable pageable);
    
    Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Comment> findAllByOrderByCreatedAtAsc(Pageable pageable);
    
    Page<Comment> findByReviewIdOrderByCreatedAtDesc(Long reviewId, Pageable pageable);
    Page<Comment> findByReviewIdOrderByCreatedAtAsc(Long reviewId, Pageable pageable);
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Comment> findByUserIdOrderByCreatedAtAsc(Long userId, Pageable pageable);
}
