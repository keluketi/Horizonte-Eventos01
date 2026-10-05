# Horizonte_eventos

Aplicacion Java de escritorio para la gestion interna de Horizonte Eventos. El primer modulo en funcionamiento es el inicio de sesion administrativo y la gestion basica de clientes.

## Paquetes (carpetas de codigo)

- `modelo`: objetos del sistema, como `Evento`, `Cliente` y `Empleado`.
- `dao`: interfaces que enumeran las operaciones de base de datos.
- `dao.impl`: codigo JDBC que implementa esas operaciones.
- `config`: conexion a MySQL/MariaDB.
- `seguridad`: generacion y verificacion de hashes de contrasenas.
- `app`: ventanas de inicio de sesion y administracion.

## Preparar y abrir en Eclipse

1. Importa la carpeta del proyecto con **File > Import > Existing Projects into Workspace**.
2. Importa/ejecuta en MySQL o MariaDB el esquema `horizonte_eventos` que ya recibiste.
3. En `Conexion.java`, configura URL, usuario y contrasena de tu base.
4. Agrega MariaDB Connector/J al proyecto: clic derecho > **Build Path > Configure Build Path > Libraries > Add External JARs**. Descarga el conector para Java 8+ desde https://mariadb.com/downloads/connectors/java8-connector/ y agrega el archivo `.jar` extraido.
5. Ejecuta `database/01_roles_iniciales.sql` en phpMyAdmin para crear los roles.
6. Ejecuta `database/02_clientes_sin_password_web.sql` para permitir clientes sin clave de acceso web.
7. Crea un usuario inicial de Administracion y otro de Gerencia con contrasenas protegidas, como se explica abajo.
8. Abre `LoginFrame.java` y elige **Run As > Java Application**.

## Crear un usuario administrativo

La tabla `empleados` guarda contrasenas como hash PBKDF2. El login rechaza contrasenas antiguas en texto plano.

1. Ejecuta `GenerarHashContrasena.java` con **Run As > Java Application**. Escribe una contrasena de al menos 8 caracteres; el hash aparecerá en la consola de Eclipse.
2. Ejecuta en tu base de datos este SQL, cambiando los datos y pegando el hash:

```sql
INSERT INTO empleados (id_rol, nombre, apellido, cuil, email, password, telefono)
VALUES (1, 'Nombre', 'Apellido', '20123456789', 'admin@horizonte.com', 'PEGA_AQUI_EL_HASH', '1122334455');
```

El correo ingresado será el usuario del login. Si ya tienes empleados, cambia su campo `password` por el hash nuevo con un `UPDATE empleados ... WHERE email = ...`.

El script carga Administración (1), Coordinación (2), Personal (3) y Gerencia (4), según la definición actual de roles. Personal puede consultar sus eventos, horarios y tareas, y registrar las incidencias que se le habiliten.

El primer usuario de Gerencia debe crearse manualmente una vez (porque todavía no existe una cuenta de Gerencia que pueda usar el formulario). Genera otro hash y ejecuta el mismo `INSERT`, cambiando `id_rol` a `4` y usando otro CUIL y email. Luego Gerencia podrá crear empleados desde su ventana.

## Modulos disponibles

- **Administracion** puede abrir Clientes, Presupuestos, Eventos, Pagos, Comprobantes, Proveedores, Servicios, Espacios y Reportes. La pestaña Clientes tiene alta, consulta, edicion y baja. Las otras pestañas ya aparecen como accesos asignados y quedan señaladas como pendientes de implementar.
- **Gerencia** puede consultar la lista de empleados y crear nuevos. El formulario incluye rol, nombre, apellido, CUIL, email, telefono y contrasena; la contrasena se guarda como hash y nunca se muestra en la tabla.
- El login de escritorio está habilitado para Administración (rol 1) y Gerencia (rol 4). Coordinación y Personal todavía no tienen una pantalla de inicio implementada.

La asignación de clientes a cada administrador aún debe incorporarse al esquema: cada cliente necesita un administrador responsable. Si también se quiere limitar cuántos clientes puede tener cada administrador, habrá que definir ese máximo por empleado y validar el cupo al asignar.

## Usar Administracion

Al ingresar, la tabla de clientes permite ver, crear, editar y eliminar registros. Para crear un cliente, el formulario también solicita una contrasena, porque el esquema actual requiere ese campo para el acceso web; se guarda como hash. Si un cliente ya tiene eventos vinculados, la base puede impedir su eliminacion para proteger esas relaciones.

## DAO de eventos

`Evento` representa una fila de `eventos`. `EventoDAO` declara las acciones y `EventoDAOImpl` las ejecuta con `PreparedStatement`: insertar, buscar por ID, listar, actualizar, eliminar y listar por fecha. La entidad sigue las columnas del esquema actual; historial de cambios, horario de armado/desmontaje y asistencia real todavía requieren ampliar la base.
