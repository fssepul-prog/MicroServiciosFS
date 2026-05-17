# delivery-service

## Informacion
- **Puerto:** 8086
- **Base de datos:** db_delivery
- **Responsable:** Integrante 2
- **Descripcion:** Entregas por zona

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `delivery-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- POST /deliveries/assign
- PATCH /deliveries/{id}/status

## Commits requeridos (IE 2.5.1)
```
feat(delivery-service): modelar entidades JPA y configurar application.properties
feat(delivery-service): implementar Service con logica de negocio
feat(delivery-service): agregar Controller REST y GlobalExceptionHandler
```
