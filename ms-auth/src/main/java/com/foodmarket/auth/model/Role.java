package com.foodmarket.auth.model;
/**  Los 4 roles del sistema para control de acceso (RBAC) */
public enum Role {
    CUSTOMER,           // Cliente que realiza pedidos
    RESTAURANT_OWNER,   // Dueno de restaurante
    DELIVERY_AGENT,     // Repartidor
    ADMIN               // Administrador del sistema
}
