-- Datos semilla para auth-service (Operadores)
-- Contraseñas en texto plano compatibles con el PIN de la app Android

INSERT INTO operadores (codigo, pin, nombre, p_nombre, ap_paterno, ap_materno, rol, sucursal_id, turno, fecha_ingreso, ultimo_login, activo)
VALUES
    ('OP-1001', '123456', 'Rin Tohsaka', 'Rin', 'Tohsaka', '', 'TERRENO', 1, 'TARDE', '2024-01-10', '2024-05-20 08:30:00', true),
    ('OP-1002', '654321', 'Gilgamesh', 'Gilgamesh', 'King', '', 'BODEGA', 1, 'MANANA', '2023-11-01', '2024-05-20 07:45:00', true),
    ('OP-1003', '234567', 'Bate galactico', 'Bate', 'Galáctico', '', 'GERENCIA', 1, 'TARDE', '2023-05-15', '2024-05-19 18:20:00', true),
    ('OP-1004', '111222', 'Emiya Shirou', 'Shirou', 'Emiya', '', 'TERRENO', 1, 'MANANA', '2024-02-01', '2024-05-20 08:00:00', true),
    ('OP-1005', '333444', 'Matou Sakura', 'Sakura', 'Matou', '', 'TERRENO', 2, 'MANANA', '2024-03-01', '2024-05-20 08:15:00', true),
    ('OP-1006', '555666', 'Artoria Pendragon', 'Artoria', 'Pendragon', '', 'BODEGA', 2, 'TARDE', '2023-12-01', '2024-05-20 14:00:00', true),
    ('OP-1007', '777888', 'Illyasviel von Einzbern', 'Illyasviel', 'von Einzbern', '', 'TERRENO', 3, 'TARDE', '2024-04-10', '2024-05-20 14:30:00', true),
    ('OP-1008', '999000', 'Tohsaka Tokiomi', 'Tokiomi', 'Tohsaka', '', 'GERENCIA', 4, 'MANANA', '2023-01-01', '2024-05-20 09:00:00', true)
ON CONFLICT (codigo) DO UPDATE
SET nombre = EXCLUDED.nombre,
    pin = EXCLUDED.pin,
    rol = EXCLUDED.rol,
    sucursal_id = EXCLUDED.sucursal_id,
    turno = EXCLUDED.turno;
