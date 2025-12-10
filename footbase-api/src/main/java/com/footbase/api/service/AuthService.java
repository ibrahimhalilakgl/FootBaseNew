package com.footbase.api.service;

import com.footbase.api.domain.Role;
import com.footbase.api.domain.UserAccount;
import com.footbase.api.dto.AuthRequest;
import com.footbase.api.dto.AuthResponse;
import com.footbase.api.dto.PasswordResetRequest;
import com.footbase.api.dto.RegisterRequest;
import com.footbase.api.exception.ConflictException;
import com.footbase.api.exception.NotFoundException;
import com.footbase.api.repository.RoleRepository;
import com.footbase.api.repository.UserRepository;
import com.footbase.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new ConflictException("Bu e-posta adresi zaten kullanımda");
        }
        
        // Kullanıcı adı kontrolü
        if (userRepository.existsByDisplayName(request.getDisplayName())) {
            throw new ConflictException("Bu kullanıcı adı zaten kullanımda");
        }
        
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        UserAccount user = UserAccount.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .build();
        user.getRoles().add(userRole);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtService.generateToken(request.getEmail());
        return new AuthResponse(token);
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        UserAccount user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

