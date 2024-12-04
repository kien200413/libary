package com.example.backendthuvien.Controller;

import com.example.backendthuvien.DTO.UpdateUserDTO;
import com.example.backendthuvien.DTO.UserDTO;
import com.example.backendthuvien.DTO.UserLoginDto;
import com.example.backendthuvien.Services.ExcelExportService;
import com.example.backendthuvien.Services.UserService;
import com.example.backendthuvien.entity.User;
import com.example.backendthuvien.reponse.LoginRespone;
import com.example.backendthuvien.reponse.RegisterRespone;
import com.example.backendthuvien.reponse.UserResponses;
import com.example.backendthuvien.util.LocalizationUtils;
import com.example.backendthuvien.util.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserCtrl {

    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private UserService userService;

    private final LocalizationUtils localizationUtils;
    private static final Logger log = LoggerFactory.getLogger(UserCtrl.class);



    @PostMapping("/register")

    public ResponseEntity<RegisterRespone> addUser(
            @Validated @RequestBody UserDTO userDto,
            BindingResult result) {

        // Lấy recaptchaToken từ DTO

        // Xác thực reCAPTCHA


        // Xử lý logic đăng ký nếu reCAPTCHA hợp lệ
        try {
            if (result.hasErrors()) {
                List<String> err = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                log.warn("Lỗi xác thực dữ liệu từ client: {}", err);
                return ResponseEntity.badRequest().body(
                        RegisterRespone.builder()
                                .message("Dữ liệu không hợp lệ: " + String.join(", ", err))
                                .build()
                );
            }

            // Kiểm tra mật khẩu
            if (!userDto.getPassword().equals(userDto.getRetypePassword())) {
                log.warn("Mật khẩu không khớp cho số điện thoại: {}", userDto.getPhoneNumber());
                return ResponseEntity.badRequest().body(
                        RegisterRespone.builder()
                                .message("Mật khẩu không khớp. Vui lòng kiểm tra lại.")
                                .build()
                );
            }

            // Tạo người dùng
            User user = userService.creatUser(userDto);
            log.info("Người dùng đăng ký thành công: {}", user.getPhoneNumber());
            return ResponseEntity.ok().body(
                    RegisterRespone.builder()
                            .message("Đăng ký thành công. Chào mừng bạn!")
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            log.error("Lỗi trùng lặp dữ liệu (Số điện thoại đã tồn tại): {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    RegisterRespone.builder()
                            .message("Số điện thoại đã tồn tại. Vui lòng chọn số khác.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Lỗi không xác định khi đăng ký: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    RegisterRespone.builder()
                            .message("Có lỗi xảy ra khi xử lý đăng ký. Vui lòng thử lại sau.")
                            .build()
            );
        }
    }


//    private boolean isRecaptchaValid(String recaptchaToken) {
//        String secretKey = "6LdHQ4wqAAAAAEOJJ6Ntor8LOKpdLfK0QEdzNigX"; // Secret Key từ Google reCAPTCHA Admin Console
//        String url = "https://www.google.com/recaptcha/api/siteverify";
//
//        // Kiểm tra xem token có hợp lệ không
//        if (recaptchaToken == null || recaptchaToken.isEmpty()) {
//            log.error("ReCAPTCHA token is null or empty.");
//            return false;
//        }
//
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, String> body = Map.of(
//                "secret", secretKey,
//                "response", recaptchaToken
//        );
//
//        try {
//            // Log payload gửi đến Google API
//            log.info("Sending payload to Google API: secret={}, response={}", secretKey, recaptchaToken);
//
//            // Gửi yêu cầu POST đến Google API
//            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
//
//            // Log trạng thái phản hồi từ Google API
//            log.info("Response status from Google API: {}", response.getStatusCode());
//            log.info("Response body from Google API: {}", response.getBody());
//
//            // Kiểm tra phản hồi từ Google API
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                Map<String, Object> responseBody = response.getBody();
//                Boolean success = (Boolean) responseBody.get("success");
//
//                // Kiểm tra kết quả xác thực
//                if (Boolean.TRUE.equals(success)) {
//                    log.info("reCAPTCHA verification successful.");
//                    return true;
//                } else {
//                    // Log mã lỗi nếu có
//                    List<String> errorCodes = (List<String>) responseBody.get("error-codes");
//                    log.error("reCAPTCHA verification failed. Errors: {}", errorCodes);
//                }
//            } else {
//                log.error("Unexpected response status from Google reCAPTCHA API: {}", response.getStatusCode());
//            }
//        } catch (Exception e) {
//            log.error("Error during reCAPTCHA verification: {}", e.getMessage(), e);
//        }
//
//        return false;
//    }



    @PostMapping("/login")
    public ResponseEntity<LoginRespone> login(@Validated @RequestBody UserLoginDto userLoginDto) {
        try {
            String token = userService.login(userLoginDto.getPhoneNumber(), userLoginDto.getPassword(),userLoginDto.getRoleId());

            return ResponseEntity.ok(LoginRespone.builder()
                    .message(localizationUtils.getLocal(MessageKey.LOGIN_SUCCESSFULLY))
                    .token(token)

                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(LoginRespone.builder().message(localizationUtils.getLocal(MessageKey.LOGIN_FAILED)).build());//kiem tr athong tin dang nhap va sinh token => tra ve respone

        }

    }
    @PostMapping("/details")
    public ResponseEntity<UserResponses> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " từ chuỗi token
            String extractedToken = token.substring(7);

            // Gọi phương thức từ service để lấy thông tin người dùng từ token
            User user = userService.getUserDetailFromToken(extractedToken);

            // Trả về thông tin người dùng dưới dạng UserResponse
            return ResponseEntity.ok(UserResponses.fromUser(user));
        } catch (Exception e) {
            // Xử lý nếu có lỗi xảy ra
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponses> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updatedUserDTO,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // Lấy token từ header
            String extractedToken = authorizationHeader.substring(7); // Giả sử "Bearer " là tiền tố

            // Lấy chi tiết người dùng từ token
            User user = userService.getUserDetailFromToken(extractedToken);

            // Kiểm tra xem người dùng yêu cầu có đúng với người dùng đang cập nhật không
            if (user.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Cập nhật thông tin người dùng
            User updatedUser = userService.updateUser(userId, updatedUserDTO);

            // Trả về phản hồi thành công với thông tin người dùng đã cập nhật
            return ResponseEntity.ok(UserResponses.fromUser(updatedUser));

        } catch (Exception e) {
            // Trả về phản hồi lỗi với mã trạng thái 400 (BAD_REQUEST)
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/me")
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " từ token
            String extractedToken = token.substring(7);

            // Lấy user từ token
            User user = userService.getUserDetailFromToken(extractedToken);

            // Trả về thông tin user
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Lỗi khi lấy user từ token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/export-users")
    public ResponseEntity<?> exportUsers() {
        try {
            // Đường dẫn cố định để lưu file
            String directoryPath = "E:\\thuctap\\duanbackend\\userExcel";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            // Tạo tên file với timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "user_list_" + timestamp + ".xlsx";
            String filePath = directoryPath + "\\" + fileName;

            // Gọi service để xuất file Excel
            excelExportService.exportUsersToExcel(filePath);

            // Kiểm tra file có tồn tại hay không
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("File không tồn tại!");
            }

            // Đọc file và trả về cho người dùng
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);

        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi để debug
            return ResponseEntity.status(500).body("Không thể xuất file Excel!");
        }
    }
    @PostMapping("/import-users")
    public ResponseEntity<?> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"message\": \"File không được để trống!\"}");
            }

            // Xử lý import file Excel
            excelExportService.importUsersFromExcel(file);

            return ResponseEntity.ok("{\"message\": \"Import danh sách user thành công!\"}");
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi
            return ResponseEntity.status(500).body("{\"message\": \"Không thể import file Excel!\"}");
        }
    }


    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
