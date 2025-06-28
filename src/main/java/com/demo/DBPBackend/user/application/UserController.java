package com.demo.DBPBackend.user.application;

import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.UserService;
import com.demo.DBPBackend.user.dto.UserPublicUpdateDto;
import com.demo.DBPBackend.user.dto.UserRequestDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/name")
    public ResponseEntity<Page<UserResponseDto>> getAllUsersOrderedByName(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsersOrderedByName(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/created-at")
    public ResponseEntity<Page<UserResponseDto>> getAllUsersOrderedByCreatedAt(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsersOrderedByCreatedAt(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/email")
    public ResponseEntity<Page<UserResponseDto>> getAllUsersOrderedByEmail(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsersOrderedByEmail(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name")
    public ResponseEntity<Page<UserResponseDto>> getUsersByName(@RequestParam String name,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByName(name, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lastname")
    public ResponseEntity<Page<UserResponseDto>> getUsersByLastname(@RequestParam String lastname,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByLastname(lastname, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email")
    public ResponseEntity<Page<UserResponseDto>> getUsersByEmail(@RequestParam String email,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByEmail(email, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role")
    public ResponseEntity<Page<UserResponseDto>> getUsersByRole(@RequestParam Role role,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByRole(role, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/created-at")
    public ResponseEntity<Page<UserResponseDto>> getUsersByRoleOrderedByCreatedAt(@RequestParam Role role,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByRoleOrderedByCreatedAt(role, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/name")
    public ResponseEntity<Page<UserResponseDto>> getUsersByRoleOrderedByName(@RequestParam Role role,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByRoleOrderedByName(role, page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PutMapping("/me")
    public ResponseEntity<Void> updateMe(@RequestBody UserRequestDto updatedUser) {
        userService.updateUser(updatedUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PatchMapping("/me")
    public ResponseEntity<Void> updatePublicInfo(@RequestBody UserPublicUpdateDto updatedInfo) {
        userService.updatePublicUserInfo(updatedInfo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/me/favourites")
    public ResponseEntity<Page<RestaurantResponseDto>> getFavouriteRestaurants(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getFavouriteRestaurants(page, size));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/me/owned")
    public ResponseEntity<Page<RestaurantResponseDto>> getOwnedRestaurants(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getOwnedRestaurants(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserComments(page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getUserReviews(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserReviews(page, size));
    }
}
