package com.foodmarket.report.kafka;
import com.foodmarket.report.model.OrderSummary; import com.foodmarket.report.repository.OrderSummaryRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener; import org.springframework.stereotype.Component; import java.time.LocalDateTime;
@Component @RequiredArgsConstructor @Slf4j
public class OrderDeliveredConsumer {
    private final OrderSummaryRepository summaryRepo;
    @KafkaListener(topics="order.delivered", groupId="report-group")
    public void onOrderDelivered(String orderId) {
        log.info("Registrando en reporte pedido entregado: {}", orderId);
        try { summaryRepo.save(OrderSummary.builder().orderId(Long.valueOf(orderId)).status("DELIVERED").occurredAt(LocalDateTime.now()).build()); }
        catch(Exception e) { log.error("Error en reporte: {}", e.getMessage()); }
    }
    @KafkaListener(topics="order.placed", groupId="report-group")
    public void onOrderPlaced(String orderId) {
        log.info("Registrando nuevo pedido en reporte: {}", orderId);
        try { summaryRepo.save(OrderSummary.builder().orderId(Long.valueOf(orderId)).status("PLACED").occurredAt(LocalDateTime.now()).build()); }
        catch(Exception e) { log.error("Error: {}", e.getMessage()); }
    }
}