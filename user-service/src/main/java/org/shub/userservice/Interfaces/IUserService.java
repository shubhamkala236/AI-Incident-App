package org.shub.userservice.Interfaces;

import org.shub.userservice.dto.AuthResponse;
import org.shub.userservice.dto.LoginRequest;
import org.shub.userservice.dto.RegisterRequest;

public interface IUserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
