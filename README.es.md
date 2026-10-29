# Cuatro ejercicios pequeños de Spring

[English](README.md) · [Fuente y comprobaciones](docs/verification.json) · [Decisiones de diseño](DESIGN.md)

Prácticas históricas de Java revisadas como cuatro lecciones independientes: tareas REST, saludo JSON, entidad JPA y página MVC. Cada módulo tiene su wrapper Maven y contexto propios. Son ejercicios formativos; no acreditan seguridad de producción ni una actualización de Spring.

<img src="docs/images/greeting-en-desktop.png" alt="Página MVC ejecutada: nombre ficticio Ada, formulario GET y explicación de petición a plantilla" width="900" />

## Elegir una práctica

| Módulo | Función real | Pruebas locales |
| --- | --- | --- |
| [01_PrimerApiRes](01_PrimerApiRes) | CRUD de tareas H2 en `/task/`; identidad generada, validación y recursos ausentes | 14 |
| [EO1](EO1) | Saludo JSON en `/saludo?name=Ada`; contenido español y contador atómico por proceso | 5 |
| [05_PrimerProyectoSpringData](05_PrimerProyectoSpringData) | Entidad JPA `Alumno`; persistir, recargar, editar y borrar en pruebas. Sin API o repositorio implementados | 4 |
| [demo](demo) | Saludo Thymeleaf en `/`; nombre ficticio, ES/EN, salida escapada y errores asociados | 10 |

Las 33 pruebas incluyen los cuatro contextos originales. Pasan localmente en macOS con Corretto 17.0.14. No se añade CI alojada. Se mantienen Spring Boot 2.5.4/2.5.5 y sus dependencias; los POM apuntan a Java 16 o 17, pero no se ejecutó en Java 16.

## Ejecutar un módulo

Usa JDK 17 y el wrapper dentro del módulo elegido:

```sh
cd 01_PrimerApiRes
sh ./mvnw verify
java -jar target/01_PrimerApiRes-0.0.1-SNAPSHOT.jar --server.port=8080
```

Los mismos pasos sirven para `EO1` (`target/EO1-0.0.1-SNAPSHOT.jar`) y `demo` (`target/demo-0.0.1-SNAPSHOT.jar`). Por defecto el puerto es efímero: consulta el real en el arranque de Tomcat. El override del ejemplo mantiene el bind en `127.0.0.1`; elige un puerto libre. No hay reactor Maven padre.

La práctica que solo contiene el modelo se ejecuta así:

```sh
cd 05_PrimerProyectoSpringData
sh ./mvnw verify
java -jar target/05_PrimerProyectoSpringData-0.0.1-SNAPSHOT.jar --spring.main.web-application-type=none
```

Los módulos JPA usan H2 en memoria y descartan el esquema al cerrar. Las dos tareas iniciales son datos ficticios de clase. Un reinicio pierde los cambios y no abre bases de datos anteriores en fichero. Usa únicamente datos ficticios.

## Contrato HTTP de tareas

```sh
curl -i http://127.0.0.1:8080/task/
curl -i -H 'Content-Type: application/json' \
  -d '{"title":"Revisar el ejercicio","text":"Tarea ficticia"}' \
  http://127.0.0.1:8080/task/
```

Con el ID devuelto puedes usar `GET /task/{id}`, `PUT /task/{id}` y `DELETE /task/{id}`. El alta devuelve 201 y el borrado 204. Un ID ausente devuelve 404 y `{"code":"not_found"}`. JSON inválido, ID no cero en alta, título vacío o campos de más de 255 caracteres devuelven 400 y `{"code":"invalid_request"}`; no se alteran las filas con peticiones rechazadas. El texto es opcional. PUT usa la identidad de la ruta aunque el cuerpo traiga otro ID, conservando ese contrato original. Los métodos no admitidos mantienen 405 y cabecera Allow.

No incluye autenticación, paginación o garantías de concurrencia. Es una práctica CRUD pequeña, sin despliegue de producción.

## Página MVC

Al ejecutar `demo` en ese puerto, abre `http://127.0.0.1:8080/?lang=es`; `lang=en` selecciona inglés. El nombre ficticio opcional viaja en la URL GET y puede aparecer en el historial del navegador/servidor. Vacío equivale a mundo; más de 80 caracteres o controles producen 400 con borrador y error asociado al campo. Thymeleaf escapa el marcado. Restablecer y cambiar idioma funcionan sin JavaScript; el cambio conserva el nombre enviado. Fuente/CSS locales, foco visible y composición apilada en móvil.

<img src="docs/images/greeting-es-mobile.png" alt="Página española real a 360 píxeles, con campo etiquetado y acciones alcanzables" width="260" />
<img src="docs/images/greeting-es-error.png" alt="Error real del servidor, foco en campo y borrador ficticio conservado" width="260" />

Se comprobaron los cuatro paquetes Maven, 26 peticiones HTTP reales contra JAR, arranque sin web del modelo, recursos empaquetados exactos y navegador a 1366×768, 360×800 y 800×360. No se atribuye auditoría con lector de pantalla, dispositivo físico, publicación en tiendas, preparación para producción o revisión de seguridad/actualización.

## Procedencia

Se conservan documentación, créditos, historial y archivos ajenos a la revisión. No se cambia la licencia del código anterior. Outfit se distribuye bajo el [aviso OFL incluido](demo/src/main/resources/static/fonts/OFL.txt); detalles en [THIRD_PARTY.md](THIRD_PARTY.md). Las capturas muestran la app local ejecutada.

<details>
<summary>Original documentation and credits · Documentación y créditos originales</summary>

# dam-psp-workspace
Workspace dedicado a recabar apuntes para los modulos de Acceso a datos y programación de servicios y procesos. 

</details>

[Más proyectos de ingeniería móvil](https://github.com/navalcode)
