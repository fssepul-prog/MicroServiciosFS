# notification-service

## Informacion
- **Puerto:** 8087
- **Base de datos:** db_notification
- **Responsable:** Integrante 2
- **Descripcion:** Alertas via Kafka

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `notification-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- GET /notifications/{userId}

## Commits requeridos (IE 2.5.1)
```
feat(notification-service): modelar entidades JPA y configurar application.properties
feat(notification-service): implementar Service con logica de negocio
feat(notification-service): agregar Controller REST y GlobalExceptionHandler
```
