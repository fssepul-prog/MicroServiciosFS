package com.foodmarket.restaurant.controller;
import com.foodmarket.restaurant.dto.*;
import com.foodmarket.restaurant.model.RestaurantStatus;
import com.foodmarket.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// Endpoints REST con rutas semanticas y ResponseEntity
@RestController @RequestMapping("/restaurants") @RequiredArgsConstructor @Slf4j
public class RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@Valid @RequestBody RestaurantDTO dto,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {
        if (!role.equals("RESTAURANT_OWNER") && !role.equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        log.info("Creando restaurante por: {}", email);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.create(dto, 1L));
    }
    @GetMapping("/{id}") public ResponseEntity<RestaurantDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(restaurantService.getById(id)); }
    @GetMapping("/zone/{zone}") public ResponseEntity<List<RestaurantDTO>> getByZone(@PathVariable String zone) { return ResponseEntity.ok(restaurantService.getByZone(zone)); }
    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemDTO> addMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemDTO dto) {
        log.info("Agregando item al menu del restaurante {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.addMenuItem(id, dto));
    }
    @GetMapping("/{id}/menu") public ResponseEntity<List<MenuItemDTO>> getMenu(@PathVariable Long id) { return ResponseEntity.ok(restaurantService.getAvailableMenu(id)); }
    @PatchMapping("/{id}/menu/{itemId}/stock")
    public ResponseEntity<MenuItemDTO> updateStock(@PathVariable Long id, @PathVariable Long itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(restaurantService.updateStock(itemId, quantity));
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam RestaurantStatus status) {
        restaurantService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}