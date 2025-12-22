package com.pdq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdq.entity.PromoCode;
import java.util.Optional;
import java.util.List;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    
    Optional<PromoCode> findByCode(String code);
    
    List<PromoCode> findByActiveTrue();
    
    List<PromoCode> findByActiveFalse();
}
