-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-10-2026 a las 23:30:05
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
(5, 'Luis', 'Torres', '28123456', 'luis.torres@correo.com', '1140000004', NULL);

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
(1, 1, 'Dante', 'Bassus', '20123456789', 'Admin@gmail.com', 'pbkdf2$120000$yZ+FIV9QGjwyOF2wI8+gUA==$Xv8qpBEmpYlcBZCRel8fB7qiDpFdmXJx7Dh9bqae8SI=', '1122334455'),
(2, 4, 'Sofia', 'Mendez', '27999111111', 'gerencia@horizonte.com', 'pbkdf2$120000$SG9yaVNhbHQyMDI2ISEhIQ==$sKnO/f+doHqCb3y8ML7N2MA0uvwPARyVgqfTlbV3kNc=', '1100000001'),
(3, 2, 'Lucas', 'Fernandez', '20123456780', 'coordinacion@horizonte.com', 'pbkdf2$120000$SG9yaVNhbHQyMDI2ISEhIQ==$sKnO/f+doHqCb3y8ML7N2MA0uvwPARyVgqfTlbV3kNc=', '1100000002'),
(4, 3, 'Ana', 'Silva', '20123456781', 'personal@horizonte.com', 'pbkdf2$120000$SG9yaVNhbHQyMDI2ISEhIQ==$sKnO/f+doHqCb3y8ML7N2MA0uvwPARyVgqfTlbV3kNc=', '1100000003'),
(5, 3, 'Mateo', 'Alvarez', '20123456782', 'mozo@horizonte.com', 'pbkdf2$120000$SG9yaVNhbHQyMDI2ISEhIQ==$sKnO/f+doHqCb3y8ML7N2MA0uvwPARyVgqfTlbV3kNc=', '1100000004');

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
  `id_espacio` int(11) NOT NULL,
  `id_paquete` int(11) DEFAULT NULL,
  `id_estado` int(11) NOT NULL,
  `fecha_evento` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `cantidad_invitados` int(11) NOT NULL,
  `asistencia_real` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `eventos`
--

INSERT INTO `eventos` (`id_evento`, `id_cliente`, `id_espacio`, `id_paquete`, `id_estado`, `fecha_evento`, `hora_inicio`, `hora_fin`, `cantidad_invitados`, `asistencia_real`) VALUES
(1, 1, 1, 2, 3, '2026-09-19', '18:00:00', '23:30:00', 120, 114),
(2, 2, 2, 2, 2, '2026-10-24', '17:00:00', '23:00:00', 85, NULL),
(3, 3, 3, 1, 1, '2026-11-14', '18:00:00', '22:30:00', 55, NULL),
(4, 4, 1, 3, 2, '2026-12-05', '09:00:00', '17:00:00', 180, NULL),
(5, 5, 3, 1, 1, '2026-10-31', '19:00:00', '23:00:00', 60, NULL);

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
(4, 2, 180, 18000.00);

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
(2, 1, 'El microfono secundario tuvo una falla intermitente.', 0.00, 'Lucas Fernandez', '2026-09-19 20:30:00');

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
(1, 'Basico', 'Salon, mobiliario, manteleria, iluminacion y coordinacion.', 900000.00),
(2, 'Premium', 'Paquete Basico con gastronomia, barra y decoracion.', 2200000.00),
(3, 'Corporativo', 'Salon, proyeccion, gastronomia y asistencia tecnica.', 1800000.00);

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
  `costo_unitario` decimal(12,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicios`
--

INSERT INTO `servicios` (`id_servicio`, `id_tercero`, `nombre_servicio`, `costo_unitario`) VALUES
(1, 1, 'DJ y sonido profesional', 250000.00),
(2, 2, 'Catering por persona', 18000.00),
(3, 3, 'Fotografia del evento', 200000.00),
(4, NULL, 'Coordinacion integral', 120000.00);

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

--
-- Índices para tablas volcadas
--

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
  ADD KEY `fk_evento_estado` (`id_estado`);

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
-- Indices de la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  ADD PRIMARY KEY (`id_presupuesto`),
  ADD KEY `fk_presupuesto_evento` (`id_evento`);

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
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
  MODIFY `id_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
  MODIFY `id_incidencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

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
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `servicios`
--
ALTER TABLE `servicios`
  MODIFY `id_servicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
-- Restricciones para tablas volcadas
--

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
  ADD CONSTRAINT `fk_evento_paquete` FOREIGN KEY (`id_paquete`) REFERENCES `paquetes` (`id_paquete`) ON UPDATE CASCADE;

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
-- Filtros para la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  ADD CONSTRAINT `fk_presupuesto_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD CONSTRAINT `fk_servicio_tercero` FOREIGN KEY (`id_tercero`) REFERENCES `terceros` (`id_tercero`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
