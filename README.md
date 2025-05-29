
# ComeTec Backend🍽️: Administración de Restaurantes y Menús

## CS20231 Desarrollo Basado en Plataformas

## Introducción


## Descripcion
Backend para un sistema de gestión de restaurantes que permite a los clientes explorar restaurantes, dejar reseñas y comentarios, mientras que los propietarios pueden administrar sus restaurantes, cartas y platos. Incluye funcionalidades avanzadas como calificaciones, comentarios y gestión de menús.

## Development Team 👥
| Nombre Completo         | Usuario GitHub     | Correo                                                                    |
| ----------------------- | ------------------ | ------------------------------------------------------------------------- |
| Benjamin Llerena        | Lazheart           | [benjamin.llerena@utec.edu.pe](mailto:benjamin.llerena@utec.edu.pe)       |
| Luciana Yangali Cáceres | Luciana-y          | [luciana.yangali@utec.edu.pe](mailto:luciana.yangali@utec.edu.pe)         |
| Leonardo Montesinos     | LeonardoMontesinos | [leonardo.montesinos@utec.edu.pe](mailto:leonardo.montesinos@utec.edu.pe) |
| Lucia Cartagena         | luciajcm           | [lucia.cartajena@utec.edu.pe](mailto:lucia.cartajena@utec.edu.pe)         |

## Descripción
Muchos restaurantes pequeños y medianos carecen de una solución digital personalizada que les permita interactuar directamente con sus clientes. Por un lado, los clientes no siempre tienen una manera clara y estructurada de calificar y comentar sus experiencias. Por otro lado, los dueños no tienen visibilidad clara de las opiniones de sus clientes ni herramientas que les permitan administrar eficientemente sus menús o disponibilidad de platos.

## Justificación
Ofrecer un backend robusto para este tipo de sistema ayuda a cubrir una necesidad real en el sector gastronómico: mejorar la experiencia del cliente y optimizar la gestión del restaurante. Al permitir que los usuarios dejen comentarios y califiquen los restaurantes, se crea un círculo virtuoso de retroalimentación que puede mejorar tanto el servicio como la reputación de los establecimientos. Además, la gestión dinámica del menú y platos facilita la adaptación rápida a la demanda y disponibilidad.

## Descripción de la Solución
- **Registro y gestión de usuarios:** Separación clara entre roles de cliente y propietario.
- **Gestión de restaurantes:** Los propietarios pueden registrar su restaurante, incluyendo ubicación y carta asociada.
- **Gestión de cartas y platos:** Permite a los propietarios crear una carta, agregar platos, y gestionar su disponibilidad.
- **Sistema de reseñas:** Los clientes pueden calificar restaurantes con estrellas (0-5), lo que influye en una media de calificaciones.
- **Comentarios en reseñas:** Los usuarios pueden comentar sobre las reseñas de otros, fomentando la interacción.

## Tecnologías Utilizadas
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

### Diagrama Entidad Relacion

![ER Diagram](https://media.discordapp.net/attachments/1361535013195219014/1377434326727135252/postgreslocalhost.png?ex=6838f32e&is=6837a1ae&hm=1fdf7444d006cd13cc70d59c00b8639f5d416603f01acc95250da3df2245d178&=&format=webp&quality=lossless&width=716&height=1421)

### Entidades

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

### Tests Unitarios

### Test Container

## Medidas de Seguridad Implementadas

## Eventos y Asincronía

## GitHub



## Conclusión

## Apéndices

## POSTMAN Collections
Esta colección de Postman contiene los diferentes endpoints REST de nuestra aplicación Spring Boot. Está diseñada para facilitar la prueba y validación de las funcionalidades principales, incluyendo la gestión de usuarios, operaciones CRUD, autenticación y cualquier otro servicio expuesto por el backend.

(https://lazheart.postman.co/workspace/Lazheart's-Workspace~2eca68a4-0d71-4d01-88e7-ae8ef981f62c/collection/43513911-afc0fe12-815b-4a4c-9b70-2aca6cc83ae6?action=share&creator=45430589)
