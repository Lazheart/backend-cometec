package com.demo.DBPBackend.comment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createdAt;
}
