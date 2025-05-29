package com.demo.DBPBackend.user;

import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.application.UserController;
import com.demo.DBPBackend.user.domain.UserService;
import com.demo.DBPBackend.user.dto.UserRequestDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;
    private List<RestaurantResponseDto> restaurantResponseDtos;
    private List<CommentResponseDto> commentResponseDtos;
    private List<ReviewResponseDto> reviewResponseDtos;

    @BeforeEach
    void setUp() {
        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Test User");
        userResponseDto.setEmail("test@example.com");

        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Updated Name");
        userRequestDto.setEmail("updated@example.com");
        userRequestDto.setPassword("newpassword");

        restaurantResponseDtos = Collections.singletonList(new RestaurantResponseDto());
        commentResponseDtos = Collections.singletonList(new CommentResponseDto());
        reviewResponseDtos = Collections.singletonList(new ReviewResponseDto());
    }

    @Test
    void getMe_ShouldReturnUserResponse() {
        when(userService.getMe()).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.getMe();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).getMe();
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        Long userId = 1L;
        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    void getUser_ShouldReturnUserResponse() {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<UserResponseDto> users = Collections.singletonList(userResponseDto);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser_ShouldReturnNoContent() {
        doNothing().when(userService).updateUser(userRequestDto);

        ResponseEntity<Void> response = userController.updateUser(userRequestDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).updateUser(userRequestDto);
    }

    @Test
    void getFavouriteRestaurants_ShouldReturnRestaurants() {
        when(userService.getFavouriteRestaurants()).thenReturn(restaurantResponseDtos);

        ResponseEntity<List<RestaurantResponseDto>> response = userController.getFavouriteRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getFavouriteRestaurants();
    }

    @Test
    void getOwnedRestaurants_ShouldReturnRestaurants() {
        when(userService.getOwnedRestaurants()).thenReturn(restaurantResponseDtos);

        ResponseEntity<List<RestaurantResponseDto>> response = userController.getOwnedRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getOwnedRestaurants();
    }

    @Test
    void getUserComments_ShouldReturnComments() {
        when(userService.getUserComments()).thenReturn(commentResponseDtos);

        ResponseEntity<List<CommentResponseDto>> response = userController.getUserComments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getUserComments();
    }

    @Test
    void getUserReviews_ShouldReturnReviews() {
        when(userService.getUserReviews()).thenReturn(reviewResponseDtos);

        ResponseEntity<List<ReviewResponseDto>> response = userController.getUserReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getUserReviews();
    }

    @Test
    void getUser_WhenUserNotFound_ShouldThrowException() {
        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class, () -> userController.getUser(999L));
    }

}