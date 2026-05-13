package com.foodmarket.auth.controller;
import com.foodmarket.auth.dto.*;
import com.foodmarket.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
/**
 * CAPA CONTROLLER: SOLO orquesta, NO contiene logica de negocio
 * Endpoints REST con rutas semanticas y ResponseEntity
 *
 * Rutas publicas (sin JWT):
 *   POST /auth/register  -> registrar cuenta nueva
 *   POST /auth/login     -> obtener token JWT
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterDTO dto) {
        log.info("Solicitud de registro: {}", dto.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)   // 201 Created
                .body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dto) {
        log.info("Solicitud de login: {}", dto.getEmail());
        return ResponseEntity.ok(authService.login(dto));  // 200 OK
    }
}
