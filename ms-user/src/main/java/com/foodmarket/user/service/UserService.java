package com.foodmarket.user.service;

import com.foodmarket.user.dto.*;
import com.foodmarket.user.exception.*;
import com.foodmarket.user.model.*;
import com.foodmarket.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * IE 1.2.1 - CAPA SERVICE: logica de negocio de perfiles y direcciones
 * IE 2.3.2 - Logs con @Slf4j para trazabilidad
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserProfileRepository profileRepo;
    private final AddressRepository addressRepo;

    public UserProfileDTO createProfile(UserProfileDTO dto) {
        if (profileRepo.existsByUserId(dto.getUserId())) {
            throw new BusinessException("Ya existe perfil para el usuario: " + dto.getUserId());
        }
        UserProfile p = UserProfile.builder()
                .userId(dto.getUserId()).fullName(dto.getFullName())
                .phone(dto.getPhone()).email(dto.getEmail()).role(dto.getRole())
                .build();
        profileRepo.save(p);
        log.info("Perfil creado para usuario {}", dto.getUserId());
        return dto;
    }

    public UserProfileDTO getProfile(Long userId) {
        UserProfile p = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado: " + userId));
        return UserProfileDTO.builder()
                .userId(p.getUserId()).fullName(p.getFullName())
                .phone(p.getPhone()).email(p.getEmail()).role(p.getRole())
                .build();
    }

    public Address addAddress(Long userId, AddressDTO dto) {
        Address a = Address.builder()
                .userId(userId).street(dto.getStreet())
                .city(dto.getCity()).zone(dto.getZone())
                .active(true).defaultAddr(dto.isDefaultAddr())
                .build();
        addressRepo.save(a);
        log.info("Direccion agregada para usuario {}: {}", userId, dto.getStreet());
        return a;
    }

    public List<Address> getAddresses(Long userId) {
        return addressRepo.findByUserIdAndActiveTrue(userId);
    }

    public void deleteAddress(Long addressId, Long userId) {
        Address a = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Direccion no encontrada: " + addressId));
        if (!a.getUserId().equals(userId)) {
            throw new BusinessException("No autorizado para eliminar esta direccion");
        }
        a.setActive(false);
        addressRepo.save(a);
        log.info("Direccion {} desactivada para usuario {}", addressId, userId);
    }
}
