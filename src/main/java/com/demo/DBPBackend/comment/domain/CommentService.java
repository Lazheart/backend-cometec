package com.demo.DBPBackend.comment.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.comment.infrastructure.CommentRepository;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.infrastructure.ReviewRepository;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    // Método para obtener todos con paginación
    public Page<CommentResponseDto> getAllComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener todos ordenados por fecha de creación (descendente)
    public Page<CommentResponseDto> getAllCommentsOrderedByCreatedAtDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findAllByOrderByCreatedAtDesc(pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener todos ordenados por fecha de creación (ascendente)
    public Page<CommentResponseDto> getAllCommentsOrderedByCreatedAtAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findAllByOrderByCreatedAtAsc(pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return toCommentResponseDto(comment);
    }

    // Método para obtener comentarios por reseña con paginación
    public Page<CommentResponseDto> getCommentsByReviewId(Long reviewId, int page, int size) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByReviewId(reviewId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener comentarios por reseña ordenados por fecha (descendente)
    public Page<CommentResponseDto> getCommentsByReviewIdOrderedByCreatedAtDesc(Long reviewId, int page, int size) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByReviewIdOrderByCreatedAtDesc(reviewId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener comentarios por reseña ordenados por fecha (ascendente)
    public Page<CommentResponseDto> getCommentsByReviewIdOrderedByCreatedAtAsc(Long reviewId, int page, int size) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByReviewIdOrderByCreatedAtAsc(reviewId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener comentarios por usuario con paginación
    public Page<CommentResponseDto> getCommentsByUserId(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByUserId(userId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener comentarios por usuario ordenados por fecha (descendente)
    public Page<CommentResponseDto> getCommentsByUserIdOrderedByCreatedAtDesc(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para obtener comentarios por usuario ordenados por fecha (ascendente)
    public Page<CommentResponseDto> getCommentsByUserIdOrderedByCreatedAtAsc(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByUserIdOrderByCreatedAtAsc(userId, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    // Método para búsqueda por contenido
    public Page<CommentResponseDto> getCommentsByContent(String content, int page, int size) {
        String normalizedContent = normalizeText(content);
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByContentContaining(normalizedContent, pageable);
        return commentPage.map(this::toCommentResponseDto);
    }

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto) {
        if (dto.getReviewId() == null) {
            throw new ResourceNotFoundException("Review is required");
        }

        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Comment content is required");
        }

        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setReview(review);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return toCommentResponseDto(savedComment);
    }

    @Transactional
    public void updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!comment.getUser().getId().equals(user.getId()) && !authUtils.isAdmin()) {
            throw new UnauthorizedOperationException("You are not authorized to update this comment");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        String email = authUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!comment.getUser().getId().equals(user.getId()) && !authUtils.isAdmin()) {
            throw new UnauthorizedOperationException("You are not authorized to delete this comment");
        }

        commentRepository.deleteById(id);
    }

    // Método auxiliar para normalizar texto
    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
                .replaceAll("[''']", "'")
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setReviewId(comment.getReview().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUserName(comment.getUser().getName());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
