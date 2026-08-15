package com.oji.mini_crm_server.dto;

import com.oji.mini_crm_server.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String userName;
    private String fullName;
    private String email;
    private String password;
    private Role role;
}
