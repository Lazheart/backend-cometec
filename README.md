
# ComeTec Backend🍽️

## Descripcion
Backend para un sistema de gestión de restaurantes que permite a los clientes explorar restaurantes, dejar reseñas y comentarios, mientras que los propietarios pueden administrar sus restaurantes, cartas y platos. Incluye funcionalidades avanzadas como calificaciones, comentarios y gestión de menús.

## Development Team 👥
| Nombre Completo         | Usuario GitHub     | Correo                                                                    |
| ----------------------- | ------------------ | ------------------------------------------------------------------------- |
| Benjamin Llerena        | Lazheart           | [benjamin.llerena@utec.edu.pe](mailto:benjamin.llerena@utec.edu.pe)       |
| Luciana Yangali Cáceres | Luciana-y          | [luciana.yangali@utec.edu.pe](mailto:luciana.yangali@utec.edu.pe)         |
| Leonardo Montesinos     | LeonardoMontesinos | [leonardo.montesinos@utec.edu.pe](mailto:leonardo.montesinos@utec.edu.pe) |
| Lucia Cartagena         | luciajcm           | [lucia.cartajena@utec.edu.pe](mailto:lucia.cartajena@utec.edu.pe)         |


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
     git clone https://github.com/your-repo/restaurant-backend.git
   ```
2. **Navega al proyecto**
   Cambia al directorio del proyecto

   ```sh
    cd proyecto-backend-mure
   ```
3. Abre el Proyecto en tu IDE preferido

   Como recomendacion usa IntelliJDEA

4. **Corre la Aplicacion**

   Ejecute `ComeTecApplication` para iniciar la aplicación. IntelliJ   manejará automáticamente las dependencias como se especifica en el archivo `pom.xml` ya que el proyecto usa Maven.

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
         POSTGRES_PASSWORD: postgres
         POSTGRES_DB: database
       ports:
         - "5433:5432"
   ```

Seguir estos pasos asegura que el proyecto pueda ser lanzado de manera local.

## Diagrama Entidad Relaciom

![ER Diagram](https://media.discordapp.net/attachments/1361535013195219014/1377434326727135252/postgreslocalhost.png?ex=6838f32e&is=6837a1ae&hm=1fdf7444d006cd13cc70d59c00b8639f5d416603f01acc95250da3df2245d178&=&format=webp&quality=lossless&width=716&height=1421)


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


## Opiniones de los Dev  💥

