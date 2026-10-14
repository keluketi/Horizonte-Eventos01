-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 14-10-2026 a las 23:13:41
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

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
  `telefono` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `apellido`, `dni_cuit`, `email`, `telefono`) VALUES
(1, 'Juan', 'Pérez', '20123456789', 'juan.perez@email.com', '+5491112345678'),
(2, 'Maria', 'Gómez', '27987654321', 'maria.gomez@email.com', '+5491187654321'),
(3, 'Carlos', 'Rodríguez', '20555544449', 'carlos.rodriguez@email.com', '+5491155554444'),
(4, 'Cliente 4', 'Ape 4', '20000000004', 'c4@email.com', '1100000004'),
(5, 'Cliente 5', 'Ape 5', '20000000005', 'c5@email.com', '1100000005'),
(6, 'Cliente 6', 'Ape 6', '20000000006', 'c6@email.com', '1100000006'),
(7, 'Cliente 7', 'Ape 7', '20000000007', 'c7@email.com', '1100000007'),
(8, 'Cliente 8', 'Ape 8', '20000000008', 'c8@email.com', '1100000008'),
(9, 'Cliente 9', 'Ape 9', '20000000009', 'c9@email.com', '1100000009'),
(10, 'Cliente 10', 'Ape 10', '20000000010', 'c10@email.com', '1100000010'),
(11, 'Cliente 11', 'Ape 11', '20000000011', 'c11@email.com', '1100000011'),
(12, 'Cliente 12', 'Ape 12', '20000000012', 'c12@email.com', '1100000012'),
(13, 'Cliente 13', 'Ape 13', '20000000013', 'c13@email.com', '1100000013'),
(14, 'Cliente 14', 'Ape 14', '20000000014', 'c14@email.com', '1100000014'),
(15, 'Cliente 15', 'Ape 15', '20000000015', 'c15@email.com', '1100000015'),
(16, 'Cliente 16', 'Ape 16', '20000000016', 'c16@email.com', '1100000016'),
(17, 'Cliente 17', 'Ape 17', '20000000017', 'c17@email.com', '1100000017'),
(18, 'Cliente 18', 'Ape 18', '20000000018', 'c18@email.com', '1100000018'),
(19, 'Cliente 19', 'Ape 19', '20000000019', 'c19@email.com', '1100000019'),
(20, 'Cliente 20', 'Ape 20', '20000000020', 'c20@email.com', '1100000020'),
(21, 'Cliente 21', 'Ape 21', '20000000021', 'c21@email.com', '1100000021'),
(22, 'Cliente 22', 'Ape 22', '20000000022', 'c22@email.com', '1100000022'),
(23, 'Cliente 23', 'Ape 23', '20000000023', 'c23@email.com', '1100000023'),
(24, 'Cliente 24', 'Ape 24', '20000000024', 'c24@email.com', '1100000024'),
(25, 'Cliente 25', 'Ape 25', '20000000025', 'c25@email.com', '1100000025'),
(26, 'Cliente 26', 'Ape 26', '20000000026', 'c26@email.com', '1100000026'),
(27, 'Cliente 27', 'Ape 27', '20000000027', 'c27@email.com', '1100000027'),
(28, 'Cliente 28', 'Ape 28', '20000000028', 'c28@email.com', '1100000028'),
(29, 'Cliente 29', 'Ape 29', '20000000029', 'c29@email.com', '1100000029'),
(30, 'Cliente 30', 'Ape 30', '20000000030', 'c30@email.com', '1100000030');

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
(1, 1, 'Carlos', 'Gómez', '20-34567890-9', 'carios.admin@horizonte.com', '$2y$10$e8T1114gGk14q0g', '1144556677'),
(2, 2, 'Mariana', 'López', '27-38901234-4', 'mariana.organizadora@horizonte.com', 'a', '1155667788'),
(3, 3, 'Sofia', 'Fernández', '27-40123456-2', 'sofia.recepcion@horizonte.com', '$2y$10$e8T1114gGk14q0g', '1166778899'),
(4, 4, 'Emp 4', 'Ape 4', '20-00000004-0', 'e4@hor.com', 'pass', '11000004'),
(5, 5, 'Emp 5', 'Ape 5', '20-00000005-0', 'e5@hor.com', 'pass', '11000005'),
(6, 6, 'Emp 6', 'Ape 6', '20-00000006-0', 'e6@hor.com', 'pass', '11000006'),
(7, 7, 'Emp 7', 'Ape 7', '20-00000007-0', 'e7@hor.com', 'pass', '11000007'),
(8, 8, 'Emp 8', 'Ape 8', '20-00000008-0', 'e8@hor.com', 'pass', '11000008'),
(9, 9, 'Emp 9', 'Ape 9', '20-00000009-0', 'e9@hor.com', 'pass', '11000009'),
(10, 10, 'Emp 10', 'Ape 10', '20-00000010-0', 'e10@hor.com', 'pass', '11000010'),
(11, 11, 'Emp 11', 'Ape 11', '20-00000011-0', 'e11@hor.com', 'pass', '11000011'),
(12, 12, 'Emp 12', 'Ape 12', '20-00000012-0', 'e12@hor.com', 'pass', '11000012'),
(13, 13, 'Emp 13', 'Ape 13', '20-00000013-0', 'e13@hor.com', 'pass', '11000013'),
(14, 14, 'Emp 14', 'Ape 14', '20-00000014-0', 'e14@hor.com', 'pass', '11000014'),
(15, 15, 'Emp 15', 'Ape 15', '20-00000015-0', 'e15@hor.com', 'pass', '11000015'),
(16, 16, 'Emp 16', 'Ape 16', '20-00000016-0', 'e16@hor.com', 'pass', '11000016'),
(17, 17, 'Emp 17', 'Ape 17', '20-00000017-0', 'e17@hor.com', 'pass', '11000017'),
(18, 18, 'Emp 18', 'Ape 18', '20-00000018-0', 'e18@hor.com', 'pass', '11000018'),
(19, 19, 'Emp 19', 'Ape 19', '20-00000019-0', 'e19@hor.com', 'pass', '11000019'),
(20, 20, 'Emp 20', 'Ape 20', '20-00000020-0', 'e20@hor.com', 'pass', '11000020'),
(21, 21, 'Emp 21', 'Ape 21', '20-00000021-0', 'e21@hor.com', 'pass', '11000021'),
(22, 22, 'Emp 22', 'Ape 22', '20-00000022-0', 'e22@hor.com', 'pass', '11000022'),
(23, 23, 'Emp 23', 'Ape 23', '20-00000023-0', 'e23@hor.com', 'pass', '11000023'),
(24, 24, 'Emp 24', 'Ape 24', '20-00000024-0', 'e24@hor.com', 'pass', '11000024'),
(25, 25, 'Emp 25', 'Ape 25', '20-00000025-0', 'e25@hor.com', 'pass', '11000025'),
(26, 26, 'Emp 26', 'Ape 26', '20-00000026-0', 'e26@hor.com', 'pass', '11000026'),
(27, 27, 'Emp 27', 'Ape 27', '20-00000027-0', 'e27@hor.com', 'pass', '11000027'),
(28, 28, 'Emp 28', 'Ape 28', '20-00000028-0', 'e28@hor.com', 'pass', '11000028'),
(29, 29, 'Emp 29', 'Ape 29', '20-00000029-0', 'e29@hor.com', 'pass', '11000029'),
(30, 30, 'Emp 30', 'Ape 30', '20-00000030-0', 'e30@hor.com', 'pass', '11000030');

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
(1, 'Salón Principal Horizonte', 200, 150000.00),
(2, 'Jardín de los Olivos', 150, 120000.00),
(3, 'Salón VIP Panorama', 50, 80000.00),
(4, 'Espacio 4', 100, 50000.00),
(5, 'Espacio 5', 100, 50000.00),
(6, 'Espacio 6', 100, 50000.00),
(7, 'Espacio 7', 100, 50000.00),
(8, 'Espacio 8', 100, 50000.00),
(9, 'Espacio 9', 100, 50000.00),
(10, 'Espacio 10', 100, 50000.00),
(11, 'Espacio 11', 100, 50000.00),
(12, 'Espacio 12', 100, 50000.00),
(13, 'Espacio 13', 100, 50000.00),
(14, 'Espacio 14', 100, 50000.00),
(15, 'Espacio 15', 100, 50000.00),
(16, 'Espacio 16', 100, 50000.00),
(17, 'Espacio 17', 100, 50000.00),
(18, 'Espacio 18', 100, 50000.00),
(19, 'Espacio 19', 100, 50000.00),
(20, 'Espacio 20', 100, 50000.00),
(21, 'Espacio 21', 100, 50000.00),
(22, 'Espacio 22', 100, 50000.00),
(23, 'Espacio 23', 100, 50000.00),
(24, 'Espacio 24', 100, 50000.00),
(25, 'Espacio 25', 100, 50000.00),
(26, 'Espacio 26', 100, 50000.00),
(27, 'Espacio 27', 100, 50000.00),
(28, 'Espacio 28', 100, 50000.00),
(29, 'Espacio 29', 100, 50000.00),
(30, 'Espacio 30', 100, 50000.00);

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
(5, 'Cancelado'),
(2, 'Confirmado'),
(3, 'En Proceso'),
(10, 'Estado 10'),
(11, 'Estado 11'),
(12, 'Estado 12'),
(13, 'Estado 13'),
(14, 'Estado 14'),
(15, 'Estado 15'),
(16, 'Estado 16'),
(17, 'Estado 17'),
(18, 'Estado 18'),
(19, 'Estado 19'),
(20, 'Estado 20'),
(21, 'Estado 21'),
(22, 'Estado 22'),
(23, 'Estado 23'),
(24, 'Estado 24'),
(25, 'Estado 25'),
(26, 'Estado 26'),
(27, 'Estado 27'),
(28, 'Estado 28'),
(29, 'Estado 29'),
(30, 'Estado 30'),
(6, 'Estado 6'),
(7, 'Estado 7'),
(8, 'Estado 8'),
(9, 'Estado 9'),
(4, 'Finalizado'),
(1, 'Pendiente');

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
  `cantidad_invitados` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `eventos`
--

INSERT INTO `eventos` (`id_evento`, `id_cliente`, `id_espacio`, `id_paquete`, `id_estado`, `fecha_evento`, `hora_inicio`, `hora_fin`, `cantidad_invitados`) VALUES
(1, 1, 1, 1, 1, '2026-11-01', '20:00:00', '02:00:00', 100),
(2, 2, 2, 2, 2, '2026-11-02', '20:00:00', '02:00:00', 100),
(3, 3, 3, 3, 3, '2026-11-03', '20:00:00', '02:00:00', 100),
(4, 4, 4, 4, 4, '2026-11-04', '20:00:00', '02:00:00', 100),
(5, 5, 5, 5, 5, '2026-11-05', '20:00:00', '02:00:00', 100),
(6, 6, 6, 6, 6, '2026-11-06', '20:00:00', '02:00:00', 100),
(7, 7, 7, 7, 7, '2026-11-07', '20:00:00', '02:00:00', 100),
(8, 8, 8, 8, 8, '2026-11-08', '20:00:00', '02:00:00', 100),
(9, 9, 9, 9, 9, '2026-11-09', '20:00:00', '02:00:00', 100),
(10, 10, 10, 10, 10, '2026-11-10', '20:00:00', '02:00:00', 100),
(11, 11, 11, 11, 11, '2026-11-11', '20:00:00', '02:00:00', 100),
(12, 12, 12, 12, 12, '2026-11-12', '20:00:00', '02:00:00', 100),
(13, 13, 13, 13, 13, '2026-11-13', '20:00:00', '02:00:00', 100),
(14, 14, 14, 14, 14, '2026-11-14', '20:00:00', '02:00:00', 100),
(15, 15, 15, 15, 15, '2026-11-15', '20:00:00', '02:00:00', 100),
(16, 16, 16, 16, 16, '2026-11-16', '20:00:00', '02:00:00', 100),
(17, 17, 17, 17, 17, '2026-11-17', '20:00:00', '02:00:00', 100),
(18, 18, 18, 18, 18, '2026-11-18', '20:00:00', '02:00:00', 100),
(19, 19, 19, 19, 19, '2026-11-19', '20:00:00', '02:00:00', 100),
(20, 20, 20, 20, 20, '2026-11-20', '20:00:00', '02:00:00', 100),
(21, 21, 21, 21, 21, '2026-11-21', '20:00:00', '02:00:00', 100),
(22, 22, 22, 22, 22, '2026-11-22', '20:00:00', '02:00:00', 100),
(23, 23, 23, 23, 23, '2026-11-23', '20:00:00', '02:00:00', 100),
(24, 24, 24, 24, 24, '2026-11-24', '20:00:00', '02:00:00', 100),
(25, 25, 25, 25, 25, '2026-11-25', '20:00:00', '02:00:00', 100),
(26, 26, 26, 26, 26, '2026-11-26', '20:00:00', '02:00:00', 100),
(27, 27, 27, 27, 27, '2026-11-27', '20:00:00', '02:00:00', 100),
(28, 28, 28, 28, 28, '2026-11-28', '20:00:00', '02:00:00', 100),
(29, 29, 29, 29, 29, '2026-11-29', '20:00:00', '02:00:00', 100),
(30, 30, 30, 30, 30, '2026-11-30', '20:00:00', '02:00:00', 100);

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
(1, 1, 'Rol 1'),
(2, 2, 'Rol 2'),
(3, 3, 'Rol 3'),
(4, 4, 'Rol 4'),
(5, 5, 'Rol 5'),
(6, 6, 'Rol 6'),
(7, 7, 'Rol 7'),
(8, 8, 'Rol 8'),
(9, 9, 'Rol 9'),
(10, 10, 'Rol 10'),
(11, 11, 'Rol 11'),
(12, 12, 'Rol 12'),
(13, 13, 'Rol 13'),
(14, 14, 'Rol 14'),
(15, 15, 'Rol 15'),
(16, 16, 'Rol 16'),
(17, 17, 'Rol 17'),
(18, 18, 'Rol 18'),
(19, 19, 'Rol 19'),
(20, 20, 'Rol 20'),
(21, 21, 'Rol 21'),
(22, 22, 'Rol 22'),
(23, 23, 'Rol 23'),
(24, 24, 'Rol 24'),
(25, 25, 'Rol 25'),
(26, 26, 'Rol 26'),
(27, 27, 'Rol 27'),
(28, 28, 'Rol 28'),
(29, 29, 'Rol 29'),
(30, 30, 'Rol 30');

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
(1, 1, 1, 1000.00),
(2, 2, 1, 1000.00),
(3, 3, 1, 1000.00),
(4, 4, 1, 1000.00),
(5, 5, 1, 1000.00),
(6, 6, 1, 1000.00),
(7, 7, 1, 1000.00),
(8, 8, 1, 1000.00),
(9, 9, 1, 1000.00),
(10, 10, 1, 1000.00),
(11, 11, 1, 1000.00),
(12, 12, 1, 1000.00),
(13, 13, 1, 1000.00),
(14, 14, 1, 1000.00),
(15, 15, 1, 1000.00),
(16, 16, 1, 1000.00),
(17, 17, 1, 1000.00),
(18, 18, 1, 1000.00),
(19, 19, 1, 1000.00),
(20, 20, 1, 1000.00),
(21, 21, 1, 1000.00),
(22, 22, 1, 1000.00),
(23, 23, 1, 1000.00),
(24, 24, 1, 1000.00),
(25, 25, 1, 1000.00),
(26, 26, 1, 1000.00),
(27, 27, 1, 1000.00),
(28, 28, 1, 1000.00),
(29, 29, 1, 1000.00),
(30, 30, 1, 1000.00);

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
(1, 1, 'F-001', 'Factura A', 150000.00, '2026-10-01 00:00:00'),
(2, 2, 'F-002', 'Factura A', 150000.00, '2026-10-02 00:00:00'),
(3, 3, 'F-003', 'Factura A', 150000.00, '2026-10-03 00:00:00'),
(4, 4, 'F-004', 'Factura A', 150000.00, '2026-10-04 00:00:00'),
(5, 5, 'F-005', 'Factura A', 150000.00, '2026-10-05 00:00:00'),
(6, 6, 'F-006', 'Factura A', 150000.00, '2026-10-06 00:00:00'),
(7, 7, 'F-007', 'Factura A', 150000.00, '2026-10-07 00:00:00'),
(8, 8, 'F-008', 'Factura A', 150000.00, '2026-10-08 00:00:00'),
(9, 9, 'F-009', 'Factura A', 150000.00, '2026-10-09 00:00:00'),
(10, 10, 'F-010', 'Factura A', 150000.00, '2026-10-10 00:00:00'),
(11, 11, 'F-011', 'Factura A', 150000.00, '2026-10-11 00:00:00'),
(12, 12, 'F-012', 'Factura A', 150000.00, '2026-10-12 00:00:00'),
(13, 13, 'F-013', 'Factura A', 150000.00, '2026-10-13 00:00:00'),
(14, 14, 'F-014', 'Factura A', 150000.00, '2026-10-14 00:00:00'),
(15, 15, 'F-015', 'Factura A', 150000.00, '2026-10-15 00:00:00'),
(16, 16, 'F-016', 'Factura A', 150000.00, '2026-10-16 00:00:00'),
(17, 17, 'F-017', 'Factura A', 150000.00, '2026-10-17 00:00:00'),
(18, 18, 'F-018', 'Factura A', 150000.00, '2026-10-18 00:00:00'),
(19, 19, 'F-019', 'Factura A', 150000.00, '2026-10-19 00:00:00'),
(20, 20, 'F-020', 'Factura A', 150000.00, '2026-10-20 00:00:00'),
(21, 21, 'F-021', 'Factura A', 150000.00, '2026-10-21 00:00:00'),
(22, 22, 'F-022', 'Factura A', 150000.00, '2026-10-22 00:00:00'),
(23, 23, 'F-023', 'Factura A', 150000.00, '2026-10-23 00:00:00'),
(24, 24, 'F-024', 'Factura A', 150000.00, '2026-10-24 00:00:00'),
(25, 25, 'F-025', 'Factura A', 150000.00, '2026-10-25 00:00:00'),
(26, 26, 'F-026', 'Factura A', 150000.00, '2026-10-26 00:00:00'),
(27, 27, 'F-027', 'Factura A', 150000.00, '2026-10-27 00:00:00'),
(28, 28, 'F-028', 'Factura A', 150000.00, '2026-10-28 00:00:00'),
(29, 29, 'F-029', 'Factura A', 150000.00, '2026-10-29 00:00:00'),
(30, 30, 'F-030', 'Factura A', 150000.00, '2026-10-30 00:00:00');

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
(1, 1, 1, 1, 'Msj 1', '2026-10-01 10:00:00'),
(2, 2, 2, 2, 'Msj 2', '2026-10-02 10:00:00'),
(3, 3, 3, 3, 'Msj 3', '2026-10-03 10:00:00'),
(4, 4, 4, 4, 'Msj 4', '2026-10-04 10:00:00'),
(5, 5, 5, 5, 'Msj 5', '2026-10-05 10:00:00'),
(6, 6, 6, 6, 'Msj 6', '2026-10-06 10:00:00'),
(7, 7, 7, 7, 'Msj 7', '2026-10-07 10:00:00'),
(8, 8, 8, 8, 'Msj 8', '2026-10-08 10:00:00'),
(9, 9, 9, 9, 'Msj 9', '2026-10-09 10:00:00'),
(10, 10, 10, 10, 'Msj 10', '2026-10-10 10:00:00'),
(11, 11, 11, 11, 'Msj 11', '2026-10-11 10:00:00'),
(12, 12, 12, 12, 'Msj 12', '2026-10-12 10:00:00'),
(13, 13, 13, 13, 'Msj 13', '2026-10-13 10:00:00'),
(14, 14, 14, 14, 'Msj 14', '2026-10-14 10:00:00'),
(15, 15, 15, 15, 'Msj 15', '2026-10-15 10:00:00'),
(16, 16, 16, 16, 'Msj 16', '2026-10-16 10:00:00'),
(17, 17, 17, 17, 'Msj 17', '2026-10-17 10:00:00'),
(18, 18, 18, 18, 'Msj 18', '2026-10-18 10:00:00'),
(19, 19, 19, 19, 'Msj 19', '2026-10-19 10:00:00'),
(20, 20, 20, 20, 'Msj 20', '2026-10-20 10:00:00'),
(21, 21, 21, 21, 'Msj 21', '2026-10-21 10:00:00'),
(22, 22, 22, 22, 'Msj 22', '2026-10-22 10:00:00'),
(23, 23, 23, 23, 'Msj 23', '2026-10-23 10:00:00'),
(24, 24, 24, 24, 'Msj 24', '2026-10-24 10:00:00'),
(25, 25, 25, 25, 'Msj 25', '2026-10-25 10:00:00'),
(26, 26, 26, 26, 'Msj 26', '2026-10-26 10:00:00'),
(27, 27, 27, 27, 'Msj 27', '2026-10-27 10:00:00'),
(28, 28, 28, 28, 'Msj 28', '2026-10-28 10:00:00'),
(29, 29, 29, 29, 'Msj 29', '2026-10-29 10:00:00'),
(30, 30, 30, 30, 'Msj 30', '2026-10-30 10:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modulos`
--

CREATE TABLE `modulos` (
  `id_modulo` int(11) NOT NULL,
  `nombre_modulo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `modulos`
--

INSERT INTO `modulos` (`id_modulo`, `nombre_modulo`) VALUES
(1, 'Modulo 1'),
(2, 'Modulo 2'),
(3, 'Modulo 3'),
(4, 'Modulo 4'),
(5, 'Modulo 5'),
(6, 'Modulo 6'),
(7, 'Modulo 7'),
(8, 'Modulo 8'),
(9, 'Modulo 9'),
(10, 'Modulo 10'),
(11, 'Modulo 11'),
(12, 'Modulo 12'),
(13, 'Modulo 13'),
(14, 'Modulo 14'),
(15, 'Modulo 15'),
(16, 'Modulo 16'),
(17, 'Modulo 17'),
(18, 'Modulo 18'),
(19, 'Modulo 19'),
(20, 'Modulo 20'),
(21, 'Modulo 21'),
(22, 'Modulo 22'),
(23, 'Modulo 23'),
(24, 'Modulo 24'),
(25, 'Modulo 25'),
(26, 'Modulo 26'),
(27, 'Modulo 27'),
(28, 'Modulo 28'),
(29, 'Modulo 29'),
(30, 'Modulo 30');

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
(1, 'Básico', 'super basico', 150000.00),
(2, 'Premium', 'mega premium', 300000.00),
(3, 'Deluxe Todo Incluido', 'todo tuyo', 500000.00),
(4, 'Paquete 4', 'Desc 4', 100000.00),
(5, 'Paquete 5', 'Desc 5', 100000.00),
(6, 'Paquete 6', 'Desc 6', 100000.00),
(7, 'Paquete 7', 'Desc 7', 100000.00),
(8, 'Paquete 8', 'Desc 8', 100000.00),
(9, 'Paquete 9', 'Desc 9', 100000.00),
(10, 'Paquete 10', 'Desc 10', 100000.00),
(11, 'Paquete 11', 'Desc 11', 100000.00),
(12, 'Paquete 12', 'Desc 12', 100000.00),
(13, 'Paquete 13', 'Desc 13', 100000.00),
(14, 'Paquete 14', 'Desc 14', 100000.00),
(15, 'Paquete 15', 'Desc 15', 100000.00),
(16, 'Paquete 16', 'Desc 16', 100000.00),
(17, 'Paquete 17', 'Desc 17', 100000.00),
(18, 'Paquete 18', 'Desc 18', 100000.00),
(19, 'Paquete 19', 'Desc 19', 100000.00),
(20, 'Paquete 20', 'Desc 20', 100000.00),
(21, 'Paquete 21', 'Desc 21', 100000.00),
(22, 'Paquete 22', 'Desc 22', 100000.00),
(23, 'Paquete 23', 'Desc 23', 100000.00),
(24, 'Paquete 24', 'Desc 24', 100000.00),
(25, 'Paquete 25', 'Desc 25', 100000.00),
(26, 'Paquete 26', 'Desc 26', 100000.00),
(27, 'Paquete 27', 'Desc 27', 100000.00),
(28, 'Paquete 28', 'Desc 28', 100000.00),
(29, 'Paquete 29', 'Desc 29', 100000.00),
(30, 'Paquete 30', 'Desc 30', 100000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos_rol`
--

CREATE TABLE `permisos_rol` (
  `id_rol` int(11) NOT NULL,
  `id_modulo` int(11) NOT NULL,
  `puede_leer` tinyint(1) NOT NULL DEFAULT 0,
  `puede_crear` tinyint(1) NOT NULL DEFAULT 0,
  `puede_editar` tinyint(1) NOT NULL DEFAULT 0,
  `puede_eliminar` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `permisos_rol`
--

INSERT INTO `permisos_rol` (`id_rol`, `id_modulo`, `puede_leer`, `puede_crear`, `puede_editar`, `puede_eliminar`) VALUES
(1, 1, 1, 1, 1, 1),
(2, 2, 1, 1, 1, 0),
(3, 3, 1, 0, 0, 0),
(4, 4, 1, 0, 0, 0),
(5, 5, 1, 1, 1, 1),
(6, 6, 1, 1, 1, 1),
(7, 7, 1, 1, 1, 1),
(8, 8, 1, 1, 1, 1),
(9, 9, 1, 1, 1, 1),
(10, 10, 1, 1, 1, 1),
(11, 11, 1, 1, 1, 1),
(12, 12, 1, 1, 1, 1),
(13, 13, 1, 1, 1, 1),
(14, 14, 1, 1, 1, 1),
(15, 15, 1, 1, 1, 1),
(16, 16, 1, 1, 1, 1),
(17, 17, 1, 1, 1, 1),
(18, 18, 1, 1, 1, 1),
(19, 19, 1, 1, 1, 1),
(20, 20, 1, 1, 1, 1),
(21, 21, 1, 1, 1, 1),
(22, 22, 1, 1, 1, 1),
(23, 23, 1, 1, 1, 1),
(24, 24, 1, 1, 1, 1),
(25, 25, 1, 1, 1, 1),
(26, 26, 1, 1, 1, 1),
(27, 27, 1, 1, 1, 1),
(28, 28, 1, 1, 1, 1),
(29, 29, 1, 1, 1, 1),
(30, 30, 1, 1, 1, 1);

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
(1, 1, '2026-10-01', '2026-10-15', 200000.00, 'Pendiente'),
(2, 2, '2026-10-02', '2026-10-16', 200000.00, 'Pendiente'),
(3, 3, '2026-10-03', '2026-10-17', 200000.00, 'Pendiente'),
(4, 4, '2026-10-04', '2026-10-18', 200000.00, 'Pendiente'),
(5, 5, '2026-10-05', '2026-10-19', 200000.00, 'Pendiente'),
(6, 6, '2026-10-06', '2026-10-20', 200000.00, 'Pendiente'),
(7, 7, '2026-10-07', '2026-10-21', 200000.00, 'Pendiente'),
(8, 8, '2026-10-08', '2026-10-22', 200000.00, 'Pendiente'),
(9, 9, '2026-10-09', '2026-10-23', 200000.00, 'Pendiente'),
(10, 10, '2026-10-10', '2026-10-24', 200000.00, 'Pendiente'),
(11, 11, '2026-10-11', '2026-10-25', 200000.00, 'Pendiente'),
(12, 12, '2026-10-12', '2026-10-26', 200000.00, 'Pendiente'),
(13, 13, '2026-10-13', '2026-10-27', 200000.00, 'Pendiente'),
(14, 14, '2026-10-14', '2026-10-28', 200000.00, 'Pendiente'),
(15, 15, '2026-10-15', '2026-10-29', 200000.00, 'Pendiente'),
(16, 16, '2026-10-16', '2026-10-30', 200000.00, 'Pendiente'),
(17, 17, '2026-10-17', '2026-10-31', 200000.00, 'Pendiente'),
(18, 18, '2026-10-18', '2026-11-01', 200000.00, 'Pendiente'),
(19, 19, '2026-10-19', '2026-11-02', 200000.00, 'Pendiente'),
(20, 20, '2026-10-20', '2026-11-03', 200000.00, 'Pendiente'),
(21, 21, '2026-10-21', '2026-11-04', 200000.00, 'Pendiente'),
(22, 22, '2026-10-22', '2026-11-05', 200000.00, 'Pendiente'),
(23, 23, '2026-10-23', '2026-11-06', 200000.00, 'Pendiente'),
(24, 24, '2026-10-24', '2026-11-07', 200000.00, 'Pendiente'),
(25, 25, '2026-10-25', '2026-11-08', 200000.00, 'Pendiente'),
(26, 26, '2026-10-26', '2026-11-09', 200000.00, 'Pendiente'),
(27, 27, '2026-10-27', '2026-11-10', 200000.00, 'Pendiente'),
(28, 28, '2026-10-28', '2026-11-11', 200000.00, 'Pendiente'),
(29, 29, '2026-10-29', '2026-11-12', 200000.00, 'Pendiente'),
(30, 30, '2026-10-30', '2026-11-13', 200000.00, 'Pendiente');

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
(3, 'Cordinacion'),
(2, 'Gerencia'),
(4, 'Personal'),
(10, 'Rol 10'),
(11, 'Rol 11'),
(12, 'Rol 12'),
(13, 'Rol 13'),
(14, 'Rol 14'),
(15, 'Rol 15'),
(16, 'Rol 16'),
(17, 'Rol 17'),
(18, 'Rol 18'),
(19, 'Rol 19'),
(20, 'Rol 20'),
(21, 'Rol 21'),
(22, 'Rol 22'),
(23, 'Rol 23'),
(24, 'Rol 24'),
(25, 'Rol 25'),
(26, 'Rol 26'),
(27, 'Rol 27'),
(28, 'Rol 28'),
(29, 'Rol 29'),
(30, 'Rol 30'),
(5, 'Rol 5'),
(6, 'Rol 6'),
(7, 'Rol 7'),
(8, 'Rol 8'),
(9, 'Rol 9');

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
(1, 1, 'Servicio 1', 5000.00),
(2, 2, 'Servicio 2', 5000.00),
(3, 3, 'Servicio 3', 5000.00),
(4, 4, 'Servicio 4', 5000.00),
(5, 5, 'Servicio 5', 5000.00),
(6, 6, 'Servicio 6', 5000.00),
(7, 7, 'Servicio 7', 5000.00),
(8, 8, 'Servicio 8', 5000.00),
(9, 9, 'Servicio 9', 5000.00),
(10, 10, 'Servicio 10', 5000.00),
(11, 11, 'Servicio 11', 5000.00),
(12, 12, 'Servicio 12', 5000.00),
(13, 13, 'Servicio 13', 5000.00),
(14, 14, 'Servicio 14', 5000.00),
(15, 15, 'Servicio 15', 5000.00),
(16, 16, 'Servicio 16', 5000.00),
(17, 17, 'Servicio 17', 5000.00),
(18, 18, 'Servicio 18', 5000.00),
(19, 19, 'Servicio 19', 5000.00),
(20, 20, 'Servicio 20', 5000.00),
(21, 21, 'Servicio 21', 5000.00),
(22, 22, 'Servicio 22', 5000.00),
(23, 23, 'Servicio 23', 5000.00),
(24, 24, 'Servicio 24', 5000.00),
(25, 25, 'Servicio 25', 5000.00),
(26, 26, 'Servicio 26', 5000.00),
(27, 27, 'Servicio 27', 5000.00),
(28, 28, 'Servicio 28', 5000.00),
(29, 29, 'Servicio 29', 5000.00),
(30, 30, 'Servicio 30', 5000.00);

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
(1, 'Tercero 1', 'Contacto 1', '11000001', 'Rubro 1'),
(2, 'Tercero 2', 'Contacto 2', '11000002', 'Rubro 2'),
(3, 'Tercero 3', 'Contacto 3', '11000003', 'Rubro 3'),
(4, 'Tercero 4', 'Contacto 4', '11000004', 'Rubro 4'),
(5, 'Tercero 5', 'Contacto 5', '11000005', 'Rubro 5'),
(6, 'Tercero 6', 'Contacto 6', '11000006', 'Rubro 6'),
(7, 'Tercero 7', 'Contacto 7', '11000007', 'Rubro 7'),
(8, 'Tercero 8', 'Contacto 8', '11000008', 'Rubro 8'),
(9, 'Tercero 9', 'Contacto 9', '11000009', 'Rubro 9'),
(10, 'Tercero 10', 'Contacto 10', '11000010', 'Rubro 10'),
(11, 'Tercero 11', 'Contacto 11', '11000011', 'Rubro 11'),
(12, 'Tercero 12', 'Contacto 12', '11000012', 'Rubro 12'),
(13, 'Tercero 13', 'Contacto 13', '11000013', 'Rubro 13'),
(14, 'Tercero 14', 'Contacto 14', '11000014', 'Rubro 14'),
(15, 'Tercero 15', 'Contacto 15', '11000015', 'Rubro 15'),
(16, 'Tercero 16', 'Contacto 16', '11000016', 'Rubro 16'),
(17, 'Tercero 17', 'Contacto 17', '11000017', 'Rubro 17'),
(18, 'Tercero 18', 'Contacto 18', '11000018', 'Rubro 18'),
(19, 'Tercero 19', 'Contacto 19', '11000019', 'Rubro 19'),
(20, 'Tercero 20', 'Contacto 20', '11000020', 'Rubro 20'),
(21, 'Tercero 21', 'Contacto 21', '11000021', 'Rubro 21'),
(22, 'Tercero 22', 'Contacto 22', '11000022', 'Rubro 22'),
(23, 'Tercero 23', 'Contacto 23', '11000023', 'Rubro 23'),
(24, 'Tercero 24', 'Contacto 24', '11000024', 'Rubro 24'),
(25, 'Tercero 25', 'Contacto 25', '11000025', 'Rubro 25'),
(26, 'Tercero 26', 'Contacto 26', '11000026', 'Rubro 26'),
(27, 'Tercero 27', 'Contacto 27', '11000027', 'Rubro 27'),
(28, 'Tercero 28', 'Contacto 28', '11000028', 'Rubro 28'),
(29, 'Tercero 29', 'Contacto 29', '11000029', 'Rubro 29'),
(30, 'Tercero 30', 'Contacto 30', '11000030', 'Rubro 30');

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
-- Indices de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD PRIMARY KEY (`id_mensaje`),
  ADD KEY `fk_mensaje_evento` (`id_evento`),
  ADD KEY `fk_mensaje_cliente` (`id_cliente`),
  ADD KEY `fk_mensaje_empleado` (`id_empleado`);

--
-- Indices de la tabla `modulos`
--
ALTER TABLE `modulos`
  ADD PRIMARY KEY (`id_modulo`);

--
-- Indices de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  ADD PRIMARY KEY (`id_paquete`);

--
-- Indices de la tabla `permisos_rol`
--
ALTER TABLE `permisos_rol`
  ADD PRIMARY KEY (`id_rol`,`id_modulo`),
  ADD KEY `fk_permiso_modulo` (`id_modulo`);

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
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `espacios`
--
ALTER TABLE `espacios`
  MODIFY `id_espacio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `estado_del_evento`
--
ALTER TABLE `estado_del_evento`
  MODIFY `id_estado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `id_factura` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  MODIFY `id_mensaje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `modulos`
--
ALTER TABLE `modulos`
  MODIFY `id_modulo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `paquetes`
--
ALTER TABLE `paquetes`
  MODIFY `id_paquete` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `presupuestos`
--
ALTER TABLE `presupuestos`
  MODIFY `id_presupuesto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `servicios`
--
ALTER TABLE `servicios`
  MODIFY `id_servicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `solicitudes_web`
--
ALTER TABLE `solicitudes_web`
  MODIFY `id_solicitud` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `terceros`
--
ALTER TABLE `terceros`
  MODIFY `id_tercero` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

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
-- Filtros para la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD CONSTRAINT `fk_mensaje_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_mensaje_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_mensaje_evento` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `permisos_rol`
--
ALTER TABLE `permisos_rol`
  ADD CONSTRAINT `fk_permiso_modulo` FOREIGN KEY (`id_modulo`) REFERENCES `modulos` (`id_modulo`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_permiso_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE CASCADE ON UPDATE CASCADE;

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
