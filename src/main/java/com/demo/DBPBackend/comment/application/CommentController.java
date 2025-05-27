package com.demo.DBPBackend.comment.application;

import com.demo.DBPBackend.comment.domain.CommentService;
import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long reviewId) {
        return ResponseEntity.ok(commentService.getCommentsByReviewId(reviewId));
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        commentService.createComment(commentRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
