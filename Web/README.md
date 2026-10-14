# Horizonte Eventos — sitio para clientes

Sitio público en PHP, HTML, CSS y JavaScript. Permite explorar los tipos de eventos, espacios y servicios, y enviar una solicitud para que Horizonte Eventos la revise. Enviar una solicitud no confirma una reserva.

## Puesta en marcha

1. Crear la base de datos MySQL `horizonte_eventos` (o usar la existente) y ejecutar `solicitudes_web.sql`.
2. Configurar estas variables de entorno en el servidor PHP:
   - `HORIZONTE_DB_HOST` (por defecto `127.0.0.1`)
   - `HORIZONTE_DB_PORT` (por defecto `3306`)
   - `HORIZONTE_DB_NAME`
   - `HORIZONTE_DB_USER`
   - `HORIZONTE_DB_PASSWORD`
3. Publicar la carpeta en un servidor con PHP y la extensión `mysqli` habilitada.

Las solicitudes se guardan en `solicitudes_web` con estado inicial `Pendiente`. La revisión, aprobación y contacto posterior corresponden al equipo de Horizonte Eventos.

## Alcance

La página incluye contenido público y un formulario de solicitud. No incorpora acceso de clientes, chat, panel de administración ni confirmación automática de disponibilidad o precios. Los datos de espacios y disponibilidad deben completarse con información real de la empresa antes de publicar.
