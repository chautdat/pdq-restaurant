package com.pdq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdq.dto.common.ApiResponse;
import com.pdq.dto.promo.PromoCodeDTO;
import com.pdq.dto.promo.PromoCodeValidateRequest;
import com.pdq.dto.promo.PromoCodeValidateResponse;
import com.pdq.entity.PromoCode;
import com.pdq.entity.User;
import com.pdq.exception.BadRequestException;
import com.pdq.service.PromoCodeService;
import com.pdq.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;
    private final UserRepository userRepository;

    public PromoCodeController(PromoCodeService promoCodeService, UserRepository userRepository) {
        this.promoCodeService = promoCodeService;
        this.userRepository = userRepository;
    }

    // ========================================
    // ADMIN ENDPOINTS
    // ========================================

    /**
     * CREATE PROMO CODE (ADMIN ONLY)
     */
    @PostMapping("/admin/promo-codes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> createPromoCode(
            @Valid @RequestBody PromoCodeDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        PromoCodeDTO result = promoCodeService.createPromoCode(dto, userDetails.getUsername());
        
        return ResponseEntity.ok(
            ApiResponse.success("Mã giảm giá đã được tạo thành công", result)
        );
    }

    /**
     * UPDATE PROMO CODE (ADMIN ONLY)
     */
    @PutMapping("/admin/promo-codes/{promoCodeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> updatePromoCode(
            @PathVariable Long promoCodeId,
            @Valid @RequestBody PromoCodeDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        PromoCodeDTO result = promoCodeService.updatePromoCode(promoCodeId, dto, userDetails.getUsername());
        
        return ResponseEntity.ok(
            ApiResponse.success("Mã giảm giá đã được cập nhật", result)
        );
    }

    /**
     * TOGGLE PROMO CODE ACTIVE/INACTIVE (ADMIN ONLY)
     */
    @PostMapping("/admin/promo-codes/{promoCodeId}/toggle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> togglePromoCode(
            @PathVariable Long promoCodeId) {

        PromoCodeDTO result = promoCodeService.togglePromoCode(promoCodeId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Trạng thái mã giảm giá đã được cập nhật", result)
        );
    }

    /**
     * DELETE PROMO CODE (ADMIN ONLY)
     */
    @DeleteMapping("/admin/promo-codes/{promoCodeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePromoCode(
            @PathVariable Long promoCodeId) {

        promoCodeService.deletePromoCode(promoCodeId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Mã giảm giá đã được xóa", null)
        );
    }

    /**
     * GET ALL PROMO CODES (ADMIN ONLY)
     */
    @GetMapping("/admin/promo-codes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<PromoCodeDTO>>> getAllPromoCodes() {

        List<PromoCodeDTO> codes = promoCodeService.getAllPromoCodes();
        
        return ResponseEntity.ok(
            ApiResponse.success("Danh sách mã giảm giá", codes)
        );
    }

    /**
     * GET PROMO CODE BY ID (ADMIN ONLY)
     */
    @GetMapping("/admin/promo-codes/{promoCodeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> getPromoCodeById(
            @PathVariable Long promoCodeId) {

        PromoCodeDTO code = promoCodeService.getPromoCodeById(promoCodeId);
        
        return ResponseEntity.ok(
            ApiResponse.success("Chi tiết mã giảm giá", code)
        );
    }

    // ========================================
    // USER/CUSTOMER ENDPOINTS
    // ========================================

    /**
     * GET ACTIVE PROMO CODES (PUBLIC)
     */
    @GetMapping("/promo-codes/active")
    public ResponseEntity<ApiResponse<List<PromoCodeDTO>>> getActivePromoCodes() {

        List<PromoCodeDTO> codes = promoCodeService.getActivePromoCodes();
        
        return ResponseEntity.ok(
            ApiResponse.success("Danh sách mã giảm giá hiện hoạt", codes)
        );
    }

    /**
     * VALIDATE PROMO CODE (USER)
     */
    @PostMapping("/promo-codes/validate")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeValidateResponse>> validatePromoCode(
            @RequestBody PromoCodeValidateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String code = request != null ? request.getCode() : null;
        if (code == null || code.trim().isEmpty()) {
            throw new BadRequestException("Mã giảm giá không hợp lệ!");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseGet(() -> userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found")));

        java.math.BigDecimal orderAmount = request.getOrderAmount() != null
                ? request.getOrderAmount()
                : java.math.BigDecimal.ZERO;

        PromoCode promoCode = promoCodeService.validatePromoCode(
            code.trim(),
            orderAmount,
            user.getUserId()
        );

        PromoCodeValidateResponse response = new PromoCodeValidateResponse();
        response.setCode(promoCode.getCode());
        response.setDiscountType(promoCode.getDiscountType().name());
        response.setDiscountAmount(
            promoCodeService.calculateDiscountAmount(promoCode, orderAmount)
        );
        response.setFreeShipping(
            PromoCode.DiscountType.FREE_SHIPPING.equals(promoCode.getDiscountType())
        );

        return ResponseEntity.ok(ApiResponse.success("Mã giảm giá hợp lệ", response));
    }
}
