package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponses {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private Boolean active;



    @JsonProperty("role")
    private RoleResponse role;

    public static UserResponses fromUser(User user) {
        RoleResponse roleResponse = user.getRole() != null
                ? new RoleResponse(user.getRole().getId(), user.getRole().getName())
                : null;

        return UserResponses.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .active(user.getActive())

                .role(roleResponse)
                .build();
    }
}
