package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.UpdateUserDTO;
import com.example.backendthuvien.DTO.UserDTO;
import com.example.backendthuvien.Repositories.RoleRepository;
import com.example.backendthuvien.Repositories.UserRepository;
import com.example.backendthuvien.components.JwtTokenUtil;
import com.example.backendthuvien.entity.Role;
import com.example.backendthuvien.entity.User;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import com.example.backendthuvien.exceptions.PermissionDenyException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class UserService implements iUserService {
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private Long roleId = 2L; // Mặc định là 2 cho vai trò USER

    @Override
    public User creatUser(UserDTO userDTO) throws  Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("SĐT đã tồn tại");
        }


        Role userRole = roleRepository.findById(2L).orElseThrow(() -> new DataNotFoundException("Role USER not found"));



        // Mã hóa mật khẩu ngay từ đầu
//        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Tạo đối tượng User với mật khẩu đã mã hóa
        User newuser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword()) // Đặt mật khẩu đã mã hóa vào đây
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
//                .facebookAcountId(userDTO.getFacebookAcountId())
                .role(userRole)
//                .googleAcountId(userDTO.getGoogleAcountId())
                .active(true)
                .build();



        System.out.println("User Password After Encoding: " + newuser.getPassword()); // Kiểm tra mật khẩu đã mã hóa

        return userRepository.save(newuser);
    }


    @Override
    public String login(String phoneNumber, String password ,long roleId) throws Exception {
        Optional<User> opyionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (opyionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phonenumber/password");
        }
        User existing=opyionalUser.get();
        if (existing.getRole().getId() != roleId) {
            throw new BadCredentialsException("User role does not match the required role");
        }
        //check password
        if (existing.getFacebookAcountId() == 0 && existing.getGoogleAcountId() == 0) {
            if(!passwordEncoder.matches(password,existing.getPassword())){
                throw new BadCredentialsException("wrong phoneNumber or passwork");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                phoneNumber,password,existing.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existing);
    }

    @Override
    public User getUserDetailFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expỉed");
        }
        String phoneNumber=jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user=userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new Exception("User not found");
        }
    }
    @Transactional
    @Override
    public User updateUser(long userId, UpdateUserDTO userDTO) throws Exception {
        // Tìm người dùng hiện tại theo userId
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra nếu số điện thoại đã tồn tại cho người dùng khác
        if (phoneNumber != null && !existingUser.getPhoneNumber().equals(phoneNumber) &&
                userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("SĐT đã tồn tại");
        }

        // Tìm role từ roleId trong userDTO


        // Không cho phép cập nhật thành quyền ADMIN nếu không được phép


        // Cập nhật thông tin người dùng từ userDTO nếu không phải null
        if (userDTO.getFullName() != null) {
            existingUser.setFullName(userDTO.getFullName());
        }
        if (phoneNumber != null) {
            existingUser.setPhoneNumber(phoneNumber);
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        }
        if (userDTO.getFacebookAcountId() > 0) {
            existingUser.setFacebookAcountId(userDTO.getFacebookAcountId());
        }
        if (userDTO.getGoogleAcountId() > 0) {
            existingUser.setGoogleAcountId(userDTO.getGoogleAcountId());
        }



        // Kiểm tra và mã hóa mật khẩu mới (nếu có)
        if (userDTO.getFacebookAcountId() == 0 && userDTO.getGoogleAcountId() == 0) {
            String password = userDTO.getPassword();
            if (password != null && !password.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(password);
                existingUser.setPassword(encodedPassword);
            }
        }

        // Lưu người dùng đã cập nhật vào cơ sở dữ liệu
        return userRepository.save(existingUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
