-- Datos semilla para inventory-service (Stock por Sucursal, Alertas, Conteos, Solicitudes, Tareas)

-- 1. Stock por Sucursal (inventario_sucursal)
INSERT INTO inventario_sucursal (sku, sucursal_id, stock_tienda, zona, pasillo, estante, nivel)
VALUES
    ('SKU-7704219', 1, 24, 'Principal', 'B-04', '3', '2'),
    ('SKU-8841920', 1, 3,  'Principal', 'A-12', '1', '1'),
    ('SKU-3361284', 1, 0,  'Principal', 'B-02', '2', '1'),
    ('SKU-5519283', 1, 12, 'Principal', 'A-05', '4', '2'),
    ('SKU-4409182', 1, 45, 'Bodega',    'C-01', '2', '3'),

    ('SKU-7704219', 2, 15, 'Tienda',    'P-01', '2', '1'),
    ('SKU-8841920', 2, 2,  'Tienda',    'P-03', '1', '2'),
    ('SKU-3361284', 2, 0,  'Tienda',    'P-04', '3', '1')
ON CONFLICT (sku, sucursal_id) DO UPDATE
SET stock_tienda = EXCLUDED.stock_tienda,
    zona = EXCLUDED.zona,
    pasillo = EXCLUDED.pasillo,
    estante = EXCLUDED.estante,
    nivel = EXCLUDED.nivel;

-- 2. Alertas iniciales
INSERT INTO alertas (id, titulo_alerta, sku, ean13, nombre_producto, stock_actual, stock_requerido, ubicacion_nombre, severidad, fecha_hora, atendida)
VALUES
    ('ALT-001', 'QUIEBRE ACTIVO', 'SKU-3361284', '7805541299831', 'Disco de Corte Metal 4 1/2" Norton (x10)', 0, 25, 'Santiago Centro Express - Pasillo 04-B', 'QUIEBRE_CRITICO', '2024-05-20 09:30:00', false),
    ('ALT-002', 'STOCK CRÍTICO INMINENTE', 'SKU-8841920', '7809482100412', 'Taladro Percutor Inalámbrico 20V Dewalt', 3, 8, 'CD Pudahuel - Rack A-12', 'STOCK_CRITICO_INMINENTE', '2024-05-20 08:15:00', false),
    ('ALT-003', 'DESCALCE EN CONTEO', 'SKU-7704219', '7801234567890', 'Esmeril Angular 4 1/2" 850W Dewalt', 21, 24, 'Santiago Centro Express - Pasillo 02-C', 'DESCALCE_CONTEO', '2024-05-19 17:45:00', true),
    ('ALT-004', 'STOCK PREVENTIVO', 'SKU-5519283', '7806543210987', 'Sierra Circular 7 1/4" 1800W Bosch', 6, 5, 'CD Pudahuel - Rack A-05', 'ADVERTENCIA_PREVENTIVA', '2024-05-19 10:00:00', false)
ON CONFLICT (id) DO UPDATE
SET titulo_alerta = EXCLUDED.titulo_alerta,
    stock_actual = EXCLUDED.stock_actual,
    stock_requerido = EXCLUDED.stock_requerido,
    severidad = EXCLUDED.severidad,
    atendida = EXCLUDED.atendida;

-- 3. Conteos físicos (Diferencias de stock)
INSERT INTO conteos_fisicos (sku, stock_teorico, conteo_fisico, diferencia, motivo, operador_codigo, sucursal_id, fecha_hora)
VALUES
    ('SKU-7704219', 24, 21, -3, 'Merma / Daño', 'OP-1001', 1, '2024-05-20 14:00:00'),
    ('SKU-8841920', 8,  3,  -5, 'Error previo', 'OP-1001', 1, '2024-05-20 15:30:00'),
    ('SKU-3361284', 25, 20, -5, 'Traspaso no registrado', 'OP-1004', 1, '2024-05-19 11:20:00');

-- 4. Solicitudes de reposición
INSERT INTO solicitudes_reposicion (sku, nombre_producto, cantidad, destino, prioridad, observacion, estado, fecha_iso, sucursal_id)
VALUES
    ('SKU-3361284', 'Disco de Corte Metal 4 1/2" Norton (x10)', 30, 'Bodega Central', 'Urgente', 'Stock en quiebre total en bodega local', 'Pendiente', '2024-05-20 10:00:00', 1),
    ('SKU-8841920', 'Taladro Percutor Inalámbrico 20V Dewalt', 15, 'Proveedor Directo', 'Normal', 'Stock actual (3 u.) bajo stock mínimo (8 u.)', 'En Tránsito', '2024-05-19 16:00:00', 1);

-- 5. Tareas iniciales de operadores
INSERT INTO tareas (operador_codigo, titulo, detalle, accion, tipo, sucursal_id)
VALUES
    ('OP-1001', 'Conteo Diario Pasillo 04', '12 SKUs pendientes de validación física', 'CONTINUAR', 'CONTEO', 1),
    ('OP-1001', 'Verificar Quiebre SKU-3361284', 'Validar si quedan unidades en bodega posterior', 'REVISAR', 'RECONTEO', 1),
    ('OP-1001', 'Recepción Camión Bodega Central', 'Revisión de bultos según Orden #44821', 'INICIAR', 'RECEPCION', 1);
