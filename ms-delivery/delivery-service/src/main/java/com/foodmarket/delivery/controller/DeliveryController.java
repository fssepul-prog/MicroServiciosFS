package com.foodmarket.delivery.controller;
import com.foodmarket.delivery.dto.*; import com.foodmarket.delivery.model.DeliveryStatus; import com.foodmarket.delivery.service.DeliveryService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/deliveries") @RequiredArgsConstructor @Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;
    @PostMapping("/agents") public ResponseEntity<DeliveryResponseDTO> registerAgent(@Valid @RequestBody AgentDTO dto) { log.info("Registrando repartidor: {}", dto.getName()); return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.registerAgent(dto)); }
    @PostMapping("/assign") public ResponseEntity<DeliveryResponseDTO> assign(@Valid @RequestBody AssignDeliveryDTO dto) { log.info("Asignando repartidor para pedido {} en zona {}", dto.getOrderId(), dto.getZone()); return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.assign(dto)); }
    @PatchMapping("/{id}/status") public ResponseEntity<DeliveryResponseDTO> updateStatus(@PathVariable Long id, @RequestParam DeliveryStatus status) { log.info("Estado entrega {} -> {}", id, status); return ResponseEntity.ok(deliveryService.updateStatus(id, status)); }
    @GetMapping("/order/{id}") public ResponseEntity<DeliveryResponseDTO> getByOrder(@PathVariable Long id) { return ResponseEntity.ok(deliveryService.getByOrder(id)); }
}