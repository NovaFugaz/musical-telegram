-- Datos semilla para catalog-service (Sucursales y Productos)

-- 1. Sucursales
INSERT INTO sucursales (id_sucursal, nombre_sucursal, direccion, latitud, longitud, ciudad, region)
VALUES
    (1, 'CD Pudahuel (Hub Central)', 'Av. Américo Vespucio 1234, Pudahuel', -33.4372, -70.7856, 'Santiago', 'Metropolitana'),
    (2, 'Santiago Centro Express', 'Av. Libertador Bernardo O''Higgins 567', -33.4489, -70.6693, 'Santiago', 'Metropolitana'),
    (3, 'Sucursal Viña del Mar', 'Av. Libertad 890', -33.0245, -71.5518, 'Viña del Mar', 'Valparaíso'),
    (4, 'Sucursal Concepción Sur', 'Av. Los Carrera 1450', -36.8270, -73.0503, 'Concepción', 'Biobío')
ON CONFLICT (id_sucursal) DO UPDATE
SET nombre_sucursal = EXCLUDED.nombre_sucursal,
    direccion = EXCLUDED.direccion,
    latitud = EXCLUDED.latitud,
    longitud = EXCLUDED.longitud,
    ciudad = EXCLUDED.ciudad,
    region = EXCLUDED.region;

-- 2. Productos de catálogo
INSERT INTO productos (sku, ean13, nombre, descripcion, unidad_medida, valor, stock_minimo)
VALUES
    ('SKU-7704219', '7801234567890', 'Esmeril Angular 4 1/2" 850W Dewalt', 'Herramientas eléctricas pesadas', 'UN', 148420.0, 10),
    ('SKU-8841920', '7809482100412', 'Taladro Percutor Inalámbrico 20V Dewalt', 'Motor Brushless Industrial', 'UN', 95000.0, 8),
    ('SKU-3361284', '7805541299831', 'Disco de Corte Metal 4 1/2" Norton (x10)', 'Pack x10 unidades extra fino', 'CJ', 12500.0, 25),
    ('SKU-5519283', '7806543210987', 'Sierra Circular 7 1/4" 1800W Bosch', 'Sierra circular profesional para corte de madera', 'UN', 119990.0, 5),
    ('SKU-4409182', '7807654321098', 'Caja de Tornillos Autoperforantes 2" (x500)', 'Fijaciones galvanizadas de alta resistencia', 'CJ', 18900.0, 30)
ON CONFLICT (sku) DO UPDATE
SET ean13 = EXCLUDED.ean13,
    nombre = EXCLUDED.nombre,
    descripcion = EXCLUDED.descripcion,
    unidad_medida = EXCLUDED.unidad_medida,
    valor = EXCLUDED.valor,
    stock_minimo = EXCLUDED.stock_minimo;
