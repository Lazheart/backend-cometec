package com.demo.DBPBackend.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String content;
    private Long userId;
    private Long reviewId;
}
