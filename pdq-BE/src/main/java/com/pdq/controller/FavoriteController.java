package com.pdq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdq.dto.common.ApiResponse;
import com.pdq.dto.common.PageResponse;
import com.pdq.dto.favorite.FavoriteResponse;
import com.pdq.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FavoriteResponse>>> getMyFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        PageResponse<FavoriteResponse> favorites = favoriteService.getMyFavorites(
                userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Favorites retrieved successfully", favorites));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getFavoriteCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        long count = favoriteService.getFavoriteCount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Favorite count retrieved successfully", count));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> isFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        boolean isFavorite = favoriteService.isFavorite(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Favorite status retrieved successfully", isFavorite));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addToFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        FavoriteResponse favorite = favoriteService.addToFavorites(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Product added to favorites successfully", favorite));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        favoriteService.removeFromFavorites(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Product removed from favorites successfully", null));
    }

    @PutMapping("/{productId}/toggle")
    public ResponseEntity<ApiResponse<Void>> toggleFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        favoriteService.toggleFavorite(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Favorite toggled successfully", null));
    }
}