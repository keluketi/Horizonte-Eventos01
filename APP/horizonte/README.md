# Horizonte Eventos — proyecto integrado

Este proyecto reúne la aplicación Java de escritorio y el sitio web de los avances del equipo.

## Aplicación de escritorio

- Administración: clientes y módulos comerciales/operativos existentes.
- Coordinación: agenda por fecha y consulta general de eventos.
- Personal: turnos propios, tareas y registro de horas/observaciones.
- Gerencia: empleados, reportes y auditoría.
- Al cerrar sesión, todos los módulos piden confirmación.

En Eclipse, importa la carpeta que contiene este archivo y `.project` como proyecto Java existente. El conector MariaDB está en `lib/mariadb-java-client.jar`. El punto de inicio es `src/com/horizonteeventos/app/LoginFrame.java`.

## Base de datos

La conexión Java carga `database.properties` desde la raíz del proyecto (o `APP/horizonte/database.properties` si ejecutas desde esa carpeta). Ese archivo local no se sube a Git. Puedes partir de `database.properties.example` y completar tus datos; también se aceptan las variables `HORIZONTE_DB_HOST`, `HORIZONTE_DB_PORT`, `HORIZONTE_DB_NAME`, `HORIZONTE_DB_USER`, `HORIZONTE_DB_PASSWORD` y `HORIZONTE_DB_URL`.

La aplicación conserva la base existente `horizonte_eventos`. El volcado completo del equipo se conserva como `database/referencia/horizonte_eventos_2026-10-19.sql`; incluye tablas, índices y datos. No lo vuelvas a importar sobre una base ya cargada porque puede duplicar registros o chocar con claves existentes.

Las migraciones pequeñas de esta carpeta agregan funciones cuando todavía faltan sus tablas:

- `database/03_eventos_y_paquetes_modulares.sql`: eventos y paquetes modulares.
- `database/04_registro_acciones.sql`: auditoría de clics de botones.
- `database/05_modulos_personal_y_solicitudes_web.sql`: asignaciones de personal y solicitudes web.
- `database/06_flujo_presupuestos_pagos.sql`: seña del 20 %, estados Aceptado/Señado/Pagado y aprobación antes de cobrar.
- `database/07_regularizar_presupuestos.sql`: reemplaza presupuestos pendientes vencidos sin pagos y crea propuestas pendientes faltantes para eventos Presupuestados.
- `database/08_respuestas_solicitudes_web.sql`: habilita respuestas visibles para los clientes en sus solicitudes web.

Importa `database/06_flujo_presupuestos_pagos.sql` desde phpMyAdmin en `horizonte_eventos` antes de iniciar esta versión. Agrega `presupuestos.monto_sena` y los estados que necesita el flujo. Si quieres actualizar presupuestos anteriores, importa después `database/07_regularizar_presupuestos.sql`: sólo elimina presupuestos pendientes vencidos de eventos Presupuestados que no tienen pagos, conserva los aceptados/rechazados y genera las propuestas faltantes con los precios actuales registrados en el evento y el paquete. Ninguno de estos archivos se ejecuta automáticamente desde la aplicación.

Antes de usar el panel de atención, ejecuta `database/05_modulos_personal_y_solicitudes_web.sql` si todavía no existe `solicitudes_web`. El escritorio agrega `respuesta_admin` si falta (requiere permiso `ALTER` en el usuario de base de datos). Si el host no permite esa operación, ejecuta una vez `database/08_respuestas_solicitudes_web.sql` desde el panel del hosting. La migración también crea `chat_mensajes` si falta.

## Sitio web

El sitio PHP está en `Web/`. Se conecta a `horizonte_eventos` y permite consultar contenido público y enviar solicitudes que quedan pendientes de revisión. Configura la conexión mediante las variables `HORIZONTE_DB_HOST`, `HORIZONTE_DB_PORT`, `HORIZONTE_DB_NAME`, `HORIZONTE_DB_USER` y `HORIZONTE_DB_PASSWORD`, indicadas en `Web/README.md`.

`Web/Horizonte_Eventos.sql` es un esquema de referencia anterior incluido por el equipo. Para trabajar con la base actual, utiliza el volcado vigente recibido y las migraciones de `database/`; no importes ese esquema anterior encima de la base existente.
