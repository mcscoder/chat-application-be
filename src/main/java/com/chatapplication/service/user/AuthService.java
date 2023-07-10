package com.chatapplication.service.user;

import com.chatapplication.dto.authentication.AuthenticationRequest;
import com.chatapplication.dto.authentication.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse register(AuthenticationRequest authenticationRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
