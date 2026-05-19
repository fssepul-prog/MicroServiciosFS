# payment-service

## Informacion
- **Puerto:** 8085
- **Base de datos:** db_payment
- **Responsable:** Integrante 2
- **Descripcion:** Pagos y reembolsos

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `payment-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- POST /payments
- POST /payments/{id}/refund

## Commits requeridos (IE 2.5.1)
```
feat(payment-service): modelar entidades JPA y configurar application.properties
feat(payment-service): implementar Service con logica de negocio
feat(payment-service): agregar Controller REST y GlobalExceptionHandler
```
