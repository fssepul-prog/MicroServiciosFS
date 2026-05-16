package com.foodmarket.order.controller;
import com.foodmarket.order.dto.*;
import com.foodmarket.order.model.OrderStatus;
import com.foodmarket.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/orders") @RequiredArgsConstructor @Slf4j
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody CreateOrderDTO dto,
            @RequestHeader("X-User-Email") String email, @RequestHeader("X-User-Role") String role) {
        log.info("Crear pedido por: {}", email);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto, 1L));
    }
    @GetMapping("/{id}") public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(orderService.getById(id)); }
    @GetMapping("/customer/{id}") public ResponseEntity<List<OrderResponseDTO>> getByCustomer(@PathVariable Long id) { return ResponseEntity.ok(orderService.getByCustomer(id)); }
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable Long id,
            @RequestParam OrderStatus newStatus, @RequestHeader("X-User-Role") String role) {
        log.info("Cambio estado pedido {} a {}", id, newStatus);
        return ResponseEntity.ok(orderService.updateStatus(id, newStatus, role));
    }
}