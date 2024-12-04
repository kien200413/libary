package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {


    private Long id;
    @NotBlank(message = "Không được để trống")
    @Size(min=3,max=200,message = "Giá trị nằm trong khonagr 3-200")
    private String name;

    @Min(value = 0,message = "Giá lớn hơn hoặc băng 0")
    @Max(value = 10000000,message = "Giá trị nằm ít hơn 10 triệu")
    private Float price;


    private String 	author;
    private String thumbnail;

    private String description;
@JsonProperty("category_id")
    private Long categoryId;
//private List<MultipartFile> files;

private String catalogue;

}
