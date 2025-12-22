package com.pdq.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.promo.PromoCodeDTO;
import com.pdq.entity.PromoCode;
import com.pdq.entity.User;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.PromoCodeRepository;
import com.pdq.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;
    private final UserRepository userRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository,
                          UserRepository userRepository) {
        this.promoCodeRepository = promoCodeRepository;
        this.userRepository = userRepository;
    }

    // ========== ADMIN METHODS ==========

    @Transactional
    public PromoCodeDTO createPromoCode(PromoCodeDTO dto, String adminEmail) {
        System.out.println("📝 Creating promo code: " + dto.getCode());

        // Validate code không trùng
        if (promoCodeRepository.findByCode(dto.getCode()).isPresent()) {
            throw new BadRequestException("Mã giảm giá này đã tồn tại!");
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(dto.getCode().toUpperCase());
        promoCode.setDescription(dto.getDescription());
        promoCode.setDiscountType(PromoCode.DiscountType.valueOf(dto.getDiscountType()));
        promoCode.setDiscountValue(dto.getDiscountValue());
        promoCode.setMinimumOrderAmount(dto.getMinimumOrderAmount() != null ? 
            dto.getMinimumOrderAmount() : BigDecimal.ZERO);
        promoCode.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        promoCode.setActive(dto.getActive() != null ? dto.getActive() : true);
        promoCode.setUsageLimit(dto.getUsageLimit());
        promoCode.setUsageCount(0);
        promoCode.setStartDate(dto.getStartDate());
        promoCode.setEndDate(dto.getEndDate());
        promoCode.setCreatedBy(adminEmail);

        promoCode = promoCodeRepository.save(promoCode);
        System.out.println("✅ Promo code created: " + promoCode.getCode());

        return mapToDTO(promoCode);
    }

    @Transactional
    public PromoCodeDTO updatePromoCode(Long promoCodeId, PromoCodeDTO dto, String adminEmail) {
        System.out.println("✏️ Updating promo code: " + promoCodeId);

        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
            .orElseThrow(() -> new ResourceNotFoundException("Mã giảm giá không tìm thấy!"));

        if (dto.getCode() != null && !dto.getCode().equals(promoCode.getCode())) {
            if (promoCodeRepository.findByCode(dto.getCode()).isPresent()) {
                throw new BadRequestException("Mã giảm giá này đã tồn tại!");
            }
            promoCode.setCode(dto.getCode().toUpperCase());
        }

        if (dto.getDescription() != null) {
            promoCode.setDescription(dto.getDescription());
        }
        if (dto.getDiscountType() != null) {
            promoCode.setDiscountType(PromoCode.DiscountType.valueOf(dto.getDiscountType()));
        }
        if (dto.getDiscountValue() != null) {
            promoCode.setDiscountValue(dto.getDiscountValue());
        }
        if (dto.getMinimumOrderAmount() != null) {
            promoCode.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        }
        if (dto.getMaxDiscountAmount() != null) {
            promoCode.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        }
        if (dto.getActive() != null) {
            promoCode.setActive(dto.getActive());
        }
        if (dto.getUsageLimit() != null) {
            promoCode.setUsageLimit(dto.getUsageLimit());
        }
        if (dto.getStartDate() != null) {
            promoCode.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            promoCode.setEndDate(dto.getEndDate());
        }

        promoCode = promoCodeRepository.save(promoCode);
        System.out.println("✅ Promo code updated: " + promoCode.getCode());

        return mapToDTO(promoCode);
    }

    @Transactional
    public void deletePromoCode(Long promoCodeId) {
        System.out.println("🗑️ Deleting promo code: " + promoCodeId);

        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
            .orElseThrow(() -> new ResourceNotFoundException("Mã giảm giá không tìm thấy!"));

        promoCodeRepository.delete(promoCode);
        System.out.println("✅ Promo code deleted: " + promoCode.getCode());
    }

    @Transactional
    public PromoCodeDTO togglePromoCode(Long promoCodeId) {
        System.out.println("🔄 Toggling promo code: " + promoCodeId);

        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
            .orElseThrow(() -> new ResourceNotFoundException("Mã giảm giá không tìm thấy!"));

        promoCode.setActive(!promoCode.getActive());
        promoCode = promoCodeRepository.save(promoCode);

        System.out.println("✅ Promo code toggled: " + promoCode.getCode() + 
                         " -> " + (promoCode.getActive() ? "ACTIVE" : "INACTIVE"));

        return mapToDTO(promoCode);
    }

    public PromoCodeDTO getPromoCodeById(Long promoCodeId) {
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
            .orElseThrow(() -> new ResourceNotFoundException("Mã giảm giá không tìm thấy!"));
        return mapToDTO(promoCode);
    }

    public List<PromoCodeDTO> getAllPromoCodes() {
        return promoCodeRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<PromoCodeDTO> getActivePromoCodes() {
        return promoCodeRepository.findByActiveTrue().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    // ========== USER METHODS ==========

    @Transactional
    public PromoCode validateAndApplyPromoCode(String code, BigDecimal orderAmount, Long userId) {
        PromoCode promoCode = validatePromoCodeInternal(code, orderAmount, userId, true);
        System.out.println("✅ Promo code applied: " + code);
        return promoCode;
    }

    public PromoCode validatePromoCode(String code, BigDecimal orderAmount, Long userId) {
        return validatePromoCodeInternal(code, orderAmount, userId, false);
    }

    public BigDecimal calculateDiscountAmount(PromoCode promoCode, BigDecimal orderAmount) {
        if (promoCode == null) return BigDecimal.ZERO;

        BigDecimal discount = BigDecimal.ZERO;

        switch (promoCode.getDiscountType()) {
            case PERCENTAGE:
                // Giảm theo %
                discount = orderAmount.multiply(promoCode.getDiscountValue())
                    .divide(new BigDecimal("100"));
                
                // Giới hạn giảm tối đa
                if (promoCode.getMaxDiscountAmount() != null &&
                    discount.compareTo(promoCode.getMaxDiscountAmount()) > 0) {
                    discount = promoCode.getMaxDiscountAmount();
                }
                break;

            case FIXED_AMOUNT:
                // Giảm số tiền cố định
                discount = promoCode.getDiscountValue();
                break;

            case FREE_SHIPPING:
                // Miễn phí vận chuyển (tính sau, xử lý ở checkout)
                discount = BigDecimal.ZERO;
                break;

            default:
                discount = BigDecimal.ZERO;
        }

        // Không giảm quá số tiền đơn hàng
        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount;
    }

    private PromoCode validatePromoCodeInternal(
            String code,
            BigDecimal orderAmount,
            Long userId,
            boolean applyUsage) {
        System.out.println("✔️ Validating promo code: " + code + " for user: " + userId);

        if (code == null || code.trim().isEmpty()) {
            throw new BadRequestException("Mã giảm giá không hợp lệ!");
        }

        PromoCode promoCode = promoCodeRepository.findByCode(code.toUpperCase())
            .orElseThrow(() -> new BadRequestException("Mã giảm giá không hợp lệ!"));

        if (!promoCode.isValid()) {
            throw new BadRequestException("Mã giảm giá không còn hoạt động!");
        }

        BigDecimal amount = orderAmount != null ? orderAmount : BigDecimal.ZERO;
        BigDecimal minimum = promoCode.getMinimumOrderAmount() != null
                ? promoCode.getMinimumOrderAmount()
                : BigDecimal.ZERO;
        if (amount.compareTo(minimum) < 0) {
            throw new BadRequestException("Đơn hàng phải từ " + minimum + " VND!");
        }

        if (promoCode.getUsedByUsers() != null && userId != null) {
            String[] usedByList = promoCode.getUsedByUsers().split(",");
            for (String usedById : usedByList) {
                if (usedById.trim().equals(userId.toString())) {
                    throw new BadRequestException("Bạn đã sử dụng mã này rồi!");
                }
            }
        }

        if (applyUsage) {
            promoCode.setUsageCount((promoCode.getUsageCount() != null
                ? promoCode.getUsageCount() : 0) + 1);

            if (userId != null) {
                String usedByUsers = promoCode.getUsedByUsers() != null
                    ? promoCode.getUsedByUsers() : "";
                if (!usedByUsers.isEmpty()) {
                    usedByUsers += "," + userId;
                } else {
                    usedByUsers = userId.toString();
                }
                promoCode.setUsedByUsers(usedByUsers);
            }

            promoCodeRepository.save(promoCode);
        }

        return promoCode;
    }

    // ========== AUTO-GENERATE ON SIGNUP ==========

    @Transactional
    public PromoCode generateSignupPromoCode(User user) {
        System.out.println("🎁 Generating signup promo code for user: " + user.getEmail());

        // Kiểm tra xem user đã có promo code chưa
        String signupCodeKey = "SIGNUP_" + user.getUserId();
        if (promoCodeRepository.findByCode(signupCodeKey).isPresent()) {
            System.out.println("⚠️ User already has signup code!");
            return null;
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(signupCodeKey); // SIGNUP_123
        promoCode.setDescription("Mã giảm 50% phí ship cho thành viên mới - " + user.getEmail());
        promoCode.setDiscountType(PromoCode.DiscountType.FREE_SHIPPING);
        promoCode.setDiscountValue(new BigDecimal("50")); // 50% FREE shipping
        promoCode.setMinimumOrderAmount(BigDecimal.ZERO);
        promoCode.setActive(true);
        promoCode.setUsageLimit(1); // Chỉ dùng 1 lần
        promoCode.setUsageCount(0);
        promoCode.setStartDate(LocalDateTime.now());
        promoCode.setEndDate(LocalDateTime.now().plusDays(30)); // Hợp lệ 30 ngày
        promoCode.setCreatedBy("SYSTEM_AUTO_SIGNUP");

        promoCode = promoCodeRepository.save(promoCode);
        System.out.println("✅ Signup promo code created: " + promoCode.getCode());

        return promoCode;
    }

    // ========== HELPER METHODS ==========

    private PromoCodeDTO mapToDTO(PromoCode promoCode) {
        PromoCodeDTO dto = new PromoCodeDTO();
        dto.setPromoCodeId(promoCode.getPromoCodeId());
        dto.setCode(promoCode.getCode());
        dto.setDescription(promoCode.getDescription());
        dto.setDiscountType(promoCode.getDiscountType().name());
        dto.setDiscountValue(promoCode.getDiscountValue());
        dto.setMinimumOrderAmount(promoCode.getMinimumOrderAmount());
        dto.setMaxDiscountAmount(promoCode.getMaxDiscountAmount());
        dto.setActive(promoCode.getActive());
        dto.setUsageLimit(promoCode.getUsageLimit());
        dto.setUsageCount(promoCode.getUsageCount());
        dto.setStartDate(promoCode.getStartDate());
        dto.setEndDate(promoCode.getEndDate());
        dto.setCreatedAt(promoCode.getCreatedAt());
        dto.setUpdatedAt(promoCode.getUpdatedAt());
        dto.setCreatedBy(promoCode.getCreatedBy());
        return dto;
    }
}
