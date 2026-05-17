package com.foodmarket.report.controller;

import com.foodmarket.report.model.OrderSummary;
import com.foodmarket.report.repository.OrderSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * IE 1.2.1 - CAPA CONTROLLER: expone reportes solo para ADMIN
 * IE 2.3.2 - @Slf4j para log de accesos y consultas de reporte
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final OrderSummaryRepository summaryRepo;

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<OrderSummary>> getByRestaurant(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            log.warn("[REPORT] Acceso denegado a reporte por restaurante. Rol: {}", role);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("[REPORT] Reporte por restaurante {} solicitado por ADMIN", id);
        List<OrderSummary> result = summaryRepo.findByRestaurantIdOrderByOccurredAtDesc(id);
        log.info("[REPORT] {} registros encontrados para restaurante {}", result.size(), id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderSummary>> getAll(
            @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            log.warn("[REPORT] Acceso denegado a reporte global. Rol: {}", role);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("[REPORT] Reporte global solicitado por ADMIN");
        List<OrderSummary> result = summaryRepo.findAll();
        log.info("[REPORT] {} registros en reporte global", result.size());
        return ResponseEntity.ok(result);
    }
}
