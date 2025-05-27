package com.demo.DBPBackend.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRequestDto {
    private String content;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createdAt;
}
