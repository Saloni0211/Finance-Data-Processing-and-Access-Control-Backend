package com.example.finance.user.dto;

import com.example.finance.user.model.Role;
import com.example.finance.user.model.UserStatus;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    private Role role;
    private UserStatus status;

    @Size(min = 6, max = 100)
    private String password;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
