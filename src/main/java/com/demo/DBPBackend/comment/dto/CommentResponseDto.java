package com.demo.DBPBackend.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createdAt;
}
