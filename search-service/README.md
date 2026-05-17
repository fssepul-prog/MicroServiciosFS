# search-service

## Informacion
- **Puerto:** 8089
- **Base de datos:** db_search
- **Responsable:** Integrante 2
- **Descripcion:** Busqueda de restaurantes

## Como abrir en IntelliJ
1. File -> Open -> seleccionar la carpeta `search-service/`
2. IntelliJ detecta el pom.xml automaticamente
3. Esperar que Maven descargue las dependencias
4. Ejecutar la clase Application (boton verde Run)

## Endpoints principales
- GET /search/restaurants?zone=X

## Commits requeridos (IE 2.5.1)
```
feat(search-service): modelar entidades JPA y configurar application.properties
feat(search-service): implementar Service con logica de negocio
feat(search-service): agregar Controller REST y GlobalExceptionHandler
```
