package com.example.backendthuvien.Config;

import com.example.backendthuvien.entity.Role;
import com.example.backendthuvien.fillter.JwtTokenFillter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration

@EnableWebMvc

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebScurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFillter jwtTokenFillter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFillter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    // Cho phép đăng ký và đăng nhập mà không cần xác thực
                    requests.requestMatchers(
                            String.format("%s/users/register", apiPrefix),

                            String.format("%s/users/login", apiPrefix)

                    ).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/users/export-users/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, String.format("%s/users", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, String.format("%s/users/import-users/**", apiPrefix)).permitAll()
                            .requestMatchers(String.format("%s/users/details", apiPrefix)).hasAnyRole("USER","ADMIN")
                            // Cho phép truy cập roles mà không cần xác thực
                            .requestMatchers(HttpMethod.GET, String.format("%s/roles/**", apiPrefix)).permitAll()

                            // Các endpoint liên quan đến orders_details
                            .requestMatchers(HttpMethod.GET, String.format("%s/orders_details/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/orders_details/order_user", apiPrefix)).hasRole("USER")
                            .requestMatchers(HttpMethod.POST, String.format("%s/orders_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/orders_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/orders_details/**", apiPrefix)).hasRole("ADMIN")

                            // Các endpoint liên quan đến products
                            .requestMatchers(HttpMethod.GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/products/images/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, String.format("%s/products/uploads/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            // Các endpoint liên quan đến categories
                            .requestMatchers(HttpMethod.GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/categories/search/**", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.POST, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, String.format("%s/coupons/**", apiPrefix)).permitAll()
                            // Cho phép tất cả người dùng xem danh sách mã giảm giá

                            .requestMatchers(HttpMethod.GET, String.format("%s/coupons/**", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            // Chỉ cho phép USER hoặc ADMIN xem chi tiết mã giảm giá

                            .requestMatchers(HttpMethod.POST, String.format("%s/coupons", apiPrefix)).hasRole("ADMIN")
// ADMIN có quyền thêm mã giảm giá

                            .requestMatchers(HttpMethod.PUT, String.format("%s/coupons/**", apiPrefix)).hasRole("ADMIN")
// ADMIN có quyền sửa mã giảm giá

                            .requestMatchers(HttpMethod.DELETE, String.format("%s/coupons/**", apiPrefix)).hasRole("ADMIN") // ADMIN có quyền xóa mã giảm giá


                            // Các endpoint liên quan đến orders
                            .requestMatchers(HttpMethod.PUT, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, String.format("%s/orders/**", apiPrefix)).permitAll()

//                            .requestMatchers(HttpMethod.GET, String.format("%s/orders/get-orders-by-keyword", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/orders/user/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, String.format("%s/statistics/weekly", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, String.format("%s/statistics/monthly", apiPrefix)).hasRole("ADMIN")

                            // Các request khác đều yêu cầu xác thực
                            .anyRequest().authenticated();
                });

        // Cấu hình CORS
        httpSecurity.cors(cors -> {
            CorsConfiguration configuration = new CorsConfiguration();

            // Cho phép tất cả các nguồn gốc (hoặc cụ thể với frontend của bạn)
            configuration.setAllowedOrigins(List.of("http://localhost:4200"));  // Frontend cụ thể

            // Cho phép các phương thức HTTP
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

            // Định nghĩa các header được phép
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));

            // Phơi bày các header nhất định trong phản hồi
            configuration.setExposedHeaders(List.of("x-auth-token"));

            // Đăng ký cấu hình CORS cho tất cả các URL
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            configuration.setAllowCredentials(true);

            // Áp dụng cấu hình CORS cho HttpSecurity
            cors.configurationSource(source);
        });

        return httpSecurity.build();
    }

}
