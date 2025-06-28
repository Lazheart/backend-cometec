package com.demo.DBPBackend.comment.application;

import com.demo.DBPBackend.comment.domain.CommentService;
import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getAllComments(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/created-desc")
    public ResponseEntity<Page<CommentResponseDto>> getAllCommentsOrderedByCreatedAtDesc(@RequestParam(defaultValue = "0") int page,
                                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getAllCommentsOrderedByCreatedAtDesc(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/created-asc")
    public ResponseEntity<Page<CommentResponseDto>> getAllCommentsOrderedByCreatedAtAsc(@RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getAllCommentsOrderedByCreatedAtAsc(page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByReview(@PathVariable Long reviewId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId, page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/review/{reviewId}/created-desc")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByReviewOrderedByCreatedAtDesc(@PathVariable Long reviewId,
                                                                                               @RequestParam(defaultValue = "0") int page,
                                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByReviewIdOrderedByCreatedAtDesc(reviewId, page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/review/{reviewId}/created-asc")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByReviewOrderedByCreatedAtAsc(@PathVariable Long reviewId,
                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByReviewIdOrderedByCreatedAtAsc(reviewId, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUser(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}/created-desc")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUserOrderedByCreatedAtDesc(@PathVariable Long userId,
                                                                                             @RequestParam(defaultValue = "0") int page,
                                                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByUserIdOrderedByCreatedAtDesc(userId, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}/created-asc")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUserOrderedByCreatedAtAsc(@PathVariable Long userId,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByUserIdOrderedByCreatedAtAsc(userId, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/content")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByContent(@RequestParam String content,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsByContent(content, page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto dto) {
        commentService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @RequestBody String content) {
        commentService.updateComment(id, content);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
