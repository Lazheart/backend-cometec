package com.demo.DBPBackend.user.application;

import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.domain.UserService;
import com.demo.DBPBackend.user.dto.UserPublicUpdateDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import com.demo.DBPBackend.user.dto.UserUpdateProfileImageDto;
import com.demo.DBPBackend.user.dto.UserSecurityUpdateDto;
import com.demo.DBPBackend.auth.dto.RecoveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    //@PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PatchMapping("/security/me")
    public ResponseEntity<RecoveryResponseDto> updatePasswordWithCode(@RequestBody UserSecurityUpdateDto dto) {
        return ResponseEntity.ok(userService.updatePasswordWithCode(dto));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PatchMapping("/update/me")
    public ResponseEntity<Void> updatePublicInfo(@RequestBody UserPublicUpdateDto updatedInfo) {
        userService.updatePublicUserInfo(updatedInfo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getUserReviews(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserReviews(page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUserComments(page, size));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/owned-restaurants")
    public ResponseEntity<Page<RestaurantResponseDto>> getOwnedRestaurants(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getOwnedRestaurants(page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/favourites")
    public ResponseEntity<Page<RestaurantResponseDto>> getFavouriteRestaurants(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getFavouriteRestaurants(page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PutMapping(value = "/{id}/profile-image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponseDto> updateProfileImage(
            @PathVariable Long id,
            @ModelAttribute UserUpdateProfileImageDto userUpdateProfileImageDto) throws FileUploadException {
        UserResponseDto updatedUser = userService.updateProfileImage(id, userUpdateProfileImageDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateProfileImage(@ModelAttribute UserUpdateProfileImageDto dto) throws FileUploadException {
        UserResponseDto updatedUser = userService.updateProfileImage(dto);
        return ResponseEntity.ok(updatedUser);
    }
}
