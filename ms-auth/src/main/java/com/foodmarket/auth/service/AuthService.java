package com.foodmarket.auth.service;
import com.foodmarket.auth.dto.*;
import com.foodmarket.auth.exception.*;
import com.foodmarket.auth.model.User;
import com.foodmarket.auth.repository.UserRepository;
import com.foodmarket.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * IE 1.2.1 - CAPA SERVICE: contiene TODA la logica de negocio
 * El Controller solo llama a este Service, NO implementa logica aqui
 * IE 2.2.1 - Reglas de negocio: email unico, BCrypt, validar credenciales
 * IE 2.3.2 - Logs @Slf4j para trazabilidad de operaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    /**
     * Registra un nuevo usuario.
     * IE 2.2.1 - Regla: el email debe ser unico en el sistema
     */
    public AuthResponseDTO register(RegisterDTO dto) {
        // Regla de negocio: email unico
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "El email ya esta registrado: " + dto.getEmail());
        }
        User user = User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword())) // BCrypt - jamas texto plano
                .role(dto.getRole())
                .build();
        userRepo.save(user);
        log.info("Usuario registrado: {} con rol {}", dto.getEmail(), dto.getRole());
        return AuthResponseDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Registro exitoso")
                .build();
    }

    /**
     * Autentica un usuario y genera un token JWT.
     */
    public AuthResponseDTO login(LoginDTO dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas"));
        // BCrypt verifica el hash almacenado contra el password ingresado
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales invalidas");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        log.info("Login exitoso: {}", dto.getEmail());
        return AuthResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login exitoso")
                .build();
    }
}
