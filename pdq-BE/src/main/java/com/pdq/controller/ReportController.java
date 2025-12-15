package com.pdq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdq.dto.common.ApiResponse;
import com.pdq.dto.report.ReportResponse;
import com.pdq.service.ReportService;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ReportResponse>> getRevenueReport(
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {

        System.out.println(">>> Revenue report API called | dateFrom=" + dateFrom + ", dateTo=" + dateTo);
        ReportResponse report = reportService.getRevenueReport(dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.success("Revenue report generated successfully", report));
    }
}
