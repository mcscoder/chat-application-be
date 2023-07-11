package com.chatapplication.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatapplication.dto.authentication.AuthenticationRequest;
import com.chatapplication.dto.authentication.AuthenticationResponse;
import com.chatapplication.dto.authentication.AuthenticationResponseType;
import com.chatapplication.model.User;
import com.chatapplication.repository.UserRepository;
import com.chatapplication.security.JwtUtils;
import com.chatapplication.service.user.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthenticationResponse register(AuthenticationRequest authenticationRequest) {

        User user = userRepository.findFirstByUsername(authenticationRequest.getUsername());
        if (user != null) {
            return AuthenticationResponse.builder()
                    .responseType(AuthenticationResponseType.EXISTED)
                    .build();
        }

        User newUser = User.builder()
                .username(authenticationRequest.getUsername())
                .password(passwordEncoder.encode(authenticationRequest.getPassword()))
                .build();

        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .username(newUser.getUsername())
                .userId(newUser.getId())
                .responseType(AuthenticationResponseType.CREATED)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        System.out.println("OK");
        User user = userRepository.findFirstByUsername(authenticationRequest.getUsername());
        if (user == null) {
            return AuthenticationResponse.builder()
                    .responseType(AuthenticationResponseType.INCORRECT).build();
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .responseType(AuthenticationResponseType.CORRECT)
                .build();
    }

    @Override
    public AuthenticationResponse tokenAuthenticate(HttpServletRequest request) {
        String requestUsername = jwtUtils.extractUsername(request);
        User user = userRepository.findFirstByUsername(requestUsername);

        // If the user is not correct
        if (user == null) {
            return AuthenticationResponse.builder()
                    .responseType(AuthenticationResponseType.INCORRECT)
                    .build();
        }

        String token = jwtUtils.generateToken(requestUsername);

        return AuthenticationResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .responseType(AuthenticationResponseType.CORRECT)
                .build();
    }

}
