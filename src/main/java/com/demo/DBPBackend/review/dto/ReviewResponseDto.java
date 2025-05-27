package com.demo.DBPBackend.review.dto;

import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ReviewResponseDto {
    private Long id;
    private String owner;
    private Long ownerId;
    private LocalDateTime createdAt;
    private Integer likes;
    private String content;
    private Set<Long> likedByUserIds;
    private List<CommentResponseDto> comments;
}
