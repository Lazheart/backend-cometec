# ComeTec Backend🍽️: Administración de Restaurantes y Menús
## CS20231 Desarrollo Basado en Plataformas

## Development Team 👥
| Nombre Completo         | Usuario GitHub     | Correo                                                                    |
| ----------------------- | ------------------ | ------------------------------------------------------------------------- |
| Benjamin Llerena        | Lazheart           | [benjamin.llerena@utec.edu.pe](mailto:benjamin.llerena@utec.edu.pe)       |
| Luciana Yangali Cáceres | Luciana-y          | [luciana.yangali@utec.edu.pe](mailto:luciana.yangali@utec.edu.pe)         |
| Leonardo Montesinos     | LeonardoMontesinos | [leonardo.montesinos@utec.edu.pe](mailto:leonardo.montesinos@utec.edu.pe) |
| Lucia Cartagena         | luciajcm           | [lucia.cartajena@utec.edu.pe](mailto:lucia.cartajena@utec.edu.pe)         |

## Introducción

### Descripción
Muchos restaurantes pequeños y medianos carecen de una solución digital personalizada que les permita interactuar directamente con sus clientes. Por un lado, los clientes no siempre tienen una manera clara y estructurada de calificar y comentar sus experiencias. Por otro lado, los dueños no tienen visibilidad clara de las opiniones de sus clientes ni herramientas que les permitan administrar eficientemente sus menús o disponibilidad de platos.

### Justificación
Ofrecer un backend robusto para este tipo de sistema ayuda a cubrir una necesidad real en el sector gastronómico: mejorar la experiencia del cliente y optimizar la gestión del restaurante. Al permitir que los usuarios dejen comentarios y califiquen los restaurantes, se crea un círculo virtuoso de retroalimentación que puede mejorar tanto el servicio como la reputación de los establecimientos. Además, la gestión dinámica del menú y platos facilita la adaptación rápida a la demanda y disponibilidad.

## Descripción de la Solución

### Funcionalidades
- **Registro y gestión de usuarios:** Separación clara entre roles de cliente y propietario.
- **Gestión de restaurantes:** Los propietarios pueden registrar su restaurante, incluyendo ubicación y carta asociada.
- **Gestión de cartas y platos:** Permite a los propietarios crear una carta, agregar platos, y gestionar su disponibilidad.
- **Sistema de reseñas:** Los clientes pueden calificar restaurantes con estrellas (0-5), lo que influye en una media de calificaciones.
- **Comentarios en reseñas:** Los usuarios pueden comentar sobre las reseñas de otros, fomentando la interacción.

### Tecnologías Utilizadas
- **Lenguaje de programación:** Java
- **Framework backend:** Spring Boot
- **Base de datos:** PostgreSQL (posiblemente administrada con Docker y Testcontainers en entornos de desarrollo)
- **ORM:** JPA (Hibernate)
- **Control de versiones:** Git + GitHub
- **Pruebas:** JUnit, Testcontainers (para pruebas de integración con la base de datos)
- **Autenticación y autorización** Spring Security con control basado en roles
- **Herramientas axuliares:** Postman(para pruebas de API)


## Prerequisites 🔧
- Java 17+
- PostgreSQL 16+
- Docker
- Maven
- Postman (for API testing)

## Setup 🚀
Para lanzar el proyecto de manera local , sigue estos pasos

1. **Clona el Repositorio**

Abre tu terminal y clona el Repositorio con el siguiente comando :
  ```sh
     git clone git@github.com:CS2031-DBP/proyecto-backend-metrodata.git
   ```
2. **Navega al proyecto**
   Cambia al directorio del proyecto

   ```sh
    cd proyecto-backend-metrodat
   ```
3. Abre el Proyecto en tu IDE preferido

   Como recomendacion usa IntelliJDEA

4. **Corre la Aplicacion**

   Ejecute `DbpBackendApplication` para iniciar la aplicación. IntelliJ   manejará automáticamente las dependencias como se especifica en el archivo `pom.xml` ya que el proyecto usa Maven.

5. **Configura la Base de Datos**

   Asegurate de tener Docker Instalado. Luego corre el siguiente comando    para incializar la Base de Datos usando Docker

   ```sh
   docker-compose up
   ```

   Ejemplo de Configuracion de Docker:

   ```yaml
   services:
     db:
       image: postgres:latest
       container_name: proyecto-backend-dev
       restart: always
       environment:
         POSTGRES_PASSWORD: password
         POSTGRES_DB: postgres
         POSTGRES_USER: postgres
       ports:
         - "5433:5432"
   ```

Seguir estos pasos asegura que el proyecto pueda ser lanzado de manera local.

## Modelo de Entidades

### Modelo de Entidad Relación

![ER Diagram](https://media.discordapp.net/attachments/1361535013195219014/1377434326727135252/postgreslocalhost.png?ex=6838f32e&is=6837a1ae&hm=1fdf7444d006cd13cc70d59c00b8639f5d416603f01acc95250da3df2245d178&=&format=webp&quality=lossless&width=716&height=1421)

### Descripción de Entidades
1. **User:**
Representa a los usuarios del sistema con diferentes roles (ADMIN, OWNER, USER).

2. **Restaurant:**
Establecimientos gastronómicos registrados en la plataforma.

3. **Menu:**
Representa la carta de un restaurante con sus platos.

4. **Dish:**
Elementos del menú ofrecidos por el restaurante.
     
5. **Location:**
Coordenadas geográficas de los restaurantes.

6. **Review:**
Valoraciones dejadas por los clientes.

7. **Comment:**
Comentarios sobre reseñas existentes.

## Endpoints 🛣️

### 👤 User
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/user/me`                    | Obtener información del usuario actual | USER             |
| GET    | `/user/{id}`                  | Obtener información de un usuario por ID | ADMIN           |
| GET    | `/user/all`                   | Listar todos los usuarios            | ADMIN            |
| PATCH  | `/user/update/me`             | Actualizar información del usuario actual | USER           |
| DELETE | `/user/{id}`                  | Eliminar usuario                     | ADMIN            |
| GET    | `/user/favourites`            | Obtener restaurantes favoritos       | USER             |
| GET    | `/user/owned-restaurants`     | Obtener restaurantes propiedad del usuario | OWNER        |
| GET    | `/user/comments`              | Obtener comentarios del usuario      | USER             |
| GET    | `/user/reviews`               | Obtener reseñas del usuario          | USER             |

### 🍽️ Restaurant
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/restaurants`                | Listar todos los restaurantes        | USER, OWNER, ADMIN |
| GET    | `/restaurants/{restaurantId}` | Obtener detalles de un restaurante   | USER, OWNER, ADMIN |
| GET    | `/restaurants/{id}/reviews`   | Obtener reseñas del restaurante      | USER, OWNER, ADMIN |
| GET    | `/restaurants/{id}/comments`  | Obtener comentarios del restaurante  | USER, OWNER, ADMIN |
| GET    | `/restaurants/{id}/menu`      | Obtener carta del restaurante        | USER, OWNER, ADMIN |
| POST   | `/restaurants`                | Crear restaurante                    | OWNER            |
| PUT    | `/restaurants/{restaurantId}` | Actualizar restaurante               | OWNER            |
| DELETE | `/restaurants/{restaurantId}` | Eliminar restaurante                 | OWNER            |

### 📝 Review
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/reviews/restaurant/{restaurantId}` | Obtener reseñas por restaurante | USER             |
| GET    | `/reviews/{reviewId}`         | Obtener reseña por ID               | ADMIN            |
| GET    | `/reviews/all`                | Obtener todas las reseñas           | USER             |
| GET    | `/reviews/me`                 | Obtener reseñas del usuario actual  | USER             |
| GET    | `/reviews/user/{userId}`      | Obtener reseñas por usuario         | USER             |
| POST   | `/reviews`                    | Crear reseña                        | USER             |
| PATCH  | `/reviews/content/{reviewId}` | Actualizar contenido de reseña      | USER             |
| PATCH  | `/reviews/dislike/{reviewId}` | Dislike a reseña                    | USER             |
| PATCH  | `/reviews/like/{reviewId}`    | Like a reseña                       | USER             |
| DELETE | `/reviews/{reviewId}`         | Eliminar reseña                     | USER             |

### 💬 Comment
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/comments/review/{reviewId}` | Obtener comentarios por reseña      | ADMIN            |
| POST   | `/comments`                   | Crear comentario                    | Todos            |
| DELETE | `/comments/{commentId}`       | Eliminar comentario                 | Todos            |

### 📋 Menu
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| POST   | `/menu/restaurants/{restaurantId}` | Crear menú para restaurante | OWNER            |
| GET    | `/menu/{menuId}`              | Obtener menú por ID                 | USER, OWNER, ADMIN |
| PUT    | `/menu/{menuId}`              | Actualizar menú                     | OWNER            |
| DELETE | `/menu/{menuId}`              | Eliminar menú                       | OWNER            |

### 🍛 Dish
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/dishes/all`                 | Obtener todos los platos            | ADMIN            |
| GET    | `/dishes/{dishId}`            | Obtener plato por ID                | ADMIN            |
| GET    | `/dishes/carta/{menuId}`      | Obtener platos por menú             | USER             |
| GET    | `/dishes/restaurant/{restaurantId}` | Obtener platos por restaurante | USER             |
| POST   | `/dishes`                     | Crear plato                         | OWNER            |
| PATCH  | `/dishes/{dishId}`            | Actualizar plato                    | OWNER            |
| DELETE | `/dishes/{dishId}`            | Eliminar plato                      | OWNER            |

### 📍 Location
| Método | EndPoint                      | Descripción                          | Roles Permitidos |
| ------ | ----------------------------- | ------------------------------------ | ---------------- |
| GET    | `/locations/restaurants/{restaurantId}` | Obtener ubicaciones de restaurante | USER, OWNER, ADMIN |
| POST   | `/locations/restaurants/{restaurantId}` | Añadir ubicación a restaurante | OWNER, ADMIN     |
| PUT    | `/locations/{locationId}`     | Actualizar ubicación                | OWNER, ADMIN     |
| DELETE | `/locations/{locationId}`     | Eliminar ubicación                  | OWNER, ADMIN     |

## Testing y Manejo de Errores

### Test Unitarios y Container

El proyecto cuenta con una suite completa de pruebas unitarias y de integración que garantizan la calidad del código y el correcto funcionamiento de los componentes. Se utilizan las siguientes herramientas y enfoques:

- **JUnit 5**: Para la ejecución de pruebas unitarias.
- **Mockito**: Para mockear dependencias en pruebas unitarias.
- **Testcontainers**: Para pruebas de integración con bases de datos reales en contenedores Docker.
- **Spring MVC Test**: Para pruebas de controladores REST.

#### Descripción de nuestros tests
- Para mantener una estructura organizada y facilitar el mantenimiento del proyecto, hemos creado una carpeta por entidad, donde se ubican sus respectivas pruebas unitarias. Esto permite una navegación más clara del código. Los tests unitarios implementados son:

   - `CommentControllerTest`
   - `DishControllerTest`
   - `LocationControllerTest`
   - `MenuControllerTest`
   - `RestaurantControllerTest`
   - `ReviewControllerTest`
   - `UserControllerTest`

- Tambien se hicieron pruebas unitarias para Auth:

  - `AuthControllerTest`
  - `AuthServiceTest`

- Las pruebas de integración funcionan gracias al uso de Testcontainers, que permiten levantar un contenedor real de base de datos PostgreSQL durante la ejecución. Para evitar la duplicación de configuración, se ha creado la clase `TestcontainersConfigurations`, la cual es importada en cada test que requiere esta funcionalidad.

   Los tests que utilizan esta configuración son:

   - `DbpBackendApplicationTests`
   - `TestDbpBackendApplication`

   Estos se encargan de verificar que el proyecto se levanta correctamente y que la conexión con la base de datos se realiza sin errores.

- Además, se ha implementado una prueba de integración más completa para la entidad Restaurant, ya que esta posee relaciones con varias otras entidades (como User, Location, Menu, y Dish). Esta prueba permite validar que la persistencia y las asociaciones funcionan correctamente:

   -`RestaurantRepositoryTest`

Esta prueba también utiliza TestcontainersConfigurations para simular un entorno real de base de datos durante su ejecución.

### Manejo de Errores

El sistema implementa un manejo robusto de errores con respuestas HTTP claras y mensajes descriptivos. Los tests demuestran los siguientes escenarios de manejo de errores:

1. **Errores de Autenticación**:
   - `UsernameNotFoundException`: Cuando un usuario no existe
   - `UserAlreadyExistException`: En intentos de registro duplicado
   - `IllegalArgumentException`: Para credenciales inválidas

2. **Errores de Recursos**:
   - `ResourceNotFoundException`: Para recursos no encontrados (usuarios, restaurantes, etc.)
   - Validación de datos de entrada con mensajes claros

3. **Control de Acceso**:
   - Pruebas de autorización para diferentes roles (USER, OWNER, ADMIN)
   - Verificación de permisos en operaciones sensibles

4. **Validación de Datos**:
   - Pruebas para campos obligatorios
   - Validación de formatos (email, números, etc.)
   - Verificación de restricciones de tamaño/longitud


## Medidas de Seguridad Implementadas
✅ Autenticación y Autorización

    JWT (JSON Web Token): Autenticación segura basada en tokens.

    Gestión de Roles: Jerarquía de permisos → ADMIN > OWNER > USER.

    Expiración de Tokens: Válidos por 10 horas.

    Validación de Token: Verificada en cada solicitud.

🔒 Control de Acceso

    RBAC (Role-Based Access Control): Mediante anotaciones @PreAuthorize.

    Ejemplo: Solo ADMIN puede acceder a endpoints sensibles, USER accede a su propio contexto.

📋 Validación de Datos

    Se utilizan anotaciones de Jakarta Bean Validation:

        @NotNull, @NotBlank: Evitan nulos o vacíos.

        @Size: Controla la longitud de cadenas.

        @Email: Verifica formato de correos electrónicos.

        @DecimalMin: Define valores numéricos mínimos.

🔐 Cifrado

    Contraseñas: Hasheadas con Spring Security (por ejemplo, BCryptPasswordEncoder).

    JWT: Firmados con el algoritmo HMAC256.

🛡️ Prevención de Vulnerabilidades

    CSRF: Deshabilitado para APIs REST; el JWT ofrece protección.

    XSS: Validación y sanitización de entradas habilitadas.

    Inyección SQL: Evitada mediante JPA/Hibernate y consultas parametrizadas.

⚙️ Otras Medidas

    Sesiones Stateless: Sin estado del lado del servidor.

    Headers de Seguridad: Aplicados en la configuración de seguridad.

    CORS: Configuración específica para dominios permitidos. 

## Eventos y Asincronía
🛠️ Configuración Asíncrona

    Habilitada globalmente con @EnableAsync.

    Métodos anotados con @Async se ejecutan en segundo plano.

📬 Eventos de Registro

    Uso de eventos asíncronos para operaciones post-registro:

        Envío de correos de bienvenida.

        Registro de auditoría.

⚡ Beneficios del Manejo Asíncrono

    Rendimiento Mejorado:

        Operaciones largas (como emails) no bloquean el flujo principal.

        Procesamiento paralelo de tareas.

    Casos de Uso:

        Envío de correos.

        Notificaciones.

        Auditorías y logs.

    Ventajas Clave:

        Mejor experiencia de usuario.

        Optimización de recursos del servidor.

        Escalabilidad.

        Desacoplamiento de lógica.

        Tolerancia a fallos (errores en tareas asíncronas no afectan la ejecución principal).

## GitHub

📌 Uso de Issues y Branches
Durante el desarrollo del sistema, se utilizó GitHub Issues como herramienta principal para la organización y planificación de tareas. Cada funcionalidad o módulo a desarrollar fue registrado como un issue, conteniendo una breve descripción del objetivo, criterios de aceptación y cualquier consideración técnica relevante.

**A partir de cada issue creado:**

Se generó una branch específica cuyo nombre correspondía al identificador del issue (por ejemplo: feature/issue-12-crear-entidad-restaurante), facilitando la trazabilidad entre el trabajo realizado y la tarea asignada.

Una vez completada la implementación correspondiente a esa branch, se creó un pull request (PR) hacia la rama principal (main), lo que permitía la revisión del código antes de su integración definitiva al proyecto.

Este flujo ayudó a mantener una estructura de desarrollo limpia y organizada, además de fomentar buenas prácticas como la revisión cruzada de código.

🔁 Pull Requests y Revisión de Código
Cada pull request era revisado manualmente antes de su merge a main, asegurando:

- Que el código cumplía con los requisitos del issue correspondiente.

- Que no se rompía ninguna funcionalidad existente.

- Que se seguían las convenciones de codificación acordadas por el equipo.


## POSTMAN Collections
Esta colección de Postman contiene los diferentes endpoints REST de nuestra aplicación Spring Boot. Está diseñada para facilitar la prueba y validación de las funcionalidades principales, incluyendo la gestión de usuarios, operaciones CRUD, autenticación y cualquier otro servicio expuesto por el backend.

(https://lazheart.postman.co/workspace/Lazheart's-Workspace~2eca68a4-0d71-4d01-88e7-ae8ef981f62c/collection/43513911-afc0fe12-815b-4a4c-9b70-2aca6cc83ae6?action=share&creator=45430589)
