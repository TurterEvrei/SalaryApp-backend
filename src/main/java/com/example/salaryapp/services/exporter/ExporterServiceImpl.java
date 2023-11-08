package com.example.salaryapp.services.exporter;

import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.Payment;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExporterServiceImpl implements ExporterService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Override
    public void exportDailyReports(HttpServletResponse response, List<DailyReport> reports) {

        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd-MM-yyyy")
                .toFormatter();

        try (ServletOutputStream outputStream = response.getOutputStream();) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Reports");

            Map<Long, Row> employeesRowMap = new HashMap<>();
            Map<Integer, Integer> employeesRowResultPaymentMap = new HashMap<>();

            Row datesRow = sheet.createRow(0);
            Row slicesPaymentRow = sheet.createRow(1);
            int dateColumnIndex = 1;
            CellStyle headerStyle = getHeaderStyle();
            CellStyle dataCellStyle = getDataCellStyle();

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            int countRowIndex = 2;
            for (DailyReport report : reports) {
                for (Payment payment : report.getPayments()) {
                    Employee employee = payment.getEmployee();
                    if (!employeesRowMap.containsKey(employee.getId())) {
                        Row row = sheet.createRow(countRowIndex++);
                        createCell(row, 0, employee.getName(), dataCellStyle);
                        employeesRowMap.put(employee.getId(), row);
                    }
                }
            }
            Row totalPaymentForDayRow = sheet.createRow(countRowIndex++);
            createCell(totalPaymentForDayRow, 0, "За день", headerStyle);

            reports.sort(Comparator.comparing(DailyReport::getDate));

            for (DailyReport report : reports) {
                createCell(datesRow, dateColumnIndex, report.getDate().format(dateFormatter), headerStyle);

                sheet.addMergedRegion(new CellRangeAddress(0, 0, dateColumnIndex, dateColumnIndex + 2));
                createCell(slicesPaymentRow, dateColumnIndex, "%", headerStyle);
                createCell(slicesPaymentRow, dateColumnIndex + 1, "Чай", headerStyle);
                createCell(slicesPaymentRow, dateColumnIndex + 2, "Общ.", headerStyle);

                int totalPaymentForDay = 0;
                for (Payment payment : report.getPayments()) {
                    Row row = employeesRowMap.get(payment.getEmployee().getId());
                    createCell(row, dateColumnIndex, payment.getProcentFromSales(), dataCellStyle);
                    createCell(row, dateColumnIndex + 1, payment.getTips(), dataCellStyle);
                    createCell(row, dateColumnIndex + 2, payment.getTotalPayment(), dataCellStyle);
                    totalPaymentForDay += payment.getTotalPayment();

                    employeesRowResultPaymentMap.merge(row.getRowNum(), payment.getTotalPayment(), Integer::sum);
                }

                createCell(totalPaymentForDayRow, dateColumnIndex, "-", headerStyle);
                createCell(totalPaymentForDayRow, dateColumnIndex + 1, "-", headerStyle);
                createCell(totalPaymentForDayRow, dateColumnIndex + 2, totalPaymentForDay, headerStyle);

                dateColumnIndex += 3;
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 1, dateColumnIndex, dateColumnIndex));
            createCell(datesRow, dateColumnIndex, "Итого:", headerStyle);

            for (int i = 2; i < countRowIndex; i++) {
                createCell(sheet.getRow(i), dateColumnIndex, employeesRowResultPaymentMap.get(i), dataCellStyle);
            }

            createCell(totalPaymentForDayRow, dateColumnIndex, employeesRowResultPaymentMap.values().stream().reduce(0, Integer::sum), headerStyle);

            workbook.write(outputStream);
            workbook.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private CellStyle getHeaderStyle() {
        CellStyle headerStyle = getGeneralStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        font.setFamily(FontFamily.ROMAN);
        headerStyle.setFont(font);
        return headerStyle;
    }

    private CellStyle getDataCellStyle() {
        CellStyle style = getGeneralStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setFamily(FontFamily.ROMAN);
        style.setFont(font);
        return style;
    }

    private CellStyle getGeneralStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnIndex);
        Cell cell = row.createCell(columnIndex);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        }
        cell.setCellStyle(style);
    }
}
