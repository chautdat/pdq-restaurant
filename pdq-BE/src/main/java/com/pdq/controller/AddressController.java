package com.pdq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

import com.pdq.dto.address.AddressRequest;
import com.pdq.dto.address.AddressResponse;
import com.pdq.dto.common.ApiResponse;
import com.pdq.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<AddressResponse> addresses = addressService.getMyAddresses(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Addresses retrieved successfully", addresses));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<AddressResponse>> getDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails) {
        AddressResponse address = addressService.getDefaultAddress(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Default address retrieved successfully", address));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse address = addressService.createAddress(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Address created successfully", address));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        AddressResponse address = addressService.updateAddress(
                userDetails.getUsername(), addressId, request);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", address));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        addressService.deleteAddress(userDetails.getUsername(), addressId);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

    @PutMapping("/{addressId}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        AddressResponse address = addressService.setDefaultAddress(
                userDetails.getUsername(), addressId);
        return ResponseEntity.ok(ApiResponse.success("Default address updated successfully", address));
    }
}