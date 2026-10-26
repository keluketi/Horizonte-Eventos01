-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 26-10-2026 a las 22:06:52
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
(7, 6, 2, 2, 2, 1, '2026-10-26', '22:00:00', '23:59:00', 120, NULL, ''),
(8, 1, 5, 1, 3, 3, '2026-10-24', '18:00:00', '23:00:00', 100, NULL, ''),
(9, 5, 5, 3, NULL, 1, '2026-10-24', '18:00:00', '23:00:00', 50, NULL, '');

--
-- Índices para tablas volcadas
--

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
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
