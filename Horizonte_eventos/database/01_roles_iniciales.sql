-- Carga los roles iniciales en la base horizonte_eventos.
-- Ejecuta este archivo una sola vez o vuelve a ejecutarlo sin duplicar registros.
-- Se mantienen los cuatro IDs de rol definidos para la aplicacion de escritorio.

INSERT INTO roles (id_rol, nombre_rol) VALUES
    (1, 'Administracion'),
    (2, 'Coordinacion'),
    (3, 'Personal'),
    (4, 'Gerencia')
ON DUPLICATE KEY UPDATE nombre_rol = VALUES(nombre_rol);
