# ms-auth

## Información del microservicio
- **Puerto:** 8081
- **Base de datos:** db_auth
- **Responsable:** Integrante 1
- **Descripción:** Registro, login y JWT

## Cómo abrir en IntelliJ
1. `File` → `Open` → seleccionar la carpeta `ms-auth/`
2. IntelliJ detecta el `pom.xml` automáticamente y configura Maven
3. Esperar el mensaje "Build completed" en la barra inferior
4. Ejecutar la clase `Application` con el botón verde **Run**

## Endpoints principales
`POST /auth/register | POST /auth/login`

## Commits sugeridos para este microservicio (IE 2.5.1)

```bash
git checkout main && git pull origin main
git checkout -b feat/ms-auth

# Copiar la carpeta ms-auth/ al repositorio clonado
git add ms-auth/
git commit -m "feat(ms-auth): modelar entidades JPA y configurar application.properties"
git commit -m "feat(ms-auth): implementar Service con lógica de negocio y logs SLF4J"
git commit -m "feat(ms-auth): agregar Controller REST y GlobalExceptionHandler"

git push origin feat/ms-auth
# En GitHub: Pull Request → merge a main
```
