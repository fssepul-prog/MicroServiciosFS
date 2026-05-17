# review-service

## Informacion
- **Puerto:** 8088
- **Base de datos:** db_review
- **Responsable:** Integrante 2
- **Descripcion:** Resenas con Feign

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `review-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- POST /reviews
- GET /reviews/restaurant/{id}/average

## Commits requeridos (IE 2.5.1)
```
feat(review-service): modelar entidades JPA y configurar application.properties
feat(review-service): implementar Service con logica de negocio
feat(review-service): agregar Controller REST y GlobalExceptionHandler
```
