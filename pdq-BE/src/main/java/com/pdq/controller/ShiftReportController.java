package com.pdq.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdq.dto.common.ApiResponse;
import com.pdq.dto.report.ShiftEarningsResponse;
import com.pdq.service.ShiftReportService;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    public ShiftReportController(ShiftReportService shiftReportService) {
        this.shiftReportService = shiftReportService;
    }

    @GetMapping("/shift-earnings")
    public ResponseEntity<ApiResponse<ShiftEarningsResponse>> getShiftEarnings(
            @RequestParam String date) {

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📊 SHIFT EARNINGS API REQUEST");
        System.out.println("   Date: " + date);
        System.out.println("   Endpoint: GET /api/admin/reports/shift-earnings");
        System.out.println("═══════════════════════════════════════════");

        ShiftEarningsResponse report = shiftReportService.generateShiftReport(date);

        System.out.println("✅ Report generated successfully");
        System.out.println("═══════════════════════════════════════════\n");

        return ResponseEntity.ok(
                ApiResponse.success("Shift earnings report generated successfully", report)
        );
    }

    @GetMapping("/shift-earnings/export")
    public ResponseEntity<ByteArrayResource> exportShiftEarningsExcel(
            @RequestParam String date) throws IOException {

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📥 EXCEL EXPORT REQUEST");
        System.out.println("   Date: " + date);
        System.out.println("═══════════════════════════════════════════");

        byte[] excelData = shiftReportService.generateShiftReportExcel(date);
        ByteArrayResource resource = new ByteArrayResource(excelData);

        String filename = "BaoCaoThuNhap_" +
                LocalDate.parse(date).format(DateTimeFormatter.ofPattern("ddMMyyyy")) +
                ".xlsx";

        System.out.println("✅ Excel file ready");
        System.out.println("   Filename: " + filename);
        System.out.println("   Size: " + excelData.length + " bytes");
        System.out.println("═══════════════════════════════════════════\n");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelData.length)
                .body(resource);
    }
}
