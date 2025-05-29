package com.demo.DBPBackend.restaurant;

import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.restaurant.application.RestaurantController;
import com.demo.DBPBackend.restaurant.domain.RestaurantService;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.ubicacion.dto.UbicacionDto;
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
public class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private RestaurantRequestDto requestDto;
    private RestaurantResponseDto responseDto;
    private RestaurantSummaryDto summaryDto;
    private UbicacionDto ubicacionDto;
    private MenuResponseDto menuResponseDto;
    private List<ReviewResponseDto> reviewList;
    private List<CommentResponseDto> commentList;

    @BeforeEach
    void setUp() {
        ubicacionDto = new UbicacionDto();
        ubicacionDto.setLatitud(10.0);
        ubicacionDto.setLongitud(20.0);

        requestDto = new RestaurantRequestDto();
        requestDto.setName("Test Restaurant");
        requestDto.setUbicacion(ubicacionDto);

        responseDto = new RestaurantResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test Restaurant");
        responseDto.setOwnerId(1L);
        responseDto.setOwnerName("Owner");
        responseDto.setUbicacion(ubicacionDto);
        responseDto.setTotalReviews(5);
        responseDto.setHasMenu(true);

        summaryDto = new RestaurantSummaryDto();
        summaryDto.setId(1L);
        summaryDto.setName("Test Restaurant");
        summaryDto.setOwnerName("Owner");
        summaryDto.setUbicacion(ubicacionDto);
        summaryDto.setTotalReviews(5);

        menuResponseDto = new MenuResponseDto();
        menuResponseDto.setId(1L);
        menuResponseDto.setRestaurantId(1L);
        menuResponseDto.setRestaurantName("Test Restaurant");

        reviewList = Collections.singletonList(new ReviewResponseDto());
        commentList = Collections.singletonList(new CommentResponseDto());
    }

    @Test
    void getAllRestaurants_ShouldReturnList() {
        when(restaurantService.getAllRestaurants()).thenReturn(Collections.singletonList(summaryDto));

        ResponseEntity<List<RestaurantSummaryDto>> response = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() {
        when(restaurantService.getRestaurantById(1L)).thenReturn(responseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.getRestaurantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(restaurantService, times(1)).getRestaurantById(1L);
    }

    @Test
    void getRestaurantReviews_ShouldReturnReviews() {
        when(restaurantService.getRestaurantReviews(1L)).thenReturn(reviewList);

        ResponseEntity<List<ReviewResponseDto>> response = restaurantController.getRestaurantReviews(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(restaurantService, times(1)).getRestaurantReviews(1L);
    }

    @Test
    void getRestaurantComments_ShouldReturnComments() {
        when(restaurantService.getRestaurantComments(1L)).thenReturn(commentList);

        ResponseEntity<List<CommentResponseDto>> response = restaurantController.getRestaurantComments(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(restaurantService, times(1)).getRestaurantComments(1L);
    }

    @Test
    void getRestaurantMenu_ShouldReturnMenu() {
        when(restaurantService.getRestaurantMenu(1L)).thenReturn(menuResponseDto);

        ResponseEntity<MenuResponseDto> response = restaurantController.getRestaurantMenu(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuResponseDto, response.getBody());
        verify(restaurantService, times(1)).getRestaurantMenu(1L);
    }

    @Test
    void createRestaurant_ShouldReturnCreated() {
        doNothing().when(restaurantService).createRestaurant(requestDto);

        ResponseEntity<Void> response = restaurantController.createRestaurant(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(restaurantService, times(1)).createRestaurant(requestDto);
    }

    @Test
    void updateRestaurant_ShouldReturnOk() {
        doNothing().when(restaurantService).updateRestaurant(1L, requestDto);

        ResponseEntity<Void> response = restaurantController.updateRestaurant(1L, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(restaurantService, times(1)).updateRestaurant(1L, requestDto);
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent() {
        doNothing().when(restaurantService).deleteRestaurant(1L);

        ResponseEntity<Void> response = restaurantController.deleteRestaurant(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteRestaurant(1L);
    }
}
