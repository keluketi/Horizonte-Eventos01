-- Horizonte Eventos: aprobación de presupuestos y seguimiento de pagos.
-- Ejecutar una sola vez en la base horizonte_eventos antes de usar el nuevo flujo.

ALTER TABLE presupuestos
    ADD COLUMN IF NOT EXISTS monto_sena DECIMAL(12,2) NULL DEFAULT NULL
    AFTER monto_total;

INSERT INTO estado_del_evento (id_estado, nombre_estado)
SELECT (SELECT COALESCE(MAX(id_estado), 0) + 1 FROM estado_del_evento), 'Aceptado'
WHERE NOT EXISTS (
    SELECT 1 FROM estado_del_evento WHERE LOWER(nombre_estado) = 'aceptado'
);

INSERT INTO estado_del_evento (id_estado, nombre_estado)
SELECT (SELECT COALESCE(MAX(id_estado), 0) + 1 FROM estado_del_evento), 'Señado'
WHERE NOT EXISTS (
    SELECT 1 FROM estado_del_evento WHERE LOWER(nombre_estado) = 'señado'
);

INSERT INTO estado_del_evento (id_estado, nombre_estado)
SELECT (SELECT COALESCE(MAX(id_estado), 0) + 1 FROM estado_del_evento), 'Pagado'
WHERE NOT EXISTS (
    SELECT 1 FROM estado_del_evento WHERE LOWER(nombre_estado) = 'pagado'
);
