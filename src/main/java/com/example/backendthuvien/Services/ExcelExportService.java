package com.example.backendthuvien.Services;
import com.example.backendthuvien.Repositories.RoleRepo;
import com.example.backendthuvien.Repositories.UserRepository;
import com.example.backendthuvien.entity.Role;
import com.example.backendthuvien.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {
@Autowired
private RoleRepo roleRepo;
    private static final String[] HEADERS = {"STT", "Hành động", "Chi tiết sản phẩm", "Thời gian", "Ghi chú"};
    private static final String FILE_NAME = "product_log.xlsx";
@Autowired
private UserRepository userRepository;
    private List<Object[]> logData = new ArrayList<>();

    // Ghi log chi tiết
    public void logAction(String action, String detail, String note) {
        logData.add(new Object[]{
                logData.size() + 1,  // STT
                action,
                detail,              // Chi tiết sản phẩm (ví dụ: "Xóa sản phẩm: Sách Toán")
                LocalDateTime.now(), // Thời gian
                note
        });
    }

    // Xuất file Excel
    public void exportToExcel(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Logs");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

        // Ghi dữ liệu
        int rowIndex = 1;
        for (Object[] log : logData) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < log.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(log[i] != null ? log[i].toString() : "");
            }
        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file ra đĩa
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        logData.clear(); // Xóa dữ liệu log sau khi xuất
    }


    // Tạo style cho header
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    public void exportUsersToExcel(String filePath) throws IOException {
        // Lấy danh sách user từ database
        List<User> users = userRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Tạo header
        String[] headers = {"ID", "Tên", "Địa chỉ", "Số điện thoại", "Vai trò","Ngày sinh","Mật khẩu"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

        // Ghi dữ liệu
        int rowIndex = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (User user : users) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFullName());
            row.createCell(2).setCellValue(user.getAddress());
            row.createCell(3).setCellValue(user.getPhoneNumber());
            row.createCell(4).setCellValue(user.getRole().getName());
            row.createCell(5).setCellValue(
                    user.getDateOfBirth() != null
                            ? dateFormat.format(user.getDateOfBirth())
                            : ""
            );
            row.createCell(6).setCellValue(user.getPassword());
        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file ra đĩa
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        System.out.println("File đã được lưu tại: " + filePath);
    }

    public void importUsersFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // Đọc từng dòng từ file Excel
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua dòng đầu tiên (header)
                Row row = sheet.getRow(i);
                if (row != null) {
                    User user = new User();

                    Cell idCell = row.getCell(0);
                    if (idCell != null) {
                        switch (idCell.getCellType()) {
                            case STRING:
                                // Nếu ID ở dạng chuỗi
                                try {
                                    Long id = Long.parseLong(idCell.getStringCellValue());
                                    user.setId(id);
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("ID không hợp lệ tại dòng: " + (i + 1), e);
                                }
                                break;
                            case NUMERIC:
                                // Nếu ID ở dạng số
                                Long id = (long) idCell.getNumericCellValue();
                                user.setId(id);
                                break;
                            default:
                                throw new IllegalArgumentException("Cột ID không hợp lệ tại dòng " + (i + 1));
                        }
                    } else {
                        throw new IllegalArgumentException("Cột ID bị trống tại dòng " + (i + 1));
                    }

                    user.setFullName(row.getCell(1).getStringCellValue());
                    user.setAddress(row.getCell(2).getStringCellValue());
                    user.setPhoneNumber(row.getCell(3).getStringCellValue());

                    // Lấy tên vai trò từ file Excel
                    String roleName = row.getCell(4).getStringCellValue();

                    // Tìm Role từ database
                    Role role = roleRepo.findByName(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Vai trò không tồn tại: " + roleName));

                    // Gán role cho user
                    user.setRole(role);
                    Cell dateCell = row.getCell(5);
                    if (dateCell != null) {
                        switch (dateCell.getCellType()) {
                            case STRING:
                                // Nếu ngày sinh ở dạng chuỗi
                                String dateStr = dateCell.getStringCellValue();
                                try {
                                    user.setDateOfBirth(dateFormat.parse(dateStr));
                                } catch (ParseException e) {
                                    throw new IllegalArgumentException("Ngày sinh không hợp lệ tại dòng: " + (i + 1), e);
                                }
                                break;
                            case NUMERIC:
                                // Nếu ngày sinh là kiểu ngày tháng
                                if (DateUtil.isCellDateFormatted(dateCell)) {
                                    user.setDateOfBirth(dateCell.getDateCellValue());
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Cột ngày sinh không hợp lệ tại dòng " + (i + 1));
                        }
                    }

                    user.setPassword(row.getCell(6).getStringCellValue());
                    // Lưu user vào database
                    userRepository.save(user);
                }
            }
        }

        System.out.println("Import danh sách user thành công!");
    }


}
