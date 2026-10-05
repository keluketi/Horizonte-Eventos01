-- La aplicacion de escritorio no crea cuentas web para los clientes.
-- Permite que clientes.password quede vacio (NULL) en nuevas altas.
ALTER TABLE clientes MODIFY password VARCHAR(255) NULL;
