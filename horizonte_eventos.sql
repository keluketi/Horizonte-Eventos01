-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-10-2026 a las 22:57:57
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `horizonte_eventos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignaciones_personal`
--

CREATE TABLE `asignaciones_personal` (
  `id_asignacion` int(11) NOT NULL,
  `id_empleado` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `funcion` varchar(100) NOT NULL,
  `tarea` varchar(255) NOT NULL,
  `hora_inicio_asignada` time NOT NULL,
  `hora_fin_asignada` time NOT NULL,
  `hora_inicio_real` time DEFAULT NULL,
  `hora_fin_real` time DEFAULT NULL,
  `estado` varchar(30) NOT NULL DEFAULT 'Pendiente',
  `observaciones` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni_cuit` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `apellido`, `dni_cuit`, `email`, `telefono`, `password`) VALUES
(1, 'thiago', 'lope', '49300500', 'thialope@gmail.com', '11247387878', 'pbkdf2$120000$SXFY5pFt+BgUMQFRTgHg+g==$Q1ZPPKQr6a7sbR0FpdNsFMm3WAerebZRa0UZRcAdKWc='),
(2, 'Maria Fernanda', 'Rios', '30123456', 'maria.rios@correo.com', '1140000001', NULL),
(3, 'Nicolas', 'Perez', '32123456', 'nicolas.perez@correo.com', '1140000002', NULL),
(4, 'Carolina', 'Gomez', '29123456', 'carolina.gomez@correo.com', '1140000003', NULL),
(5, 'Luis', 'Torres', '28123456', 'luis.torres@correo.com', '1140000004', NULL),
(6, 'Dante', 'Bassus', '49300508', 'dantebassus@gmail.com', '1111111000000000', NULL),
(7, 'papoi', 'minion', '59409304', 'minion@gmail.com', '11000010109', NULL),
(8, 'Rodrigo', 'peregrini', '83833883', 'peregrinirodrigo@gmail.com', '1001010105', NULL),
(9, 'jose', 'mujica', '28129822', 'jose@gmail.com', '28172812', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id_empleado` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `cuil` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `id_rol`, `nombre`, `apellido`, `cuil`, `email`, `password`, `telefono`) VALUES
(1, 1, 'Dante', 'Bassus', '20123456789', 'Admin@gmail.com', 'pbkdf2$120000$vgL/+5O4E8TntZP2umzjZQ==$28gR1WV/JjJuBl7UkeQLSOuPrN9PlSZY72I+iasSDP8=', '1122334455'),
(2, 4, 'Sofia', 'Mendez', '27999111111', 'gerencia@horizonte.com', 'pbkdf2$120000$msqhvtD57j7stC4BbiQjxA==$jPfzS943l0HJq/BwShz4lPLu9yppfE6SELCotQJ53gk=', '1100000001'),
(3, 2, 'Lucas', 'Fernandez', '20123456780', 'coordinacion@horizonte.com', 'pbkdf2$120000$qPkOjthXvMK/S1ndXBwvrw==$bewq4M4LYvGW1XjPtK18UobGKXSucMEHzArif/Nkfo0=', '1100000002'),
(4, 3, 'Ana', 'Silva', '20123456781', 'personal@horizonte.com', 'pbkdf2$120000$qQ1W9UaarhXvwwsXQckCIg==$l8cDtr9lAotqKiCv+z28tCXYXhSK4gQnBO8xgnn20Yo=', '1100000003'),
(5, 3, 'Mateo', 'Alvarez', '20123456782', 'mozo@horizonte.com', 'pbkdf2$120000$M1zM2TlTJ4NEwZ0TTeVw1g==$aOc1c41Dxg03aRHHD2RM3MysvlsJEq+ad2eMpCWM+mM=', '1100000004'),
(6, 3, 'adriana', 'lima', '20678797', 'adrianalima@gmail.com', 'pbkdf2$120000$CHjojd/ceN/2uTOevHIlqw==$TRR3+VocXbHUillGlJVpV47mDCwde0KkgXjgbon9KZw=', '100101109'),
(7, 2, 'jose', 'mujica', '2049223467', 'jose@gmail.com', 'pbkdf2$120000$ei7M9Sh1bXxAh7/IuLburg==$2c88D2AxILGIGtRiSWgQ7wC7D/UBeQCEgc0Tw3C6OGk=', '1212211212'),
(8, 3, 'gerardo', 'rodriguez', '20898999981', 'gerardo@gmail.com', 'pbkdf2$120000$SYnXis88VxoZT+rNPYLYOQ==$P6nMtkhwnHR8bVkBb3aImg5vJ2AfhY+Sk/n9kzKispo=', '100101010189'),
(9, 2, 'jose', 'pepe', '87438384756', 'jodaenmicasa@gmial.com', 'pbkdf2$120000$12HL35XU0CW5yqNkLHbzEA==$6iZJ5VBnaH3bZqfPeWIG22dJofuwpeHtLriPeGQpYhQ=', '938938922');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `espacios`
--

CREATE TABLE `espacios` (
  `id_espacio` int(11) NOT NULL,
  `nombre_espacio` varchar(100) NOT NULL,
  `capacidad_maxima` int(11) NOT NULL,
  `precio_base_alquiler` decimal(12,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `espacios`
--

INSERT INTO `espacios` (`id_espacio`, `nombre_espacio`, `capacidad_maxima`, `precio_base_alquiler`) VALUES
(1, 'Salon Horizonte', 250, 450000.00),
(2, 'Salon Jardin', 120, 300000.00),
(3, 'Espacio Terraza', 80, 220000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_del_evento`
--

CREATE TABLE `estado_del_evento` (
  `id_estado` int(11) NOT NULL,
  `nombre_estado` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estado_del_evento`
--

INSERT INTO `estado_del_evento` (`id_estado`, `nombre_estado`) VALUES
(4, 'Cancelado'),
(2, 'Confirmado'),
(1, 'Presupuestado'),
(3, 'Realizado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `eventos`
--

CREATE TABLE `eventos` (
  `id_evento` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_tipo_evento` int(11) DEFAULT NULL,
  `id_espacio` int(11) NOT NULL,
  `id_paquete` int(11) DEFAULT NULL,
  `id_estado` int(11) NOT NULL,
  `fecha_evento` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `cantidad_invitados` int(11) NOT NULL,
  `asistencia_real` int(11) DEFAULT NULL,
  `observaciones` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `eventos`
--

INSERT INTO `eventos` (`id_evento`, `id_cliente`, `id_tipo_evento`, `id_espacio`, `id_paquete`, `id_estado`, `fecha_evento`, `hora_inicio`, `hora_fin`, `cantidad_invitados`, `asistencia_real`, `observaciones`) VALUES
(1, 1, NULL, 1, 2, 3, '2026-09-19', '18:00:00', '23:30:00', 120, 114, NULL),
(2, 2, NULL, 2, 2, 2, '2026-10-24', '17:00:00', '23:00:00', 85, NULL, NULL),
(3, 3, NULL, 3, 1, 1, '2026-11-14', '18:00:00', '22:30:00', 55, NULL, NULL),
(4, 4, NULL, 1, 3, 2, '2026-12-05', '09:00:00', '17:00:00', 180, NULL, NULL),
(5, 5, NULL, 3, 1, 1, '2026-10-31', '19:00:00', '23:00:00', 60, NULL, NULL),
(6, 4, 5, 1, 2, 1, '2026-10-26', '18:00:00', '23:00:00', 50, NULL, 'esto es una prueba 19/10'),
(7, 6, 2, 2, 2, 1, '2026-10-26', '22:00:00', '23:59:00', 120, NULL, '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento_empleados`
--

CREATE TABLE `evento_empleados` (
  `id_evento` int(11) NOT NULL,
  `id_empleado` int(11) NOT NULL,
  `rol_en_evento` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `evento_empleados`
--

INSERT INTO `evento_empleados` (`id_evento`, `id_empleado`, `rol_en_evento`) VALUES
(1, 3, 'Coordinacion'),
(1, 4, 'Recepcion'),
(1, 5, 'Mozo'),
(2, 3, 'Coordinacion'),
(2, 4, 'Recepcion');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento_servicios`
--

CREATE TABLE `evento_servicios` (
  `id_evento` int(11) NOT NULL,
  `id_servicio` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1,
  `precio_acordado` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `evento_servicios`
--

INSERT INTO `evento_servicios` (`id_evento`, `id_servicio`, `cantidad`, `precio_acordado`) VALUES
(1, 1, 1, 250000.00),
(1, 2, 120, 18000.00),
(1, 4, 1, 120000.00),
(2, 1, 1, 250000.00),
(2, 3, 1, 200000.00),
(4, 2, 180, 18000.00),
(6, 1, 1, 0.00),
(6, 2, 1, 0.00),
(6, 3, 1, 0.00),
(6, 4, 1, 0.00),
(6, 5, 1, 0.00),
(6, 6, 1, 0.00),
(6, 7, 1, 0.00),
(6, 8, 1, 0.00),
(6, 9, 1, 0.00),
(6, 10, 1, 0.00),
(6, 11, 1, 0.00),
(6, 12, 1, 0.00),
(6, 13, 1, 0.00),
(6, 14, 1, 0.00),
(6, 15, 1, 0.00),
(6, 16, 1, 0.00),
(6, 17, 1, 0.00),
(7, 1, 1, 0.00),
(7, 2, 1, 0.00),
(7, 4, 1, 0.00),
(7, 5, 1, 0.00),
(7, 6, 1, 0.00),
(7, 7, 1, 0.00),
(7, 8, 1, 0.00),
(7, 9, 1, 0.00),
(7, 10, 1, 0.00),
(7, 11, 1, 40000.00),
(7, 12, 1, 0.00),
(7, 13, 1, 0.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas`
--

CREATE TABLE `facturas` (
  `id_factura` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `nro_factura` varchar(50) NOT NULL,
  `tipo_comprobante` enum('Factura A','Factura B','Factura C','Recibo X') NOT NULL,
  `monto_abonado` decimal(12,2) NOT NULL,
  `fecha_emision` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `facturas`
--

INSERT INTO `facturas` (`id_factura`, `id_evento`, `nro_factura`, `tipo_comprobante`, `monto_abonado`, `fecha_emision`) VALUES
(1, 1, 'B-2026-0001', 'Factura B', 500000.00, '2026-08-05 12:05:00'),
(2, 1, 'B-2026-0002', 'Factura B', 2700000.00, '2026-09-18 12:05:00'),
(3, 2, 'RX-2026-0003', 'Recibo X', 600000.00, '2026-09-05 12:05:00'),
(4, 4, 'RX-2026-0004', 'Recibo X', 800000.00, '2026-09-25 12:05:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_modificaciones`
--

CREATE TABLE `historial_modificaciones` (
  `id_modificacion` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `descripcion_cambio` text NOT NULL,
  `fecha_cambio` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `historial_modificaciones`
--

INSERT INTO `historial_modificaciones` (`id_modificacion`, `id_evento`, `descripcion_cambio`, `fecha_cambio`) VALUES
(1, 2, 'Cantidad de invitados actualizada de 80 a 85 por Coordinacion.', '2026-09-28 14:00:00'),
(2, 4, 'Se agrego servicio de catering para 180 personas.', '2026-09-22 09:15:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `incidencias`
--

CREATE TABLE `incidencias` (
  `id_incidencia` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `descripcion` text NOT NULL,
  `costo_estimado` decimal(12,2) DEFAULT 0.00,
  `reportado_por` varchar(100) DEFAULT NULL,
  `fecha_reporte` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `incidencias`
--

INSERT INTO `incidencias` (`id_incidencia`, `id_evento`, `descripcion`, `costo_estimado`, `reportado_por`, `fecha_reporte`) VALUES
(1, 1, 'Se reporto una copa rota en el sector de la barra.', 3500.00, 'Ana Silva', '2026-09-19 21:10:00'),
(2, 1, 'El microfono secundario tuvo una falla intermitente.', 0.00, 'Lucas Fernandez', '2026-09-19 20:30:00'),
(3, 4, 'se rompio una mesa de marmol', 0.00, 'jose pepe', '2026-10-19 16:39:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensajes`
--

CREATE TABLE `mensajes` (
  `id_mensaje` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `id_empleado` int(11) DEFAULT NULL,
  `cuerpo_mensaje` text NOT NULL,
  `fecha_envio` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mensajes`
--

INSERT INTO `mensajes` (`id_mensaje`, `id_evento`, `id_cliente`, `id_empleado`, `cuerpo_mensaje`, `fecha_envio`) VALUES
(1, 2, 2, 3, 'Confirmamos el cronograma de armado para las 15:00.', '2026-10-01 10:00:00'),
(2, 3, 3, 3, 'Enviamos la propuesta de servicios para revision.', '2026-09-16 11:30:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

CREATE TABLE `pagos` (
  `id_pago` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `fecha_pago` datetime NOT NULL DEFAULT current_timestamp(),
  `monto` decimal(12,2) NOT NULL,
  `medio_pago` enum('Efectivo','Transferencia','Tarjeta','Otro') NOT NULL DEFAULT 'Otro',
  `referencia` varchar(100) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id_pago`, `id_evento`, `fecha_pago`, `monto`, `medio_pago`, `referencia`, `observaciones`) VALUES
(1, 1, '2026-08-05 12:00:00', 500000.00, 'Transferencia', 'PAGO-2026-001', 'Sena'),
(2, 1, '2026-09-18 12:00:00', 2700000.00, 'Transferencia', 'PAGO-2026-002', 'Saldo final'),
(3, 2, '2026-09-05 12:00:00', 600000.00, 'Transferencia', 'PAGO-2026-003', 'Sena de reserva'),
(4, 4, '2026-09-25 12:00:00', 800000.00, 'Transferencia', 'PAGO-2026-004', 'Anticipo corporativo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paquetes`
--

CREATE TABLE `paquetes` (
  `id_paquete` int(11) NOT NULL,
  `nombre_paquete` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio_fijo` decimal(12,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paquetes`
--

INSERT INTO `paquetes` (`id_paquete`, `nombre_paquete`, `descripcion`, `precio_fijo`) VALUES
(1, 'Basico', 'Pensado para eventos pequeños. Incluye alquiler del salón, mesas y sillas, mantelería básica, iluminación, sonido básico y personal de coordinación.', 900000.00),
(2, 'Premium', 'Incluye todo lo del paquete Básico y suma servicio gastronómico, barra, decoración, sonido e iluminación profesional, personal adicional y coordinación integral.', 2200000.00),
(3, 'Corporativo', 'Pensado para eventos empresariales. Incluye salón, mobiliario, sonido, proyección, servicio gastronómico, personal de coordinación y asistencia técnica.', 1800000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paquete_servicios`
--

CREATE TABLE `paquete_servicios` (
  `id_paquete` int(11) NOT NULL,
  `id_servicio` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paquete_servicios`
--

INSERT INTO `paquete_servicios` (`id_paquete`, `id_servicio`, `cantidad`) VALUES
(1, 5, 1),
(1, 6, 1),
(1, 7, 1),
(1, 8, 1),
(1, 9, 1),
(1, 10, 1),
(2, 1, 1),
(2, 2, 1),
(2, 4, 1),
(2, 5, 1),
(2, 6, 1),
(2, 7, 1),
(2, 8, 1),
(2, 9, 1),
(2, 10, 1),
(2, 11, 1),
(2, 12, 1),
(2, 13, 1),
(3, 2, 1),
(3, 9, 1),
(3, 10, 1),
(3, 14, 1),
(3, 15, 1),
(3, 16, 1),
(3, 17, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `presupuestos`
--

CREATE TABLE `presupuestos` (
  `id_presupuesto` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  `fecha_emision` date NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `monto_total` decimal(12,2) NOT NULL,
  `estado_aprobacion` enum('Pendiente','Aceptado','Rechazado') NOT NULL DEFAULT 'Pendiente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `presupuestos`
--

INSERT INTO `presupuestos` (`id_presupuesto`, `id_evento`, `fecha_emision`, `fecha_vencimiento`, `monto_total`, `estado_aprobacion`) VALUES
(1, 1, '2026-08-01', '2026-08-15', 3200000.00, 'Aceptado'),
(2, 2, '2026-09-01', '2026-09-15', 2850000.00, 'Aceptado'),
(3, 3, '2026-09-15', '2026-10-15', 1450000.00, 'Pendiente'),
(4, 4, '2026-09-20', '2026-10-20', 4100000.00, 'Aceptado'),
(5, 5, '2026-09-10', '2026-09-24', 1300000.00, 'Rechazado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_acciones`
--

CREATE TABLE `registro_acciones` (
  `id_accion` int(11) NOT NULL,
  `id_empleado` int(11) DEFAULT NULL,
  `empleado_nombre` varchar(150) NOT NULL,
  `rol` varchar(80) NOT NULL,
  `pantalla` varchar(150) NOT NULL,
  `accion` varchar(250) NOT NULL,
  `fecha_hora` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `registro_acciones`
--

INSERT INTO `registro_acciones` (`id_accion`, `id_empleado`, `empleado_nombre`, `rol`, `pantalla`, `accion`, `fecha_hora`) VALUES
(1, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:17:34'),
(2, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:18:51'),
(3, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 11:19:03'),
(4, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Dashboard', '2026-10-19 11:20:01'),
(5, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:20:03'),
(6, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:06'),
(7, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:08'),
(8, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:09'),
(9, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:09'),
(10, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:10'),
(11, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:10'),
(12, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:21:10'),
(13, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 11:21:31'),
(14, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 11:21:33'),
(15, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:22:09'),
(16, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 11:26:00'),
(17, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:26:15'),
(18, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 11:32:16'),
(19, 1, 'Dante Bassus', 'Rol 1', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 11:33:15'),
(20, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 11:33:24'),
(21, 2, 'Sofia Mendez', 'Rol 4', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 13:45:47'),
(22, 2, 'Sofia Mendez', 'Rol 4', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 13:46:00'),
(23, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:03:33'),
(24, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:04:16'),
(25, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Cerrar Sesión', '2026-10-19 14:04:23'),
(26, 1, 'Dante Bassus', 'Administracion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 14:04:26'),
(27, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:04:46'),
(28, 6, 'adriana lima', 'Personal', 'Horizonte Eventos - Personal (sin asignaciones)', 'Cerrar sesión', '2026-10-19 14:04:55'),
(29, 6, 'adriana lima', 'Personal', 'Confirmar cierre de sesión', 'Cerrar sesión', '2026-10-19 14:04:57'),
(30, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:05:21'),
(31, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Cerrar Sesión', '2026-10-19 14:05:23'),
(32, 1, 'Dante Bassus', 'Administracion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 14:05:30'),
(33, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:05:44'),
(34, 6, 'adriana lima', 'Personal', 'Horizonte Eventos - Personal (sin asignaciones)', 'Cerrar sesión', '2026-10-19 14:05:47'),
(35, 6, 'adriana lima', 'Personal', 'Confirmar cierre de sesión', 'Cerrar sesión', '2026-10-19 14:07:18'),
(36, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:11:05'),
(37, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 14:11:07'),
(38, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 14:11:08'),
(39, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 14:11:11'),
(40, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 14:11:24'),
(41, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 14:11:26'),
(42, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Aceptar', '2026-10-19 14:12:14'),
(43, 2, 'Sofia Mendez', 'Gerencia', 'Gerencia', 'Aceptar', '2026-10-19 14:12:16'),
(44, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 14:12:18'),
(45, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 14:12:20'),
(46, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Aceptar', '2026-10-19 14:13:08'),
(47, 2, 'Sofia Mendez', 'Gerencia', 'Mensaje', 'Aceptar', '2026-10-19 14:13:11'),
(48, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:13:42'),
(49, 7, 'jose mujica', 'Coordinacion', 'Horizonte Eventos - Coordinacion', 'Cerrar sesión', '2026-10-19 14:18:35'),
(50, 7, 'jose mujica', 'Coordinacion', 'Confirmar cierre de sesión', 'Cerrar sesión', '2026-10-19 14:18:36'),
(51, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 14:20:18'),
(52, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Pagos', '2026-10-19 14:20:22'),
(53, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 15:06:22'),
(54, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:06:37'),
(55, 6, 'adriana lima', 'Personal', 'Horizonte Eventos - Personal (sin asignaciones)', 'Cerrar sesión', '2026-10-19 15:06:45'),
(56, 6, 'adriana lima', 'Personal', 'Confirmar cierre de sesión', 'Cerrar sesión', '2026-10-19 15:06:46'),
(57, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Cerrar Sesión', '2026-10-19 15:06:47'),
(58, 1, 'Dante Bassus', 'Administracion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 15:06:52'),
(59, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:07:09'),
(60, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 15:07:13'),
(61, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:07:47'),
(62, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 15:08:07'),
(63, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Nuevo cliente', '2026-10-19 15:08:08'),
(64, 1, 'Dante Bassus', 'Administracion', 'Nuevo cliente', 'Aceptar', '2026-10-19 15:08:31'),
(65, 1, 'Dante Bassus', 'Administracion', 'Mensaje', 'Aceptar', '2026-10-19 15:08:32'),
(66, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar auditoría', '2026-10-19 15:08:39'),
(67, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 15:10:04'),
(68, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:10:30'),
(69, 7, 'jose mujica', 'Coordinacion', 'Horizonte Eventos - Coordinacion', 'Día siguiente ▶', '2026-10-19 15:10:33'),
(70, 7, 'jose mujica', 'Coordinacion', 'Horizonte Eventos - Coordinacion', 'Cerrar sesión', '2026-10-19 15:10:50'),
(71, 7, 'jose mujica', 'Coordinacion', 'Confirmar cierre de sesión', 'Cerrar sesión', '2026-10-19 15:10:53'),
(72, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:11:14'),
(73, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Dashboard', '2026-10-19 15:11:20'),
(74, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 15:11:22'),
(75, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Presupuestos', '2026-10-19 15:11:28'),
(76, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Proveedores', '2026-10-19 15:11:34'),
(77, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Presupuestos', '2026-10-19 15:11:35'),
(78, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 15:11:37'),
(79, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 15:11:38'),
(80, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Pagos', '2026-10-19 15:11:52'),
(81, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:11:54'),
(82, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:11:59'),
(83, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Presupuestos', '2026-10-19 15:12:26'),
(84, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:12:29'),
(85, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Crear evento', '2026-10-19 15:12:31'),
(86, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Seleccionar y continuar', '2026-10-19 15:12:44'),
(87, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'MetalComboBoxButton', '2026-10-19 15:12:58'),
(88, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:15:32'),
(89, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Proveedores', '2026-10-19 15:16:03'),
(90, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 15:16:07'),
(91, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Actualizar información', '2026-10-19 15:16:13'),
(92, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:16:15'),
(93, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 15:16:16'),
(94, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Actualizar lista', '2026-10-19 15:16:18'),
(95, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Pagos', '2026-10-19 15:16:19'),
(96, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Presupuestos', '2026-10-19 15:16:20'),
(97, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 15:16:33'),
(98, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Actualizar información', '2026-10-19 15:16:35'),
(99, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:16:38'),
(100, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Crear evento', '2026-10-19 15:16:40'),
(101, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Seleccionar y continuar', '2026-10-19 15:16:42'),
(102, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Abrir calendario', '2026-10-19 15:16:47'),
(103, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:52'),
(104, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(105, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(106, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(107, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(108, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(109, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:53'),
(110, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:54'),
(111, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:54'),
(112, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:54'),
(113, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:54'),
(114, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:54'),
(115, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:55'),
(116, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(117, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(118, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(119, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(120, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(121, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(122, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:16:56'),
(123, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:57'),
(124, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:58'),
(125, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:58'),
(126, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:58'),
(127, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:58'),
(128, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:16:59'),
(129, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:17:00'),
(130, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:17:01'),
(131, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:17:01'),
(132, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:17:02'),
(133, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '›', '2026-10-19 15:17:02'),
(134, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:04'),
(135, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:04'),
(136, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:05'),
(137, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:06'),
(138, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:07'),
(139, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:08'),
(140, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:08'),
(141, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:08'),
(142, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:08'),
(143, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:08'),
(144, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:09'),
(145, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:09'),
(146, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:10'),
(147, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:10'),
(148, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:10'),
(149, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:11'),
(150, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:12'),
(151, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:12'),
(152, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '‹', '2026-10-19 15:17:13'),
(153, 1, 'Dante Bassus', 'Administracion', 'Elegir fecha del evento', '<html><center>22<br>Libre</center></html>', '2026-10-19 15:17:17'),
(154, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Guardar evento', '2026-10-19 15:17:41'),
(155, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 15:18:18'),
(156, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:18:32'),
(157, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Crear evento', '2026-10-19 15:18:33'),
(158, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Crear nuevo cliente', '2026-10-19 15:18:36'),
(159, 1, 'Dante Bassus', 'Administracion', 'Crear evento', 'Seleccionar y continuar', '2026-10-19 15:18:39'),
(160, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 15:20:40'),
(161, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:21:12'),
(162, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:48:48'),
(163, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Ingresos', '2026-10-19 15:48:56'),
(164, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 15:48:57'),
(165, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 15:49:02'),
(166, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 15:49:07'),
(167, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 15:50:18'),
(168, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:50:36'),
(169, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 15:50:38'),
(170, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 15:50:40'),
(171, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Cerrar Sesión', '2026-10-19 15:52:20'),
(172, 1, 'Dante Bassus', 'Administracion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 15:52:26'),
(173, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:52:41'),
(174, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 15:52:43'),
(175, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:53:05'),
(176, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 15:53:08'),
(177, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 15:53:09'),
(178, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 15:53:11'),
(179, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Guardar empleado', '2026-10-19 15:54:39'),
(180, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Guardar empleado', '2026-10-19 15:54:55'),
(181, 2, 'Sofia Mendez', 'Gerencia', 'Mensaje', 'Aceptar', '2026-10-19 15:54:58'),
(182, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar lista', '2026-10-19 15:54:59'),
(183, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar lista', '2026-10-19 15:55:00'),
(184, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Ingresos', '2026-10-19 15:55:13'),
(185, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar ingresos', '2026-10-19 15:55:14'),
(186, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Desempeño', '2026-10-19 15:55:16'),
(187, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Desempeño', '2026-10-19 15:56:01'),
(188, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Desempeño', '2026-10-19 15:56:03'),
(189, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Dashboard', '2026-10-19 15:56:09'),
(190, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 15:56:15'),
(191, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 15:56:47'),
(192, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:16:09'),
(193, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:16:12'),
(194, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 16:16:16'),
(195, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 16:16:19'),
(196, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 16:16:21'),
(197, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 16:16:24'),
(198, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Ingresos', '2026-10-19 16:16:25'),
(199, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Espacios', '2026-10-19 16:16:26'),
(200, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:16:27'),
(201, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 16:16:30'),
(202, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:16:31'),
(203, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 16:16:32'),
(204, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar eventos', '2026-10-19 16:16:34'),
(205, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Desempeño', '2026-10-19 16:16:50'),
(206, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Espacios', '2026-10-19 16:16:53'),
(207, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Auditoría', '2026-10-19 16:16:55'),
(208, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar auditoría', '2026-10-19 16:17:22'),
(209, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 16:17:51'),
(210, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Ingresos', '2026-10-19 16:17:55'),
(211, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Dashboard', '2026-10-19 16:18:12'),
(212, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:18:14'),
(213, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 16:18:16'),
(214, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'MetalComboBoxButton', '2026-10-19 16:20:59'),
(215, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Guardar empleado', '2026-10-19 16:21:09'),
(216, 2, 'Sofia Mendez', 'Gerencia', 'Crear empleado', 'Guardar empleado', '2026-10-19 16:21:15'),
(217, 2, 'Sofia Mendez', 'Gerencia', 'Mensaje', 'Aceptar', '2026-10-19 16:21:17'),
(218, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 16:21:26'),
(219, 2, 'Sofia Mendez', 'Gerencia', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 16:21:27'),
(220, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:21:41'),
(221, NULL, 'Sin iniciar sesión', 'No autenticado', 'No se pudo ingresar', 'Aceptar', '2026-10-19 16:21:43'),
(222, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:21:49'),
(223, NULL, 'Sin iniciar sesión', 'No autenticado', 'No se pudo ingresar', 'Aceptar', '2026-10-19 16:21:50'),
(224, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:21:57'),
(225, NULL, 'Sin iniciar sesión', 'No autenticado', 'No se pudo ingresar', 'Aceptar', '2026-10-19 16:21:59'),
(226, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:22:22'),
(227, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Calendario', '2026-10-19 16:22:28'),
(228, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'BasicArrowButton', '2026-10-19 16:22:33'),
(229, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'BasicArrowButton', '2026-10-19 16:22:34'),
(230, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'BasicArrowButton', '2026-10-19 16:22:35'),
(231, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'BasicArrowButton', '2026-10-19 16:22:35'),
(232, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'BasicArrowButton', '2026-10-19 16:22:36'),
(233, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:39'),
(234, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(235, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(236, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(237, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(238, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(239, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(240, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(241, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(242, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:40'),
(243, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(244, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(245, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(246, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(247, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(248, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(249, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(250, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(251, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:41'),
(252, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(253, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(254, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(255, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(256, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(257, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(258, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(259, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:42'),
(260, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:43'),
(261, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:43'),
(262, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:44'),
(263, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:44'),
(264, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:44'),
(265, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:44'),
(266, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:44'),
(267, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:45'),
(268, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:46'),
(269, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:46'),
(270, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:46'),
(271, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:48'),
(272, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:52'),
(273, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:52'),
(274, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:52'),
(275, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(276, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(277, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(278, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(279, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(280, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(281, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(282, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(283, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:53'),
(284, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(285, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(286, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(287, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(288, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(289, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(290, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(291, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(292, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(293, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:54'),
(294, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(295, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(296, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(297, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(298, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(299, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(300, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(301, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:55'),
(302, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(303, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(304, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(305, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(306, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(307, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(308, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:56'),
(309, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(310, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(311, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(312, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(313, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(314, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(315, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(316, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(317, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:57'),
(318, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:58'),
(319, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:58'),
(320, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:58'),
(321, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:58'),
(322, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:22:58'),
(323, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:59'),
(324, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:59'),
(325, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:59'),
(326, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:22:59'),
(327, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(328, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(329, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(330, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(331, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(332, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(333, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:00'),
(334, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(335, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(336, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(337, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(338, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(339, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:01'),
(340, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:02'),
(341, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:02'),
(342, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:02'),
(343, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(344, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(345, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(346, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(347, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(348, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(349, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(350, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:03'),
(351, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(352, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(353, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(354, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(355, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(356, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(357, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(358, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(359, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:04'),
(360, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(361, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(362, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(363, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(364, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(365, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(366, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(367, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(368, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(369, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:05'),
(370, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:06'),
(371, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:06'),
(372, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:06'),
(373, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:06'),
(374, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:06'),
(375, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:07'),
(376, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:07'),
(377, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:07'),
(378, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:07'),
(379, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:07'),
(380, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:08'),
(381, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:08'),
(382, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:08'),
(383, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:08'),
(384, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:09'),
(385, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:09'),
(386, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:09'),
(387, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:09'),
(388, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:10'),
(389, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:10'),
(390, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:10'),
(391, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:10'),
(392, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(393, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(394, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(395, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(396, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(397, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(398, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(399, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:11'),
(400, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(401, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(402, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(403, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(404, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(405, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(406, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(407, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(408, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:12'),
(409, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(410, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(411, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(412, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(413, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(414, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(415, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(416, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(417, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:13'),
(418, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(419, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(420, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(421, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(422, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(423, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(424, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:14'),
(425, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15'),
(426, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15'),
(427, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15'),
(428, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15'),
(429, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15');
INSERT INTO `registro_acciones` (`id_accion`, `id_empleado`, `empleado_nombre`, `rol`, `pantalla`, `accion`, `fecha_hora`) VALUES
(430, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:15'),
(431, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(432, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(433, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(434, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(435, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(436, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(437, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:16'),
(438, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(439, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(440, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(441, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(442, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(443, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:17'),
(444, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:18'),
(445, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:18'),
(446, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:18'),
(447, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:18'),
(448, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:18'),
(449, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:20'),
(450, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:21'),
(451, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:21'),
(452, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:23'),
(453, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:24'),
(454, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:26'),
(455, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:26'),
(456, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:27'),
(457, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:27'),
(458, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:27'),
(459, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:27'),
(460, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:28'),
(461, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:28'),
(462, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:30'),
(463, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:30'),
(464, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:31'),
(465, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Hoy', '2026-10-19 16:23:37'),
(466, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:38'),
(467, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:40'),
(468, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:40'),
(469, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:40'),
(470, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:40'),
(471, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(472, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(473, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(474, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(475, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(476, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día siguiente', '2026-10-19 16:23:41'),
(477, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:42'),
(478, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:42'),
(479, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:44'),
(480, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Día anterior', '2026-10-19 16:23:44'),
(481, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Cerrar sesión', '2026-10-19 16:32:33'),
(482, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Dashboard', '2026-10-19 16:34:29'),
(483, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Calendario', '2026-10-19 16:34:39'),
(484, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Eventos confirmados', '2026-10-19 16:34:40'),
(485, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Eventos confirmados', '2026-10-19 16:34:42'),
(486, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Calendario', '2026-10-19 16:34:43'),
(487, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Dashboard', '2026-10-19 16:34:44'),
(488, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Dashboard', '2026-10-19 16:34:58'),
(489, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Calendario', '2026-10-19 16:35:00'),
(490, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Eventos confirmados', '2026-10-19 16:35:08'),
(491, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Actualizar eventos', '2026-10-19 16:35:21'),
(492, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Recursos', '2026-10-19 16:35:29'),
(493, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Eventos confirmados', '2026-10-19 16:35:39'),
(494, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Recursos', '2026-10-19 16:35:47'),
(495, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Modificaciones', '2026-10-19 16:38:29'),
(496, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Incidencias', '2026-10-19 16:38:32'),
(497, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Registrar incidencia', '2026-10-19 16:38:34'),
(498, 9, 'jose pepe', 'Coordinacion', 'Registrar incidencia', 'Aceptar', '2026-10-19 16:39:23'),
(499, 9, 'jose pepe', 'Coordinacion', 'Mensaje', 'Aceptar', '2026-10-19 16:39:25'),
(500, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Registrar incidencia', '2026-10-19 16:40:04'),
(501, 9, 'jose pepe', 'Coordinacion', 'Horizonte Eventos - Panel de Coordinación', 'Cerrar sesión', '2026-10-19 16:41:10'),
(502, 9, 'jose pepe', 'Coordinacion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 16:41:12'),
(503, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:41:23'),
(504, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 16:41:41'),
(505, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Nuevo cliente', '2026-10-19 16:41:43'),
(506, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 16:43:13'),
(507, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Cerrar Sesión', '2026-10-19 16:45:22'),
(508, 1, 'Dante Bassus', 'Administracion', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 16:45:23'),
(509, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:45:37'),
(510, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 16:45:45'),
(511, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Actualizar eventos', '2026-10-19 16:45:48'),
(512, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Ingresos', '2026-10-19 16:45:49'),
(513, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:45:49'),
(514, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Crear empleado', '2026-10-19 16:45:50'),
(515, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:55:02'),
(516, NULL, 'Sin iniciar sesión', 'No autenticado', 'No se pudo ingresar', 'Aceptar', '2026-10-19 16:55:04'),
(517, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:55:13'),
(518, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Eventos', '2026-10-19 16:55:18'),
(519, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Empleados', '2026-10-19 16:55:25'),
(520, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'MetalComboBoxButton', '2026-10-19 16:56:10'),
(521, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'MetalComboBoxButton', '2026-10-19 16:56:11'),
(522, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'MetalComboBoxButton', '2026-10-19 16:56:11'),
(523, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'MetalComboBoxButton', '2026-10-19 16:56:12'),
(524, 2, 'Sofia Mendez', 'Gerencia', 'Horizonte Eventos - Panel de Gerencia', 'Cerrar Sesión', '2026-10-19 16:56:30'),
(525, 2, 'Sofia Mendez', 'Gerencia', 'Confirmar cierre de sesión', 'Sí', '2026-10-19 16:56:31'),
(526, NULL, 'Sin iniciar sesión', 'No autenticado', 'Horizonte Eventos - Iniciar sesión', 'INICIAR SESIÓN', '2026-10-19 16:56:49'),
(527, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 16:56:53'),
(528, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Presupuestos', '2026-10-19 16:56:54'),
(529, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 16:56:56'),
(530, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 16:56:57'),
(531, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Pagos', '2026-10-19 16:56:59'),
(532, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Comprobantes', '2026-10-19 16:57:01'),
(533, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Proveedores', '2026-10-19 16:57:03'),
(534, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 16:57:04'),
(535, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Dashboard', '2026-10-19 17:49:01'),
(536, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Clientes', '2026-10-19 17:49:02'),
(537, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Editar seleccionado', '2026-10-19 17:49:10'),
(538, 1, 'Dante Bassus', 'Administracion', 'Editar cliente', 'Cancelar', '2026-10-19 17:49:12'),
(539, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Paquetes', '2026-10-19 17:49:14'),
(540, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Eventos', '2026-10-19 17:49:14'),
(541, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Pagos', '2026-10-19 17:49:15'),
(542, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Comprobantes', '2026-10-19 17:49:17'),
(543, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Proveedores', '2026-10-19 17:49:19'),
(544, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 17:49:20'),
(545, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Espacios', '2026-10-19 17:49:22'),
(546, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Actualizar información', '2026-10-19 17:49:24'),
(547, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Reportes', '2026-10-19 17:49:27'),
(548, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Reportes', '2026-10-19 17:49:41'),
(549, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Espacios', '2026-10-19 17:49:43'),
(550, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Servicios', '2026-10-19 17:49:45'),
(551, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Espacios', '2026-10-19 17:49:46'),
(552, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Reportes', '2026-10-19 17:49:48'),
(553, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Reportes', '2026-10-19 17:49:52'),
(554, 1, 'Dante Bassus', 'Administracion', 'Horizonte Eventos - Panel de Administración', 'Reportes', '2026-10-19 17:50:06');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL,
  `nombre_rol` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre_rol`) VALUES
(1, 'Administracion'),
(2, 'Coordinacion'),
(5, 'Encargado de Personal'),
(4, 'Gerencia'),
(3, 'Personal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicios`
--

CREATE TABLE `servicios` (
  `id_servicio` int(11) NOT NULL,
  `id_tercero` int(11) DEFAULT NULL,
  `nombre_servicio` varchar(100) NOT NULL,
  `costo_unitario` decimal(12,2) NOT NULL DEFAULT 0.00,
  `cantidad_disponible` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicios`
--

INSERT INTO `servicios` (`id_servicio`, `id_tercero`, `nombre_servicio`, `costo_unitario`, `cantidad_disponible`) VALUES
(1, 1, 'DJ y sonido profesional', 250000.00, NULL),
(2, 2, 'Catering por persona', 18000.00, NULL),
(3, 3, 'Fotografia del evento', 200000.00, NULL),
(4, NULL, 'Coordinacion integral', 120000.00, NULL),
(5, NULL, 'Mesas y sillas', 490.00, 300),
(6, NULL, 'Mantelería básica', 7000.00, NULL),
(7, NULL, 'Iluminación', 90909.00, NULL),
(8, NULL, 'Sonido básico', 12000.00, 4),
(9, NULL, 'Personal de coordinación', 0.00, NULL),
(10, NULL, 'Alquiler del salón', 10000.00, NULL),
(11, NULL, 'Barra', 20000.00, NULL),
(12, NULL, 'Decoración', 2000430.00, NULL),
(13, NULL, 'Personal adicional', 0.00, NULL),
(14, NULL, 'Mobiliario', 699.00, NULL),
(15, NULL, 'Sonido', 14000.00, NULL),
(16, NULL, 'Proyección', 17000.00, 6),
(17, NULL, 'Asistencia técnica', 100500.00, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitudes_web`
--

CREATE TABLE `solicitudes_web` (
  `id_solicitud` bigint(20) UNSIGNED NOT NULL,
  `tipo_evento` varchar(80) NOT NULL,
  `fecha_tentativa` date DEFAULT NULL,
  `horario` varchar(20) DEFAULT NULL,
  `invitados` int(10) UNSIGNED NOT NULL,
  `espacio_preferido` varchar(120) DEFAULT NULL,
  `servicios` text NOT NULL,
  `comentarios` varchar(1500) DEFAULT NULL,
  `nombre` varchar(80) NOT NULL,
  `apellido` varchar(80) NOT NULL,
  `email` varchar(160) NOT NULL,
  `telefono` varchar(40) DEFAULT NULL,
  `estado` enum('Pendiente','Contactado','Aprobado','Rechazado') NOT NULL DEFAULT 'Pendiente',
  `fecha_creacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `terceros`
--

CREATE TABLE `terceros` (
  `id_tercero` int(11) NOT NULL,
  `razon_social` varchar(150) NOT NULL,
  `contacto_nombre` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `rubro` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `terceros`
--

INSERT INTO `terceros` (`id_tercero`, `razon_social`, `contacto_nombre`, `telefono`, `rubro`) VALUES
(1, 'Sonido Sur', 'Diego Ruiz', '1140000101', 'DJ y sonido'),
(2, 'Gastronomia del Parque', 'Laura Diaz', '1140000102', 'Catering'),
(3, 'Foto Luz', 'Pablo Vera', '1140000103', 'Fotografia');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_evento`
--

CREATE TABLE `tipos_evento` (
  `id_tipo_evento` int(11) NOT NULL,
  `nombre` varchar(80) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipos_evento`
--

INSERT INTO `tipos_evento` (`id_tipo_evento`, `nombre`, `activo`) VALUES
(1, 'Casamientos', 1),
(2, 'Cumpleaños de 15', 1),
(3, 'Cumpleaños infantiles', 1),
(4, 'Eventos corporativos', 1),
(5, 'Aniversarios', 1),
(6, 'Bautismos', 1),
(7, 'Reuniones sociales', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asignaciones_personal`
--
ALTER TABLE `asignaciones_personal`
  ADD PRIMARY KEY (`id_asignacion`),
  ADD UNIQUE KEY `uq_asignacion_empleado_evento_tarea` (`id_empleado`,`id_evento`,`tarea`),
  ADD KEY `ix_asignaciones_personal_empleado` (`id_empleado`),
  ADD KEY `ix_asignaciones_personal_evento` (`id_evento`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `uk_dni_cuit_cliente` (`dni_cuit`),
  ADD UNIQUE KEY `uk_email_cliente` (`email`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id_empleado`),
  ADD UNIQUE KEY `uk_cuil_empleado` (`cuil`),
  ADD UNIQUE KEY `uk_email_empleado` (`email`),
  ADD KEY `fk_empleado_rol` (`id_rol`);

--
-- Indices de la tabla `espacios`
--
ALTER TABLE `espacios`
  ADD PRIMARY KEY (`id_espacio`);

--
-- Indices de la tabla `estado_del_evento`
--
ALTER TABLE `estado_del_evento`
  ADD PRIMARY KEY (`id_estado`),
  ADD UNIQUE KEY `uk_nombre_estado` (`nombre_estado`);

--
-- Indices de la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD PRIMARY KEY (`id_evento`),
  ADD KEY `fk_evento_cliente` (`id_cliente`),
  ADD KEY `fk_evento_espacio` (`id_espacio`),
  ADD KEY `fk_evento_paquete` (`id_paquete`),
  ADD KEY `fk_evento_estado` (`id_estado`),
  ADD KEY `fk_eventos_tipo_evento` (`id_tipo_evento`);

--
-- Indices de la tabla `evento_empleados`
--
ALTER TABLE `evento_empleados`
  ADD PRIMARY KEY (`id_evento`,`id_empleado`),
  ADD KEY `fk_evem_empleado` (`id_empleado`);

--
-- Indices de la tabla `evento_servicios`
--
ALTER TABLE `evento_servicios`
  ADD PRIMARY KEY (`id_evento`,`id_servicio`),
  ADD KEY `fk_evser_servicio` (`id_servicio`);

--
-- Indices de la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`id_factura`),
  ADD UNIQUE KEY `uk_nro_factura` (`nro_factura`),
  ADD KEY `fk_factura_evento` (`id_evento`);

--
-- Indices de la tabla `historial_modificaciones`
--
ALTER TABLE `historial_modificaciones`
  ADD PRIMARY KEY (`id_modificacion`),
  ADD KEY `fk_historial_evento` (`id_evento`);

--
-- Indices de la tabla `incidencias`
--
ALTER TABLE `incidencias`
  ADD PRIMARY KEY (`id_incidencia`),
  ADD KEY `fk_incidencia_evento` (`id_evento`);

--
-- Indices de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD PRIMARY KEY (`id_mensaje`),
  ADD KEY `fk_mensaje_evento` (`id_evento`),
  ADD KEY `fk_mensaje_cliente` (`id_cliente`),
  ADD KEY `fk_mensaje_empleado` (`id_empleado`);

--
-- Indices de la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD PRIMARY KEY (`id_pago`),
  ADD UNIQUE KEY `uk_pago_referencia` (`referencia`),
  ADD KEY `fk_pago_evento` (`id_evento`);

--
-- Indices de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  ADD PRIMARY KEY (`id_paquete`);

--
-- Indices de la tabla `paquete_servicios`
--
ALTER TABLE `paquete_servicios`
  ADD PRIMARY KEY (`id_paquete`,`id_servicio`),
  ADD KEY `fk_paquete_servicios_servicio` (`id_servicio`);

--
-- Indices de la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  ADD PRIMARY KEY (`id_presupuesto`),
  ADD KEY `fk_presupuesto_evento` (`id_evento`);

--
-- Indices de la tabla `registro_acciones`
--
ALTER TABLE `registro_acciones`
  ADD PRIMARY KEY (`id_accion`),
  ADD KEY `idx_registro_acciones_fecha` (`fecha_hora`),
  ADD KEY `idx_registro_acciones_empleado` (`id_empleado`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `uk_nombre_rol` (`nombre_rol`);

--
-- Indices de la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD PRIMARY KEY (`id_servicio`),
  ADD KEY `fk_servicio_tercero` (`id_tercero`);

--
-- Indices de la tabla `solicitudes_web`
--
ALTER TABLE `solicitudes_web`
  ADD PRIMARY KEY (`id_solicitud`),
  ADD KEY `idx_solicitudes_email` (`email`),
  ADD KEY `idx_solicitudes_fecha` (`fecha_creacion`),
  ADD KEY `idx_solicitudes_estado` (`estado`);

--
-- Indices de la tabla `terceros`
--
ALTER TABLE `terceros`
  ADD PRIMARY KEY (`id_tercero`);

--
-- Indices de la tabla `tipos_evento`
--
ALTER TABLE `tipos_evento`
  ADD PRIMARY KEY (`id_tipo_evento`),
  ADD UNIQUE KEY `uq_tipos_evento_nombre` (`nombre`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asignaciones_personal`
--
ALTER TABLE `asignaciones_personal`
  MODIFY `id_asignacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `espacios`
--
ALTER TABLE `espacios`
  MODIFY `id_espacio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `estado_del_evento`
--
ALTER TABLE `estado_del_evento`
  MODIFY `id_estado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `id_factura` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `historial_modificaciones`
--
ALTER TABLE `historial_modificaciones`
  MODIFY `id_modificacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `incidencias`
--
ALTER TABLE `incidencias`
  MODIFY `id_incidencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  MODIFY `id_mensaje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pagos`
--
ALTER TABLE `pagos`
  MODIFY `id_pago` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  MODIFY `id_paquete` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  MODIFY `id_presupuesto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `registro_acciones`
--
ALTER TABLE `registro_acciones`
  MODIFY `id_accion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=555;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `servicios`
--
ALTER TABLE `servicios`
  MODIFY `id_servicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `solicitudes_web`
--
ALTER TABLE `solicitudes_web`
  MODIFY `id_solicitud` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `terceros`
--
ALTER TABLE `terceros`
  MODIFY `id_tercero` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipos_evento`
--
ALTER TABLE `tipos_evento`
  MODIFY `id_tipo_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asignaciones_personal`
--
ALTER TABLE `asignaciones_personal`
  ADD CONSTRAINT `fk_asignaciones_personal_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`),
  ADD CONSTRAINT `fk_asignaciones_personal_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`);

--
-- Filtros para la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD CONSTRAINT `fk_empleado_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD CONSTRAINT `fk_evento_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_evento_espacio` FOREIGN KEY (`id_espacio`) REFERENCES `espacios` (`id_espacio`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_evento_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado_del_evento` (`id_estado`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_evento_paquete` FOREIGN KEY (`id_paquete`) REFERENCES `paquetes` (`id_paquete`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_eventos_tipo_evento` FOREIGN KEY (`id_tipo_evento`) REFERENCES `tipos_evento` (`id_tipo_evento`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `evento_empleados`
--
ALTER TABLE `evento_empleados`
  ADD CONSTRAINT `fk_evem_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_evem_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `evento_servicios`
--
ALTER TABLE `evento_servicios`
  ADD CONSTRAINT `fk_evser_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_evser_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD CONSTRAINT `fk_factura_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `historial_modificaciones`
--
ALTER TABLE `historial_modificaciones`
  ADD CONSTRAINT `fk_historial_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `incidencias`
--
ALTER TABLE `incidencias`
  ADD CONSTRAINT `fk_incidencia_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD CONSTRAINT `fk_mensaje_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_mensaje_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_mensaje_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD CONSTRAINT `fk_pago_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `paquete_servicios`
--
ALTER TABLE `paquete_servicios`
  ADD CONSTRAINT `fk_paquete_servicios_paquete` FOREIGN KEY (`id_paquete`) REFERENCES `paquetes` (`id_paquete`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_paquete_servicios_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  ADD CONSTRAINT `fk_presupuesto_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `registro_acciones`
--
ALTER TABLE `registro_acciones`
  ADD CONSTRAINT `fk_registro_acciones_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD CONSTRAINT `fk_servicio_tercero` FOREIGN KEY (`id_tercero`) REFERENCES `terceros` (`id_tercero`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
