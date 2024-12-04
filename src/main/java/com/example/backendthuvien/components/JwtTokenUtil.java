package com.example.backendthuvien.components;

import com.example.backendthuvien.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private Long expiration;// lưu voà biến môi trường

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception{
        Map<String, Object> claims = new HashMap<>();
//        this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            //sau này dùng logger
            throw new InvalidParameterException("Can not creat JWT token, error" + e.getMessage());

        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);//
        return Keys.hmacShaKeyFor(bytes);//Keys.hmacShaKeyFor(Decoders.BASE64.decode("gRBe4aIKIMbU8xiFLV5a6IUjdIMYFLmN07u78NYGxG4="));
    }
    private String generateSecretKey(){
        SecureRandom random=new SecureRandom();
        byte[] keyBytes=new byte[32];
        random.nextBytes(keyBytes);
        String secretKey= Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  <T> T extracClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
       return claimsResolver.apply(claims);
    }
    //check expiration hết hạn chưa
    public Boolean isTokenExpired(String token){//kiểm tra xem ngày đó có nằm trước ngày quá hạn hay không
        Date expirationDate=this.extracClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractPhoneNumber(String token){
        return extracClaim(token,Claims::getSubject);
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber=extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
