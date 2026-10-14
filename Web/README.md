# Horizonte Eventos — sitio para clientes

Sitio público en PHP, HTML, CSS y JavaScript. Permite explorar los tipos de eventos, espacios y servicios, y enviar una solicitud para que Horizonte Eventos la revise. Enviar una solicitud no confirma una reserva.

## Puesta en marcha

1. Usar la base existente `horizonte_eventos` o importar el esquema SQL que acompaña al proyecto. Si el esquema no incluye `solicitudes_web`, ejecutar el archivo `solicitudes_web.sql` disponible en el proyecto.
2. Configurar estas variables de entorno en el servidor PHP:
   - `HORIZONTE_DB_HOST` (por defecto `127.0.0.1`)
   - `HORIZONTE_DB_PORT` (por defecto `3306`)
   - `HORIZONTE_DB_NAME`
   - `HORIZONTE_DB_USER`
   - `HORIZONTE_DB_PASSWORD`
3. Publicar la carpeta en un servidor con PHP y la extensión `mysqli` habilitada.

Las solicitudes se guardan en `solicitudes_web` con estado inicial `Pendiente`. La revisión, aprobación y contacto posterior corresponden al equipo de Horizonte Eventos.

## Acceso y organización

- El login está en `login.php`; clientes ingresan con su correo y trabajadores con su correo o CUIL.
- El registro de `registro.php` crea solamente clientes. Gerencia crea las cuentas de empleados en la aplicación de escritorio.
- Los empleados ven `panel-trabajador.php`, que adapta los módulos iniciales al rol almacenado en `roles`. Los módulos son una estructura de interfaz y todavía no ejecutan operaciones internas.
- `app/` contiene la aplicación MVC en POO: `Controllers`, `Models`, `Repositories`, `Services` y `Core`. Las páginas PHP de la raíz son rutas de entrada; `views/` contiene las plantillas de inicio, administración y cuentas.
- El inicio, las solicitudes, el chat y las acciones de administración delegan su lógica en controladores y repositorios de `app/`.
- Las claves se guardan en formato PBKDF2-SHA256 compatible con `PasswordUtil` de la aplicación Java. No se convierten a un formato que el escritorio no pueda validar.

La conexión usa por defecto `127.0.0.1:3306`, la base `horizonte_eventos`, usuario `root` y contraseña vacía para XAMPP. En otros entornos, configurá las variables `HORIZONTE_DB_HOST`, `HORIZONTE_DB_PORT`, `HORIZONTE_DB_NAME`, `HORIZONTE_DB_USER` y `HORIZONTE_DB_PASSWORD`.

## Alcance

La página incluye contenido público y un formulario de solicitud, junto con las primeras pantallas de acceso de clientes y trabajadores. La cuenta web de cliente y el panel de trabajador todavía no muestran datos vivos de presupuestos, eventos, pagos ni permisos. La aplicación de escritorio continúa siendo la herramienta de gestión interna.
