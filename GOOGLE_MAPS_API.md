# Google Maps API - Backend Implementation

## Descripción
Este documento describe la implementación del servicio de Google Maps en el backend de Spring Boot, que permite obtener ubicaciones de restaurantes para mostrarlas en un mapa interactivo.

## Endpoints Disponibles

### 1. Obtener todas las ubicaciones
```
GET /api/maps/locations
```
**Descripción:** Obtiene todas las ubicaciones disponibles para mostrar en Google Maps.

**Respuesta:**
```json
[
  {
    "id": 1,
    "latitud": 40.4168,
    "longitud": -3.7038,
    "restaurantName": "Restaurante Ejemplo",
    "restaurantAddress": "Calle Ejemplo 123",
    "restaurantCategory": "ITALIAN",
    "averageRating": 4.5,
    "imageUrl": "https://example.com/image.jpg"
  }
]
```

### 2. Obtener configuración de Google Maps
```
GET /api/maps/config
```
**Descripción:** Obtiene la configuración necesaria para inicializar Google Maps en el frontend.

**Respuesta:**
```json
{
  "enabled": true,
  "defaultZoom": 12,
  "defaultLat": 40.4168,
  "defaultLng": -3.7038,
  "apiKeyConfigured": true
}
```

### 3. Buscar ubicaciones por área geográfica
```
POST /api/maps/locations/area
```
**Body:**
```json
{
  "minLatitud": 40.4000,
  "maxLatitud": 40.4300,
  "minLongitud": -3.7200,
  "maxLongitud": -3.6900,
  "page": 0,
  "size": 20
}
```

**Respuesta:**
```json
{
  "locations": [...],
  "currentPage": 0,
  "totalPages": 5,
  "totalElements": 100,
  "hasNext": true,
  "hasPrevious": false
}
```

### 4. Buscar ubicaciones por categoría
```
GET /api/maps/locations/category/{category}
```
**Ejemplo:** `/api/maps/locations/category/ITALIAN`

### 5. Búsqueda con filtros combinados
```
POST /api/maps/locations/filter
```
**Body:**
```json
{
  "minLatitud": 40.4000,
  "maxLatitud": 40.4300,
  "minLongitud": -3.7200,
  "maxLongitud": -3.6900,
  "category": "ITALIAN",
  "minRating": 4.0,
  "page": 0,
  "size": 20
}
```

### 6. Búsqueda con parámetros de query
```
GET /api/maps/locations/search?minLat=40.4000&maxLat=40.4300&category=ITALIAN&minRating=4.0&page=0&size=20
```

## Configuración

### Variables de entorno
Agregar en `application.properties`:

```properties
# Google Maps Configuration
google.maps.api.key=TU_API_KEY_AQUI
google.maps.enabled=true
google.maps.default.zoom=12
google.maps.default.lat=40.4168
google.maps.default.lng=-3.7038
```

### Seguridad
- La API key de Google Maps NO se expone en los endpoints por seguridad
- Todos los endpoints requieren autenticación con roles USER, OWNER o ADMIN
- Se recomienda configurar CORS apropiadamente para el frontend

## Uso en el Frontend

### Ejemplo básico con JavaScript
```javascript
// Obtener configuración
fetch('/api/maps/config')
  .then(res => res.json())
  .then(config => {
    // Inicializar Google Maps con la configuración
    const map = new google.maps.Map(document.getElementById('map'), {
      center: { lat: config.defaultLat, lng: config.defaultLng },
      zoom: config.defaultZoom
    });
  });

// Obtener ubicaciones
fetch('/api/maps/locations')
  .then(res => res.json())
  .then(locations => {
    locations.forEach(location => {
      new google.maps.Marker({
        position: { lat: location.latitud, lng: location.longitud },
        map: map,
        title: location.restaurantName,
        // Agregar info window con detalles del restaurante
        infoWindow: new google.maps.InfoWindow({
          content: `
            <div>
              <h3>${location.restaurantName}</h3>
              <p>${location.restaurantAddress}</p>
              <p>Categoría: ${location.restaurantCategory}</p>
              <p>Rating: ${location.averageRating} ⭐</p>
            </div>
          `
        })
      });
    });
  });
```

### Ejemplo con búsqueda por área
```javascript
// Cuando el usuario mueve el mapa o cambia el zoom
map.addListener('bounds_changed', () => {
  const bounds = map.getBounds();
  const searchParams = {
    minLatitud: bounds.getSouthWest().lat(),
    maxLatitud: bounds.getNorthEast().lat(),
    minLongitud: bounds.getSouthWest().lng(),
    maxLongitud: bounds.getNorthEast().lng(),
    page: 0,
    size: 50
  };

  fetch('/api/maps/locations/area', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(searchParams)
  })
  .then(res => res.json())
  .then(response => {
    // Limpiar marcadores existentes
    clearMarkers();
    
    // Agregar nuevos marcadores
    response.locations.forEach(location => {
      addMarker(location);
    });
  });
});
```

## Consideraciones de Rendimiento

1. **Paginación:** Los endpoints soportan paginación para manejar grandes cantidades de ubicaciones
2. **Filtrado por área:** Solo se cargan las ubicaciones visibles en el mapa actual
3. **Caché:** Considerar implementar caché Redis para ubicaciones frecuentemente consultadas
4. **Índices de base de datos:** Asegurar que existan índices en las columnas latitud y longitud

## Estructura de Archivos

```
src/main/java/com/demo/DBPBackend/
├── location/
│   ├── apllication/
│   │   └── GoogleMapsController.java
│   ├── domain/
│   │   └── GoogleMapsLocationService.java
│   ├── dto/
│   │   ├── GoogleMapsLocationDto.java
│   │   ├── LocationSearchDto.java
│   │   └── LocationPageResponseDto.java
│   └── infrastructure/
│       └── LocationRepository.java (actualizado)
└── conf/
    └── GoogleMapsConfig.java
```

## Próximos Pasos

1. Implementar caché Redis para mejorar el rendimiento
2. Agregar filtros adicionales (horarios, precios, etc.)
3. Implementar geocodificación inversa para obtener direcciones
4. Agregar métricas y monitoreo de uso de la API
5. Implementar rate limiting para proteger la API 