package com.foodmarket.notification.kafka;
import com.foodmarket.notification.model.Notification;
import com.foodmarket.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener; import org.springframework.stereotype.Component;
// IE 2.4.1 - Consumers Kafka para 6 topicos
@Component @RequiredArgsConstructor @Slf4j
public class OrderEventConsumer {
    private final NotificationRepository notifRepo;
    @KafkaListener(topics="order.placed", groupId="notification-group")
    public void onOrderPlaced(String orderId) { log.info("Evento order.placed para orden {}", orderId); save(null,"ORDER_PLACED","Nuevo pedido recibido #"+orderId); }
    @KafkaListener(topics="order.confirmed", groupId="notification-group")
    public void onOrderConfirmed(String orderId) { log.info("Evento order.confirmed para orden {}", orderId); save(null,"ORDER_CONFIRMED","Tu pedido #"+orderId+" fue confirmado"); }
    @KafkaListener(topics="payment.completed", groupId="notification-group")
    public void onPaymentCompleted(String orderId) { log.info("Evento payment.completed para orden {}", orderId); save(null,"PAYMENT_OK","Pago exitoso para pedido #"+orderId); }
    @KafkaListener(topics="payment.failed", groupId="notification-group")
    public void onPaymentFailed(String orderId) { log.warn("Evento payment.failed para orden {}", orderId); save(null,"PAYMENT_FAILED","Pago fallido para pedido #"+orderId+". Reintenta."); }
    @KafkaListener(topics="order.delivered", groupId="notification-group")
    public void onOrderDelivered(String orderId) { log.info("Evento order.delivered para orden {}", orderId); save(null,"ORDER_DELIVERED","Tu pedido #"+orderId+" fue entregado. Califícalo!"); }
    @KafkaListener(topics="stock.low", groupId="notification-group")
    public void onStockLow(String data) { log.warn("ALERTA STOCK BAJO: {}", data); save(null,"STOCK_LOW","ALERTA: Stock bajo detectado - "+data); }
    private void save(Long recipientId, String type, String message) {
        try { notifRepo.save(Notification.builder().recipientId(recipientId).type(type).message(message).build()); }
        catch(Exception e) { log.error("Error guardando notificacion: {}", e.getMessage()); }
    }
}