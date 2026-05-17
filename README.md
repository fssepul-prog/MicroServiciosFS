# MicroServiciosFS
EvaluacionN2

FoodMarket — Plataforma de Delivery con Microservicios
DSY1103 Desarrollo FullStack I · Evaluación Parcial 2
Arquitectura de microservicios Spring Boot aplicada a una plataforma de pedidos de comida a domicilio.

Propuesta del proyecto
FoodMarket es una plataforma de delivery que conecta clientes, restaurantes y repartidores. Está construida sobre una arquitectura de 12 microservicios independientes que se comunican entre sí de forma síncrona (OpenFeign) y asíncrona (Apache Kafka).

Cada microservicio tiene su propia base de datos MySQL, su propia lógica de negocio y expone una API REST independiente. Todos los requests externos ingresan por el API Gateway, que valida el token JWT antes de enrutar la solicitud al servicio correspondiente.


Integrantes
Servicios a cargo

Felipe Echeverría
eureka-server, api-gateway, auth-service, user-service, restaurant-service, order-service

Daniel Parada
payment-service, delivery-service, notification-service, review-service, search-service, report-service










Arquitectura general
Cliente (Postman / App)

         │
         ▼

  ┌─────────────┐

  │ API Gateway │  :8080  — valida JWT, enruta requests

  └──────┬──────┘

         │

  ┌──────▼──────────────────────────────────────────────┐

  │                  Eureka Server :8761                 │

  │        Registro y descubrimiento de servicios        │

  └──────────────────────────────────────────────────────┘

         │

  ┌──────┴────────────────────────────────────────────────────┐

  │                   MICROSERVICIOS                           │

  │                                                           │

  │  auth-service        :8081   user-service       :8082     │

  │  restaurant-service  :8083   order-service       :8084    │

  │  payment-service     :8085   delivery-service    :8086    │

  │  notification-service:8087   review-service      :8088    │

  │  search-service      :8089   report-service      :8090    │
└───────────────────────────────────────────────────────────┘

         │

  ┌──────▼──────────┐

  │  Apache Kafka   │  — mensajería asíncrona entre servicios

  └─────────────────┘


Definición de microservicios
1. auth-service  Puerto 8081
Gestiona la autenticación del sistema mediante JWT. No tiene base de datos propia: los usuarios de prueba se cargan en memoria con InMemoryUserDetailsManager. Genera tokens con roles (CUSTOMER, RESTAURANT_OWNER, ADMIN) que el API Gateway propaga a todos los servicios mediante headers X-User-Email y X-User-Role.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /auth/register | Registrar cuenta nueva | | POST | /auth/login | Iniciar sesión y obtener token JWT |


2. user-service  Puerto 8082 · BD: db_user
Gestiona los perfiles de usuario y sus direcciones de entrega. Aplica la relación OneToMany entre un perfil y sus direcciones, y valida que no existan perfiles duplicados para el mismo userId.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /users/{userId}/profile | Crear perfil de usuario | | GET | /users/{userId}/profile | Obtener perfil | | POST | /users/{userId}/addresses | Agregar dirección | | GET | /users/{userId}/addresses | Listar direcciones activas | | DELETE | /users/{userId}/addresses/{addressId} | Desactivar dirección |


3. restaurant-service — Puerto 8083 · BD: db_restaurant
Administra el catálogo de restaurantes y sus menús. Implementa la regla de negocio de stock: cuando un ítem llega a stock = 0, se marca automáticamente como available = false y Kafka publica un evento stock.low. Aplica la relación ManyToOne entre MenuItem y Restaurant.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /restaurants | Crear restaurante (rol: RESTAURANT_OWNER / ADMIN) | | GET | /restaurants/{id} | Obtener restaurante por ID | | GET | /restaurants/zone/{zone} | Listar restaurantes abiertos por zona | | POST | /restaurants/{id}/menu | Agregar ítem al menú | | GET | /restaurants/{id}/menu | Ver menú disponible | | PATCH | /restaurants/{id}/menu/{itemId}/stock | Actualizar stock de ítem | | PATCH | /restaurants/{id}/status | Abrir/cerrar restaurante |

4. order-service  Puerto 8084 · BD: db_order
Es el servicio central del flujo de negocio. Crea pedidos validando en tiempo real vía OpenFeign que el restaurante esté abierto, que la zona de entrega coincida y que los ítems tengan stock. Gestiona el ciclo de vida del pedido con transiciones de estado validadas (PENDING → CONFIRMED → PREPARING → READY → IN_DELIVERY → DELIVERED). Publica eventos Kafka para comunicar cambios de estado.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /orders | Crear pedido | | GET | /orders/{id} | Obtener pedido por ID | | GET | /orders/customer/{id} | Historial de pedidos del cliente | | PATCH | /orders/{id}/status | Cambiar estado del pedido |


5. payment-service  Puerto 8085 · BD: db_payment
Simula el procesamiento de pagos. Valida que no exista ya un pago completado para la misma orden. Publica eventos payment.completed o payment.failed según el resultado. Solo el rol ADMIN puede emitir reembolsos.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /payments | Procesar pago | | GET | /payments/order/{id} | Obtener pago por orden | | GET | /payments/customer/{id} | Historial de pagos del cliente | | POST | /payments/{id}/refund | Emitir reembolso (solo ADMIN) |


6. delivery-service  Puerto 8086 · BD: db_delivery
Asigna automáticamente el primer repartidor disponible en la zona del pedido. Al completar la entrega (DELIVERED), libera al repartidor y publica eventos delivery.completed y order.delivered.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /deliveries/agents | Registrar repartidor | | POST | /deliveries/assign | Asignar repartidor a pedido | | PATCH | /deliveries/{id}/status | Actualizar estado de la entrega | | GET | /deliveries/order/{id} | Obtener entrega por pedido |

7. notification-service — Puerto 8087 · BD: db_notification
Consumer Kafka puro: escucha 6 tópicos y almacena notificaciones en la base de datos. No produce eventos, solo reacciona a los de otros servicios.

Tópicos que consume:

order.placed → "Nuevo pedido recibido"
order.confirmed → "Pedido confirmado"
payment.completed → "Pago exitoso"
payment.failed → "Pago fallido, reintenta"
order.delivered → "Pedido entregado, califícalo"
stock.low → "Alerta de stock bajo"

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | GET | /notifications/{userId} | Todas las notificaciones del usuario | | GET | /notifications/{userId}/unread | Solo las no leídas | | PATCH | /notifications/{id}/read | Marcar como leída |

8. review-service  Puerto 8088 · BD: db_review
Permite calificar restaurantes y repartidores. Valida vía OpenFeign que el pedido exista y esté en estado DELIVERED antes de permitir la reseña. Previene reseñas duplicadas del mismo tipo para el mismo pedido.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | POST | /reviews | Crear reseña | | GET | /reviews/restaurant/{id} | Reseñas de un restaurante | | GET | /reviews/agent/{id} | Reseñas de un repartidor | | GET | /reviews/restaurant/{id}/average | Rating promedio del restaurante |

9. search-service — Puerto 8089 · BD: db_search
Permite buscar restaurantes por nombre (búsqueda LIKE), zona o categoría. Mantiene un índice local (RestaurantIndex) ordenado por rating promedio. Es la única ruta pública que no requiere token JWT.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | GET | /search/restaurants?name=X | Buscar por nombre | | GET | /search/restaurants?zone=X | Buscar por zona (orden por rating) | | GET | /search/restaurants?category=X | Buscar por categoría |
10. report-service  Puerto 8090 · BD: db_report
Consume eventos Kafka de order.placed y order.delivered para registrar un historial de pedidos. Expone reportes de uso solo para el rol ADMIN.

Endpoints: | Método | Ruta | Descripción | |--------|------|-------------| | GET | /reports/all | Reporte global de pedidos (solo ADMIN) | | GET | /reports/restaurant/{id} | Reporte por restaurante (solo ADMIN) |

Flujo completo de un pedido
1. POST /auth/login            → obtener token JWT

2. GET  /search/restaurants    → buscar restaurante y ver menú

3. POST /orders                → crear pedido

                                 ├─ Feign → verifica restaurante abierto

                                 ├─ Feign → verifica stock de cada ítem

                                 └─ Kafka → publica "order.placed"

4. POST /payments              → procesar pago

                                 └─ Kafka → publica "payment.completed"

5. PATCH /orders/{id}/status   → CONFIRMED → PREPARING → READY

6. POST /deliveries/assign     → asignar repartidor disponible en la zona

7. PATCH /deliveries/{id}/status → IN_DELIVERY → DELIVERED

                                 └─ Kafka → publica "order.delivered"

8. POST /reviews               → calificar restaurante (solo si DELIVERED)

                                 └─ Feign → verifica estado del pedido




