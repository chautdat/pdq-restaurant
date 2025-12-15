package com.pdq.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.common.PageResponse;
import com.pdq.dto.favorite.FavoriteResponse;
import com.pdq.dto.product.ProductResponse;
import com.pdq.entity.Favorite;
import com.pdq.entity.Product;
import com.pdq.entity.User;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.FavoriteRepository;
import com.pdq.repository.ProductRepository;
import com.pdq.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public PageResponse<FavoriteResponse> getMyFavorites(String userEmail, int page, int size) {
        User user = getUserByEmail(userEmail);
        Pageable pageable = PageRequest.of(page, size);
        Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return mapToPageResponse(favoritePage);
    }

    public long getFavoriteCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        return favoriteRepository.countByUser(user);
    }

    public boolean isFavorite(String userEmail, Long productId) {
        User user = getUserByEmail(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return favoriteRepository.existsByUserAndProduct(user, product);
    }

    @Transactional
    public FavoriteResponse addToFavorites(String userEmail, Long productId) {
        User user = getUserByEmail(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if already exists
        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException("Product is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        favorite = favoriteRepository.save(favorite);
        return mapToResponse(favorite);
    }

    @Transactional
    public void removeFromFavorites(String userEmail, Long productId) {
        User user = getUserByEmail(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Transactional
    public void toggleFavorite(String userEmail, Long productId) {
        User user = getUserByEmail(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserAndProduct(user, product);
        
        if (favoriteOpt.isPresent()) {
            // Remove from favorites
            favoriteRepository.delete(favoriteOpt.get());
        } else {
            // Add to favorites
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private FavoriteResponse mapToResponse(Favorite favorite) {
        FavoriteResponse response = new FavoriteResponse();
        response.setFavoriteId(favorite.getFavoriteId());
        response.setProduct(mapProductToResponse(favorite.getProduct()));
        response.setCreatedAt(favorite.getCreatedAt());
        return response;
    }

    private ProductResponse mapProductToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setProductSlug(product.getProductSlug());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setShortDescription(product.getShortDescription());
        response.setPrice(product.getPrice());
        response.setDiscountPrice(product.getDiscountPrice());
        response.setImageUrl(product.getImageUrl());
        // ❌ REMOVED: response.setImages(convertJsonToList(product.getImages()));
        response.setStockQuantity(product.getStockQuantity());
        response.setUnit(product.getUnit());
        response.setPreparationTime(product.getPreparationTime());
        response.setIsFeatured(product.getIsFeatured());
        response.setIsAvailable(product.getIsAvailable());
        response.setAverageRating(product.getAverageRating());
        response.setReviewCount(product.getReviewCount());
        response.setSoldCount(product.getSoldCount());
        response.setViewCount(product.getViewCount());
        response.setCreatedAt(product.getCreatedAt());
        return response;
    }

    private PageResponse<FavoriteResponse> mapToPageResponse(Page<Favorite> page) {
        List<FavoriteResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // ❌ REMOVED: convertJsonToList() method
    // ❌ REMOVED: ObjectMapper dependency
}