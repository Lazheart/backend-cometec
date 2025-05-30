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
- **User:**
- **Restaurant:**
- **Menu:**
- **Dish:**
- **Location:**
- **Review:**
- **Comment:**

## Endpoints 🛣️
📍 Location

| Método | EndPoint                      | Descripción                          |
| ------ | ----------------------------- | ------------------------------------ |
| GET    | `/restaurants/{id}/locations` | Listar ubicaciones de un restaurante |
| POST   | `/restaurants/{id}/locations` | Añadir ubicación                     |
| PUT    | `/locations/{locationId}`     | Editar ubicación                     |
| DELETE | `/locations/{locationId}`     | Eliminar ubicación                   |

👤 Usuario

| Método | EndPoint               | Descripción                     |
|--------|------------------------|---------------------------------|
| GET    | `/users/me`            | Información personal            |
| PATCH  | `/users/me`            | Actualizar información personal |
| GET    | `/users/{id}`          | Información del usuario por ID  |
| GET    | `/users/{id}/reviews`  | Reseñas por cliente             |
| GET    | `/users/{id}/comments` | Comentarios dejados por cliente |

🍽️ Restaurante

| Método | EndPoint                     | Descripción                                    |
| ------ | ---------------------------- |------------------------------------------------|
| GET    | `/restaurants`               | Listar todos los restaurantes                  |
| GET    | `/restaurants/{id}`          | Obtener detalles de un restaurante             |
| GET    | `/restaurants/{id}/reviews`  | Obtener reseñas del restaurante                |
| GET    | `/restaurants/{id}/comments` | Obtener todos los comentarios del restaurante  |
| GET    | `/restaurants/{id}/menu`     | Obtener carta del restaurante                  |
| POST   | `/restaurants`               | Actualizar datos del restaurante (propietario) |
| PUT    | `/restaurants/{id}`          | Crear restaurante (solo para propietarios)     |
| DELETE | `/restaurants/{id}`          | Eliminar restaurante                           |

📝 Review (Reseñas)

| Método | EndPoint                              | Descripción                                 |
| ------ | ------------------------------------- | ------------------------------------------- |
| POST   | `/restaurants/{restaurantId}/reviews` | Cliente deja reseña                         |
| GET    | `/reviews/{id}`                       | Obtener una reseña                          |
| PUT    | `/reviews/{id}`                       | Editar reseña (cliente)                     |
| DELETE | `/restaurants/{id}/reviews`           | Eliminar reseña (cliente)                   |
| GET    | `/restaurants/{id}/average-rating`    | Obtener promedio de reseñas del restaurante |

💬 Comment (Comentarios)

| Método | EndPoint                       | Descripción                      |
| ------ | ------------------------------ | -------------------------------- |
| POST   | `/reviews/{reviewId}/comments` | Agregar comentario a reseña      |
| GET    | `/reviews/{reviewId}/comments` | Listar comentarios de una reseña |
| PUT    | `/comments/{commentId}`        | Editar comentario                |
| DELETE | `/comments/{commentId}`        | Eliminar comentario              |

📋 Menu (Carta)

| Método | EndPoint                           | Descripción                  |
| ------ | ---------------------------------- | ---------------------------- |
| POST   | `/restaurants/{restaurantId}/menu` | Crear carta para restaurante |
| GET    | `/menus/{id}`                      | Ver carta específica         |
| PUT    | `/menus/{id}`                      | Actualizar carta             |
| DELETE | `/menus/{id}`                      | Eliminar carta               |

🍛 Dish (Plato)

| Método | EndPoint                 | Descripción                |
| ------ | ------------------------ | -------------------------- |
| POST   | `/menus/{menuId}/dishes` | Agregar plato a la carta   |
| GET    | `/menus/{menuId}/dishes` | Listar platos de una carta |
| PUT    | `/dishes/{id}`           | Actualizar plato           |
| DELETE | `/dishes/{id}`           | Eliminar plato             |
| GET    | `/dishes/{id}`           | Detalles del plato         |

## Testing y Manejo de Errores

### Test Unitarios y Container

### Manejo de Errores

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

## Conclusion

## POSTMAN Collections
Esta colección de Postman contiene los diferentes endpoints REST de nuestra aplicación Spring Boot. Está diseñada para facilitar la prueba y validación de las funcionalidades principales, incluyendo la gestión de usuarios, operaciones CRUD, autenticación y cualquier otro servicio expuesto por el backend.

(https://lazheart.postman.co/workspace/Lazheart's-Workspace~2eca68a4-0d71-4d01-88e7-ae8ef981f62c/collection/43513911-afc0fe12-815b-4a4c-9b70-2aca6cc83ae6?action=share&creator=45430589)
