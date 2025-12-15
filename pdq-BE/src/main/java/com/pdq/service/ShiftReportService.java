package com.pdq.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.report.ShiftEarningsResponse;
import com.pdq.dto.report.ShiftProductSale;
import com.pdq.dto.report.ShiftReport;
import com.pdq.entity.Order;
import com.pdq.entity.OrderItem;
import com.pdq.entity.OrderStatus;
import com.pdq.entity.PaymentMethod;
import com.pdq.entity.PaymentStatus;
import com.pdq.exception.BadRequestException;
import com.pdq.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class ShiftReportService {

    private final OrderRepository orderRepository;

    public ShiftReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ShiftEarningsResponse generateShiftReport(String dateStr) {
        LocalDate date = parseDate(dateStr);

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📊 GENERATING SHIFT EARNINGS REPORT");
        System.out.println("   Date: " + date);
        System.out.println("═══════════════════════════════════════════");

        ShiftReport morningShift = processShift(
                date, 9, 14, "MORNING", "Ca Sáng", "09:00 - 14:00");

        ShiftReport eveningShift = processShift(
                date, 16, 22, "EVENING", "Ca Tối", "16:00 - 22:00");

        BigDecimal totalDailyRevenue = morningShift.getRevenue().add(eveningShift.getRevenue());
        Long totalDailyOrders = morningShift.getTotalOrders() + eveningShift.getTotalOrders();

        System.out.println("\n💰 DAILY SUMMARY:");
        System.out.println("   Total Revenue: " + totalDailyRevenue);
        System.out.println("   Total Orders: " + totalDailyOrders);
        System.out.println("═══════════════════════════════════════════\n");

        ShiftEarningsResponse response = new ShiftEarningsResponse();
        response.setDate(date);
        response.setTotalDailyRevenue(totalDailyRevenue);
        response.setTotalDailyOrders(totalDailyOrders);
        List<ShiftReport> shifts = new ArrayList<>();
        shifts.add(morningShift);
        shifts.add(eveningShift);
        response.setShifts(shifts);

        return response;
    }

    public byte[] generateShiftReportExcel(String dateStr) throws IOException {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("📥 GENERATING EXCEL EXPORT");
        System.out.println("   Date: " + dateStr);
        System.out.println("═══════════════════════════════════════════");

        ShiftEarningsResponse report = generateShiftReport(dateStr);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ShiftReport morning = findShift(report, "MORNING");
            ShiftReport evening = findShift(report, "EVENING");

            Sheet summarySheet = workbook.createSheet("Tổng Quan");
            createSummarySheet(summarySheet, report, morning, evening);

            Sheet morningSheet = workbook.createSheet("Ca Sáng");
            createShiftSheet(morningSheet, morning);

            Sheet eveningSheet = workbook.createSheet("Ca Tối");
            createShiftSheet(eveningSheet, evening);

            workbook.write(out);
            System.out.println("✅ Excel file generated successfully");
            return out.toByteArray();
        }
    }

    private ShiftReport processShift(LocalDate date,
            int startHour,
            int endHour,
            String shiftType,
            String shiftName,
            String timeRange) {

        LocalDateTime startTime = date.atTime(startHour, 0, 0);
        LocalDateTime endTime = date.atTime(endHour, 0, 0);

        System.out.println("\n⏰ Processing " + shiftName + " (" + timeRange + ")");

        List<Order> orders = fetchPaidOrders(startTime, endTime);
        System.out.println("   Found " + orders.size() + " paid orders");

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal cashRevenue = BigDecimal.ZERO;
        BigDecimal vnpayRevenue = BigDecimal.ZERO;
        long cashOrders = 0;
        long vnpayOrders = 0;

        Map<Long, ShiftProductSale> productSalesMap = new HashMap<>();

        for (Order order : orders) {
            BigDecimal orderRevenue = order.getFinalAmount() != null
                    ? order.getFinalAmount()
                    : order.getTotalAmount();
            if (orderRevenue == null) {
                orderRevenue = BigDecimal.ZERO;
            }

            totalRevenue = totalRevenue.add(orderRevenue);

            if (order.getPaymentMethod() == PaymentMethod.cash) {
                cashRevenue = cashRevenue.add(orderRevenue);
                cashOrders++;
            } else if (order.getPaymentMethod() == PaymentMethod.vnpay) {
                vnpayRevenue = vnpayRevenue.add(orderRevenue);
                vnpayOrders++;
            }

            if (order.getItems() == null) {
                continue;
            }

            for (OrderItem item : order.getItems()) {
                if (item == null || item.getProduct() == null) {
                    continue;
                }

                Long productId = item.getProduct().getProductId();
                ShiftProductSale sale = productSalesMap.get(productId);
                if (sale == null) {
                    sale = new ShiftProductSale();
                    sale.setProductId(productId);
                    sale.setProductName(item.getProductName());
                    sale.setCategoryName(item.getProduct().getCategory() != null
                            ? item.getProduct().getCategory().getCategoryName()
                            : "N/A");
                    sale.setQuantitySold(0);
                    sale.setUnitPrice(item.getPrice());
                    sale.setTotalRevenue(BigDecimal.ZERO);
                    sale.setPercentage(BigDecimal.ZERO);
                    productSalesMap.put(productId, sale);
                }

                int currentQty = sale.getQuantitySold() != null ? sale.getQuantitySold().intValue() : 0;
                int itemQty = item.getQuantity() != null ? item.getQuantity().intValue() : 0;
                sale.setQuantitySold(currentQty + itemQty);

                BigDecimal itemSubtotal = item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO;
                sale.setTotalRevenue(sale.getTotalRevenue().add(itemSubtotal));
            }
        }

        List<ShiftProductSale> productSales = new ArrayList<>(productSalesMap.values());
        for (ShiftProductSale sale : productSales) {
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = sale.getTotalRevenue()
                        .divide(totalRevenue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
                sale.setPercentage(percentage);
            } else {
                sale.setPercentage(BigDecimal.ZERO);
            }
        }

        productSales.sort((a, b) -> b.getTotalRevenue().compareTo(a.getTotalRevenue()));

        BigDecimal averageOrderValue = orders.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);

        System.out.println("   💰 Total Revenue: " + totalRevenue);
        System.out.println("   💵 Cash: " + cashRevenue + " (" + cashOrders + " orders)");
        System.out.println("   💳 VNPay: " + vnpayRevenue + " (" + vnpayOrders + " orders)");
        System.out.println("   📊 Avg Order Value: " + averageOrderValue);
        System.out.println("   🥇 Products: " + productSales.size());

        ShiftReport report = new ShiftReport();
        report.setShiftType(shiftType);
        report.setShiftName(shiftName);
        report.setTimeRange(timeRange);
        report.setRevenue(totalRevenue);
        report.setCashRevenue(cashRevenue);
        report.setVnpayRevenue(vnpayRevenue);
        report.setTotalOrders((long) orders.size());
        report.setCashOrders(cashOrders);
        report.setVnpayOrders(vnpayOrders);
        report.setAverageOrderValue(averageOrderValue);
        report.setProductSales(productSales);

        return report;
    }

    private void createSummarySheet(Sheet sheet, ShiftEarningsResponse report, ShiftReport morning, ShiftReport evening) {
        Workbook wb = sheet.getWorkbook();

        CellStyle titleStyle = createTitleStyle(wb, IndexedColors.DARK_BLUE, (short) 16);
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        setAllBorders(headerStyle);

        CellStyle labelStyle = wb.createCellStyle();
        Font labelFont = wb.createFont();
        labelFont.setBold(true);
        labelStyle.setFont(labelFont);

        CellStyle moneyStyle = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        moneyStyle.setDataFormat(df.getFormat("#,##0\"₫\""));

        // Title row
        Row r0 = sheet.createRow(0);
        r0.setHeightInPoints(30);
        createCellWithStyle(r0, 0, "💰 BÁO CÁO THU NHẬP THEO CA", titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        // Date
        Row r1 = sheet.createRow(1);
        createCellWithStyle(r1, 0, "Ngày:", labelStyle);
        createCell(r1, 1, report.getDate() != null ? report.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");

        // Totals
        Row r3 = sheet.createRow(3);
        createCellWithStyle(r3, 0, "💰 TỔNG DOANH THU NGÀY", labelStyle);
        Cell totalRevenueCell = createCell(r3, 1, report.getTotalDailyRevenue());
        totalRevenueCell.setCellStyle(moneyStyle);

        Row r4 = sheet.createRow(4);
        createCellWithStyle(r4, 0, "📦 TỔNG ĐƠN HÀNG", labelStyle);
        createCell(r4, 1, report.getTotalDailyOrders());

        // Comparison header
        Row r6 = sheet.createRow(6);
        createCell(r6, 0, "");
        createCellWithStyle(r6, 1, "🌅 CA SÁNG", headerStyle);
        createCellWithStyle(r6, 2, "🌙 CA TỐI", headerStyle);

        // Time range
        Row r7 = sheet.createRow(7);
        createCellWithStyle(r7, 0, "Khung giờ", labelStyle);
        createCell(r7, 1, morning != null ? morning.getTimeRange() : "");
        createCell(r7, 2, evening != null ? evening.getTimeRange() : "");

        // Revenue
        Row r8 = sheet.createRow(8);
        createCellWithStyle(r8, 0, "Doanh thu", labelStyle);
        Cell c8m = createCell(r8, 1, morning != null ? morning.getRevenue() : BigDecimal.ZERO);
        c8m.setCellStyle(moneyStyle);
        Cell c8e = createCell(r8, 2, evening != null ? evening.getRevenue() : BigDecimal.ZERO);
        c8e.setCellStyle(moneyStyle);

        // Orders
        Row r9 = sheet.createRow(9);
        createCellWithStyle(r9, 0, "Số đơn", labelStyle);
        createCell(r9, 1, morning != null ? morning.getTotalOrders() : 0);
        createCell(r9, 2, evening != null ? evening.getTotalOrders() : 0);

        // Cash revenue
        Row r10 = sheet.createRow(10);
        createCellWithStyle(r10, 0, "Tiền mặt", labelStyle);
        Cell c10m = createCell(r10, 1, morning != null ? morning.getCashRevenue() : BigDecimal.ZERO);
        c10m.setCellStyle(moneyStyle);
        Cell c10e = createCell(r10, 2, evening != null ? evening.getCashRevenue() : BigDecimal.ZERO);
        c10e.setCellStyle(moneyStyle);

        // VNPay revenue
        Row r11 = sheet.createRow(11);
        createCellWithStyle(r11, 0, "VNPay", labelStyle);
        Cell c11m = createCell(r11, 1, morning != null ? morning.getVnpayRevenue() : BigDecimal.ZERO);
        c11m.setCellStyle(moneyStyle);
        Cell c11e = createCell(r11, 2, evening != null ? evening.getVnpayRevenue() : BigDecimal.ZERO);
        c11e.setCellStyle(moneyStyle);

        // AOV
        Row r12 = sheet.createRow(12);
        createCellWithStyle(r12, 0, "Giá trị đơn TB", labelStyle);
        Cell c12m = createCell(r12, 1, morning != null ? morning.getAverageOrderValue() : BigDecimal.ZERO);
        c12m.setCellStyle(moneyStyle);
        Cell c12e = createCell(r12, 2, evening != null ? evening.getAverageOrderValue() : BigDecimal.ZERO);
        c12e.setCellStyle(moneyStyle);

        for (int i = 0; i <= 2; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
    }

    private void createShiftSheet(Sheet sheet, ShiftReport shift) {
        Workbook wb = sheet.getWorkbook();

        IndexedColors titleColor = "MORNING".equalsIgnoreCase(shift.getShiftType())
                ? IndexedColors.GOLD
                : IndexedColors.BLUE;

        CellStyle titleStyle = createTitleStyle(wb, titleColor, (short) 14);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        Row r0 = sheet.createRow(0);
        r0.setHeightInPoints(25);
        createCellWithStyle(r0, 0, shift.getShiftName() + " - " + shift.getTimeRange(), titleStyle);

        // Label styles
        Font bold = wb.createFont();
        bold.setBold(true);
        CellStyle boldStyle = wb.createCellStyle();
        boldStyle.setFont(bold);

        CellStyle moneyStyle = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        moneyStyle.setDataFormat(df.getFormat("#,##0\"₫\""));

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        setAllBorders(headerStyle);

        CellStyle centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row r2 = sheet.createRow(2);
        createCellWithStyle(r2, 0, "💰 Tổng doanh thu:", boldStyle);
        Cell c2 = createCell(r2, 1, shift.getRevenue());
        c2.setCellStyle(moneyStyle);

        Row r3 = sheet.createRow(3);
        createCellWithStyle(r3, 0, "📦 Tổng đơn hàng:", boldStyle);
        createCell(r3, 1, shift.getTotalOrders());

        Row r4 = sheet.createRow(4);
        createCellWithStyle(r4, 0, "💵 Tiền mặt:", boldStyle);
        Cell c4 = createCell(r4, 1, shift.getCashRevenue());
        c4.setCellStyle(moneyStyle);
        createCellWithStyle(r4, 2, "(" + shift.getCashOrders() + " đơn)", centerStyle);

        Row r5 = sheet.createRow(5);
        createCellWithStyle(r5, 0, "💳 VNPay:", boldStyle);
        Cell c5 = createCell(r5, 1, shift.getVnpayRevenue());
        c5.setCellStyle(moneyStyle);
        createCellWithStyle(r5, 2, "(" + shift.getVnpayOrders() + " đơn)", centerStyle);

        Row r7 = sheet.createRow(7);
        createCellWithStyle(r7, 0, "🥇 SẢN PHẨM BÁN CHẠY", boldStyle);

        Row header = sheet.createRow(8);
        String[] headers = {"STT", "Sản phẩm", "Danh mục", "Số lượng", "Đơn giá", "Doanh thu", "% Doanh thu"};
        for (int i = 0; i < headers.length; i++) {
            createCellWithStyle(header, i, headers[i], headerStyle);
        }

        List<ShiftProductSale> sales = shift.getProductSales() != null ? shift.getProductSales() : List.of();
        int rowIdx = 9;
        int index = 1;
        for (ShiftProductSale sale : sales) {
            Row row = sheet.createRow(rowIdx++);
            createCellWithStyle(row, 0, index++, centerStyle);
            createCell(row, 1, sale.getProductName());
            createCell(row, 2, sale.getCategoryName());

            Cell qtyCell = createCell(row, 3, sale.getQuantitySold() != null ? sale.getQuantitySold().intValue() : 0);
            qtyCell.setCellStyle(centerStyle);

            Cell unitCell = createCell(row, 4, sale.getUnitPrice() != null ? sale.getUnitPrice() : BigDecimal.ZERO);
            unitCell.setCellStyle(moneyStyle);

            Cell revCell = createCell(row, 5, sale.getTotalRevenue() != null ? sale.getTotalRevenue() : BigDecimal.ZERO);
            revCell.setCellStyle(moneyStyle);

            String percentStr = sale.getPercentage() != null ? sale.getPercentage().toString() + "%" : "0%";
            createCellWithStyle(row, 6, percentStr, centerStyle);
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
    }

    private ShiftReport findShift(ShiftEarningsResponse report, String shiftType) {
        if (report.getShifts() == null) {
            return new ShiftReport();
        }
        return report.getShifts().stream()
                .filter(s -> shiftType.equalsIgnoreCase(s.getShiftType()))
                .findFirst()
                .orElse(new ShiftReport());
    }

    private CellStyle createTitleStyle(Workbook wb, IndexedColors bg, short fontSize) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontSize);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(bg.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setAllBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private Cell createCell(Row row, int col, Object value) {
        Cell cell = row.createCell(col);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        return cell;
    }

    private Cell createCellWithStyle(Row row, int col, Object value, CellStyle style) {
        Cell cell = createCell(row, col, value);
        cell.setCellStyle(style);
        return cell;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new BadRequestException("Date is required in format yyyy-MM-dd");
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format. Use yyyy-MM-dd");
        }
    }

    private List<Order> fetchPaidOrders(LocalDateTime startTime, LocalDateTime endTime) {
        List<Order> orders = orderRepository.findByPaymentStatusAndCreatedAtBetween(
                PaymentStatus.paid, startTime, endTime);
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderStatus() != OrderStatus.cancelled) {
                filtered.add(order);
            }
        }
        return filtered;
    }
}
