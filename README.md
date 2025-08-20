# Literatura 📚

Una aplicación de consola desarrollada en **Java 17** con **Spring Boot** que permite buscar y gestionar libros utilizando la API de [Gutendx](https://gutendx.com/). La aplicación almacena información de libros y autores en una base de datos PostgreSQL con una interfaz de menú interactiva y fácil de usar.

## ✨ Características

- 🔍 **Búsqueda de libros** por título utilizando la API de Gutendx
- 📖 **Gestión de biblioteca personal** con libros registrados
- 👥 **Catálogo de autores** con información biográfica
- 🕰️ **Filtrado de autores** por período histórico (año específico)
- 🌍 **Clasificación por idiomas** (Español, Inglés, Francés, Portugués)
- 🚫 **Prevención de duplicados** automática
- 💾 **Persistencia de datos** en PostgreSQL
- ✅ **Validación robusta** de entrada de usuario
- 🔄 **Manejo de errores** comprehensivo

## 🛠️ Tecnologías

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.3.1** - Framework de aplicación
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 15.3** - Base de datos
- **Jackson 2.16.0** - Procesamiento JSON
- **Docker Compose** - Gestión de contenedores
- **Maven** - Gestión de dependencias
- **JUnit 5** - Testing

## 📋 Requisitos Previos

- ☕ **Java 17** o superior
- 🐳 **Docker** y **Docker Compose**
- 🌐 **Conexión a internet** (para API de Gutendx)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/luis13d/literatura-alura.git
cd literatura
```

### 2. Configurar Base de Datos

Iniciar PostgreSQL usando Docker Compose:

```bash
docker-compose up -d
```

Esto creará:
- 🗄️ Base de datos PostgreSQL en puerto `5432`
- 📁 Usuario: `usuario` / Contraseña: `172922`
- 🏷️ Base de datos: `literatura-db`

### 3. Ejecutar la Aplicación

#### Windows:
```bash
mvnw.cmd spring-boot:run
```

#### Linux/macOS:
```bash
./mvnw spring-boot:run
```

### 4. Detener los Servicios

```bash
docker-compose down
```

## 📖 Uso de la Aplicación

Al ejecutar la aplicación, verás el siguiente menú interactivo:

```
¡Bienvenido a Literatura!
**************************************
Elija la opción a través de su número:
1 - Buscar por título.
2 - Listar libros registrados.
3 - Listar autores registrados.
4 - Listar autores vivos en un determinado año.
5 - Listar libros por idioma.
0 - Salir
**************************************
```

### 🔍 Funcionalidades Disponibles

1. **Buscar por título**: Busca libros en la API de Gutendx y los registra en tu biblioteca personal
2. **Listar libros**: Muestra todos los libros guardados en tu base de datos
3. **Listar autores**: Displays todos los autores registrados con sus obras
4. **Autores por año**: Encuentra autores que estaban vivos en un año específico
5. **Libros por idioma**: Filtra libros por idioma (es, en, fr, pt)

## 🧪 Testing

Ejecutar tests unitarios:

```bash
# Todos los tests
./mvnw test

# Tests específicos
./mvnw test -Dtest=PersonTest
./mvnw test -Dtest=BookServiceTest
```

## 🏗️ Arquitectura

### 📁 Estructura del Proyecto

```
src/main/java/com/library/literatura/
├── 🎯 LiteraturaApplication.java      # Clase principal
├── 📋 menu/                           # Interfaz de usuario
│   ├── Menu.java                      # Controlador del menú
│   └── MenuValidator.java             # Validación de entrada
├── 🏢 service/                        # Lógica de negocio
│   ├── BookService.java               # Gestión de libros
│   ├── PersonService.java             # Gestión de autores
│   ├── APIFetcher.java                # Cliente HTTP
│   └── DataConversor.java             # Conversión JSON
├── 📊 models/                         # Entidades de datos
│   ├── Book.java                      # Entidad libro
│   ├── Person.java                    # Entidad autor
│   └── [Data classes...]             # DTOs para API
└── 🗄️ repository/                     # Acceso a datos
    ├── BookRepository.java            # Repositorio de libros
    └── PersonRepository.java          # Repositorio de autores
```

### 🔄 Flujo de Datos

1. **Usuario** interactúa con **Menu**
2. **Menu** delega a **Services** (BookService/PersonService)
3. **Services** consultan **Repositories** (base de datos local)
4. Si no encuentra datos, **APIFetcher** consulta API externa
5. **DataConversor** procesa respuesta JSON
6. **Services** persisten nuevos datos y retornan resultado

## ⚡ Optimizaciones Implementadas

### 🎯 Separación de Responsabilidades
- **Menu**: Solo maneja interfaz de usuario
- **Services**: Contienen lógica de negocio
- **Repositories**: Acceso a datos optimizado

### 🔍 Índices de Base de Datos
- Índices en campos frecuentemente consultados
- Optimización de consultas JPQL
- Manejo eficiente de relaciones JPA

### 🛡️ Validación y Manejo de Errores
- Validación robusta de entrada de usuario
- Mensajes de error informativos
- Manejo de excepciones comprehensivo

### 🌐 Optimización de API
- Timeouts configurables
- Manejo de códigos de estado HTTP
- Headers de User-Agent apropiados

## 🤝 Contribuciones

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Soporte

Si encuentras algún problema o tienes sugerencias:

- 🐛 Reporta bugs creando un [Issue](https://github.com/luis13d/literatura-alura/issues)
- 💡 Sugiere mejoras en las [Discussions](https://github.com/luis13d/literatura-alura/discussions)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 🌟 Ejemplo de Uso

![Ejemplo de uso](img.png)

---

**¡Disfruta explorando el mundo de la literatura! 📚✨**