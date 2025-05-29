package com.demo.DBPBackend.comment;

import com.demo.DBPBackend.comment.application.CommentController;
import com.demo.DBPBackend.comment.domain.CommentService;
import com.demo.DBPBackend.comment.dto.CommentRequestDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentRequestDto requestDto;
    private CommentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new CommentRequestDto();
        requestDto.setContent("Comentario de prueba");
        requestDto.setUserId(1L);
        requestDto.setReviewId(2L);

        responseDto = new CommentResponseDto();
        responseDto.setId(10L);
        responseDto.setContent("Comentario de prueba");
        responseDto.setUserId(1L);
        responseDto.setReviewId(2L);
    }

    @Test
    void testCreateComment() {
        ResponseEntity<Void> response = commentController.createComment(requestDto);

        verify(commentService).createComment(requestDto);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetCommentsByReviewId() {
        when(commentService.getCommentsByReviewId(2L)).thenReturn(List.of(responseDto));

        ResponseEntity<List<CommentResponseDto>> response = commentController.getCommentsByPostId(2L);

        verify(commentService).getCommentsByReviewId(2L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Comentario de prueba", response.getBody().get(0).getContent());
    }

    @Test
    void testDeleteComment() {
        ResponseEntity<Void> response = commentController.deleteComment(10L);

        verify(commentService).deleteComment(10L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
