-- Regulariza presupuestos de eventos que siguen en estado Presupuestado.
-- Ejecutar en horizonte_eventos después de 06_flujo_presupuestos_pagos.sql.
-- Criterio aplicado: sólo se elimina un presupuesto Pendiente vencido si el
-- evento no tiene pagos. Se conservan los presupuestos Aceptados/Rechazados
-- y cualquier presupuesto asociado a un evento que ya tenga pagos.

START TRANSACTION;

DELETE p
FROM presupuestos p
JOIN eventos e ON e.id_evento = p.id_evento
JOIN estado_del_evento ee ON ee.id_estado = e.id_estado
WHERE p.estado_aprobacion = 'Pendiente'
  AND p.fecha_vencimiento < CURRENT_DATE
  AND LOWER(ee.nombre_estado) = 'presupuestado'
  AND NOT EXISTS (
      SELECT 1 FROM pagos pg WHERE pg.id_evento = e.id_evento
  );

-- Crea una propuesta actualizada para cada evento Presupuestado que no tenga
-- ya un presupuesto Pendiente. Usa el precio vigente del paquete y los
-- precios acordados registrados en evento_servicios.
INSERT INTO presupuestos
    (id_evento, fecha_emision, fecha_vencimiento, monto_total, estado_aprobacion)
SELECT e.id_evento,
       CURRENT_DATE,
       DATE_ADD(CURRENT_DATE, INTERVAL 15 DAY),
       COALESCE(pa.precio_fijo, 0) + COALESCE(SUM(es.cantidad * es.precio_acordado), 0),
       'Pendiente'
FROM eventos e
JOIN estado_del_evento ee ON ee.id_estado = e.id_estado
LEFT JOIN paquetes pa ON pa.id_paquete = e.id_paquete
LEFT JOIN evento_servicios es ON es.id_evento = e.id_evento
WHERE LOWER(ee.nombre_estado) = 'presupuestado'
  AND NOT EXISTS (
      SELECT 1 FROM presupuestos p
      WHERE p.id_evento = e.id_evento
        AND p.estado_aprobacion = 'Pendiente'
  )
GROUP BY e.id_evento, pa.precio_fijo;

COMMIT;
