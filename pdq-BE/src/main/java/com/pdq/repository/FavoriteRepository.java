package com.pdq.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pdq.entity.Favorite;
import com.pdq.entity.Product;
import com.pdq.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Optional<Favorite> findByUserAndProduct(User user, Product product);
    
    boolean existsByUserAndProduct(User user, Product product);
    
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.user = :user")
    long countByUser(@Param("user") User user);
    
    void deleteByUserAndProduct(User user, Product product);
}