package com.demo.DBPBackend.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long reviewId;
    private Long userId;
    private String userName;
    private String userLastname;
    private LocalDateTime createdAt;
}
