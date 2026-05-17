# report-service

## Informacion
- **Puerto:** 8090
- **Base de datos:** db_report
- **Responsable:** Integrante 2
- **Descripcion:** Reportes solo ADMIN

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `report-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- GET /reports/all

## Commits requeridos (IE 2.5.1)
```
feat(report-service): modelar entidades JPA y configurar application.properties
feat(report-service): implementar Service con logica de negocio
feat(report-service): agregar Controller REST y GlobalExceptionHandler
```
