-- Migracion aditiva para la base horizonte_eventos (volcado MariaDB 10.4.32 del 19-10-2026).
-- Importar el volcado existente primero y ejecutar despues este archivo.
-- Conserva los registros y precios existentes; se puede volver a ejecutar.

CREATE TABLE IF NOT EXISTS tipos_evento (
    id_tipo_evento INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_tipo_evento),
    UNIQUE KEY uq_tipos_evento_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO tipos_evento (nombre) VALUES
    ('Casamientos'),
    ('Cumpleaños de 15'),
    ('Cumpleaños infantiles'),
    ('Eventos corporativos'),
    ('Aniversarios'),
    ('Bautismos'),
    ('Reuniones sociales')
ON DUPLICATE KEY UPDATE activo = 1;

ALTER TABLE eventos
    ADD COLUMN IF NOT EXISTS id_tipo_evento INT NULL AFTER id_cliente;
ALTER TABLE eventos
    ADD COLUMN IF NOT EXISTS observaciones TEXT NULL;

-- NULL indica que el inventario/capacidad todavía debe confirmarse manualmente.
ALTER TABLE servicios
    ADD COLUMN IF NOT EXISTS cantidad_disponible INT UNSIGNED NULL DEFAULT NULL;

CREATE TABLE IF NOT EXISTS paquete_servicios (
    id_paquete INT NOT NULL,
    id_servicio INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_paquete, id_servicio),
    CONSTRAINT fk_paquete_servicios_paquete FOREIGN KEY (id_paquete)
        REFERENCES paquetes (id_paquete) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_paquete_servicios_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicios (id_servicio) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Si falta alguno de los paquetes iniciales, se crea editable con precio 0 para que Administración lo configure.
INSERT INTO paquetes (nombre_paquete, descripcion, precio_fijo)
SELECT 'Basico', 'Plantilla inicial para eventos pequeños; componentes editables.', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM paquetes WHERE LOWER(nombre_paquete) = 'basico');
INSERT INTO paquetes (nombre_paquete, descripcion, precio_fijo)
SELECT 'Premium', 'Plantilla inicial con servicios ampliados; componentes editables.', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM paquetes WHERE LOWER(nombre_paquete) = 'premium');
INSERT INTO paquetes (nombre_paquete, descripcion, precio_fijo)
SELECT 'Corporativo', 'Plantilla inicial para eventos empresariales; componentes editables.', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM paquetes WHERE LOWER(nombre_paquete) = 'corporativo');

-- Completa las descripciones iniciales sin reemplazar cambios que ya hizo Administración.
UPDATE paquetes SET descripcion = 'Pensado para eventos pequeños. Incluye alquiler del salón, mesas y sillas, mantelería básica, iluminación, sonido básico y personal de coordinación.'
WHERE LOWER(nombre_paquete) = 'basico'
  AND (descripcion IS NULL OR descripcion LIKE 'Plantilla inicial%'
       OR descripcion = 'Salon, mobiliario, manteleria, iluminacion y coordinacion.');
UPDATE paquetes SET descripcion = 'Incluye todo lo del paquete Básico y suma servicio gastronómico, barra, decoración, sonido e iluminación profesional, personal adicional y coordinación integral.'
WHERE LOWER(nombre_paquete) = 'premium'
  AND (descripcion IS NULL OR descripcion LIKE 'Plantilla inicial%'
       OR descripcion = 'Paquete Basico con gastronomia, barra y decoracion.');
UPDATE paquetes SET descripcion = 'Pensado para eventos empresariales. Incluye salón, mobiliario, sonido, proyección, servicio gastronómico, personal de coordinación y asistencia técnica.'
WHERE LOWER(nombre_paquete) = 'corporativo'
  AND (descripcion IS NULL OR descripcion LIKE 'Plantilla inicial%'
       OR descripcion = 'Salon, proyeccion, gastronomia y asistencia tecnica.');

-- Recursos propios y servicios que pueden ofrecerse desde la empresa.
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Mesas y sillas', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Mesas y sillas'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Mantelería básica', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Mantelería básica'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Iluminación', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Iluminación'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Sonido básico', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Sonido básico'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Personal de coordinación', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Personal de coordinación'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Servicio gastronómico', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) LIKE '%gastron%'
                  OR LOWER(nombre_servicio) LIKE '%catering%');
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Alquiler del salón', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Alquiler del salón'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Barra', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Barra'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Decoración', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Decoración'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Sonido e iluminación profesional', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios
                  WHERE LOWER(nombre_servicio) LIKE '%sonido%'
                    AND LOWER(nombre_servicio) LIKE '%profesional%');
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Personal adicional', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Personal adicional'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Coordinación integral', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Coordinación integral'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Mobiliario', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Mobiliario'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Sonido', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Sonido'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Proyección', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Proyección'));
INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario)
SELECT NULL, 'Asistencia técnica', 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE LOWER(nombre_servicio) = LOWER('Asistencia técnica'));

-- Configuración inicial de componentes para los tres paquetes, editable desde Administración.
INSERT IGNORE INTO paquete_servicios (id_paquete, id_servicio, cantidad)
SELECT p.id_paquete, s.id_servicio, 1
FROM paquetes p CROSS JOIN servicios s
WHERE LOWER(p.nombre_paquete) = 'basico'
  AND s.nombre_servicio IN ('Alquiler del salón', 'Mesas y sillas', 'Mantelería básica', 'Iluminación',
                            'Sonido básico', 'Personal de coordinación');

INSERT IGNORE INTO paquete_servicios (id_paquete, id_servicio, cantidad)
SELECT p.id_paquete, s.id_servicio, 1
FROM paquetes p CROSS JOIN servicios s
WHERE LOWER(p.nombre_paquete) = 'premium'
  AND s.nombre_servicio IN ('Alquiler del salón', 'Mesas y sillas', 'Mantelería básica', 'Iluminación',
                            'Sonido básico', 'Personal de coordinación', 'Servicio gastronómico',
                            'Barra', 'Decoración', 'Sonido e iluminación profesional', 'DJ y sonido profesional',
                            'Personal adicional', 'Coordinación integral');
-- Compatibilidad con bases que ya tenían el servicio con el nombre anterior.
INSERT IGNORE INTO paquete_servicios (id_paquete, id_servicio, cantidad)
SELECT p.id_paquete, s.id_servicio, 1
FROM paquetes p CROSS JOIN servicios s
WHERE LOWER(p.nombre_paquete) = 'premium'
  AND LOWER(s.nombre_servicio) = 'catering por persona'
  AND NOT EXISTS (SELECT 1 FROM servicios sg WHERE LOWER(sg.nombre_servicio) = 'servicio gastronómico');

INSERT IGNORE INTO paquete_servicios (id_paquete, id_servicio, cantidad)
SELECT p.id_paquete, s.id_servicio, 1
FROM paquetes p CROSS JOIN servicios s
WHERE LOWER(p.nombre_paquete) = 'corporativo'
  AND s.nombre_servicio IN ('Alquiler del salón', 'Mobiliario', 'Sonido', 'Proyección', 'Servicio gastronómico',
                            'Personal de coordinación', 'Asistencia técnica');
INSERT IGNORE INTO paquete_servicios (id_paquete, id_servicio, cantidad)
SELECT p.id_paquete, s.id_servicio, 1
FROM paquetes p CROSS JOIN servicios s
WHERE LOWER(p.nombre_paquete) = 'corporativo'
  AND LOWER(s.nombre_servicio) = 'catering por persona'
  AND NOT EXISTS (SELECT 1 FROM servicios sg WHERE LOWER(sg.nombre_servicio) = 'servicio gastronómico');

-- Relación de tipo opcional para mantener válidos los eventos cargados antes de esta mejora.
SET @fk_tipo_evento_existe = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'eventos'
      AND CONSTRAINT_NAME = 'fk_eventos_tipo_evento'
);
SET @sql_fk_tipo_evento = IF(@fk_tipo_evento_existe = 0,
    'ALTER TABLE eventos ADD CONSTRAINT fk_eventos_tipo_evento FOREIGN KEY (id_tipo_evento) REFERENCES tipos_evento (id_tipo_evento) ON DELETE SET NULL ON UPDATE CASCADE',
    'SELECT 1');
PREPARE stmt_fk_tipo_evento FROM @sql_fk_tipo_evento;
EXECUTE stmt_fk_tipo_evento;
DEALLOCATE PREPARE stmt_fk_tipo_evento;
