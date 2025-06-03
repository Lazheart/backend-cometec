package com.demo.DBPBackend.comment.domain;

import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.comment.infrastructure.CommentRepository;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.infrastructure.ReviewRepository;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.TypeCollector;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        Review review = reviewRepository.findById(commentRequestDto.getReviewId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setReview(review);
        User user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(savedComment.getId());
        responseDto.setContent(savedComment.getContent());
        responseDto.setReviewId(savedComment.getReview().getId());
        responseDto.setUserId(savedComment.getUser().getId());
        return responseDto;
    }

    public List<CommentResponseDto> getCommentsByReviewId(Long reviewId) {
        List<Comment> comments = commentRepository.findByReviewId(reviewId);

        return comments.stream().map(comment -> {
            CommentResponseDto commentResponseDto = new CommentResponseDto();
            commentResponseDto.setId(comment.getId());
            commentResponseDto.setContent(comment.getContent());
            commentResponseDto.setReviewId(comment.getReview().getId());
            commentResponseDto.setUserId(comment.getUser().getId());
            return commentResponseDto;
        }).collect(Collectors.toList());
    }


    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(() -> new RuntimeException("Comment not found"));
        Review review = comment.getReview();
        review.getComments().remove(comment);
        reviewRepository.save(review);
        User user = comment.getUser();
        user.getComments().remove(comment);
        userRepository.save(user);
        commentRepository.delete(comment);
    }

}
