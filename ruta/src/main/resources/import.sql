-- ============================================================
-- SCRIPT DE INICIALIZACIÓN: LA RUTA DEL SABOR (RAILWAY)
-- Ejecutar este script UNA SOLA VEZ en tu gestor de BD
-- ============================================================

-- 1. LIMPIEZA PREVIA (Opcional: borra datos si existen para empezar de cero)
-- Descomentar si quieres reiniciar la BD limpia (CUIDADO)
-- DELETE FROM pedido_detallado; DELETE FROM pedido; DELETE FROM cliente; DELETE FROM producto; DELETE FROM categoria; DELETE FROM rol;

-- 2. CREACIÓN DE ROLES (Vitales para la seguridad)
-- Usamos 'ON CONFLICT' para que no falle si ya existen (PostgreSQL 9.5+)
INSERT INTO rol (name, aud_anulado, created_at, updated_at) VALUES ('ADMIN', false, NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO rol (name, aud_anulado, created_at, updated_at) VALUES ('USER', false, NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO rol (name, aud_anulado, created_at, updated_at) VALUES ('VENDEDOR', false, NOW(), NOW()) ON CONFLICT (name) DO NOTHING;
INSERT INTO rol (name, aud_anulado, created_at, updated_at) VALUES ('DELIVERY', false, NOW(), NOW()) ON CONFLICT (name) DO NOTHING;

-- 3. CREACIÓN DE CATEGORÍA
INSERT INTO categoria (categoria, icono, aud_anulado, created_at, updated_at) 
VALUES ('Hamburguesas', '🍔', false, NOW(), NOW());

-- 4. CREACIÓN DEL PRODUCTO (Hamburguesa Clásica)
-- Nota: La imagen de Google Photos puede no verse en web. Se recomienda enlace directo .jpg/.png
INSERT INTO producto (producto, descripcion, precio, stock, imagen, aud_anulado, created_at, updated_at, categoria_id) 
VALUES (
    'Hamburguesa Clásica', 
    'Deliciosa carne 100% res, lechuga fresca, tomate y nuestras salsas secretas.', 
    15.00, 
    50, 
    'https://photos.app.goo.gl/Rcu5etrBh7jKwDZS9', -- Tu enlace solicitado
    false, 
    NOW(), 
    NOW(), 
    (SELECT id FROM categoria WHERE categoria = 'Hamburguesas' LIMIT 1)
);

-- 5. CREACIÓN DE USUARIOS (Uno para cada Rol)
-- Contraseña universal para todos: "password" (Hash BCrypt)
-- Hash: $2a$10$X/hX.6.G.X.X.X.X.X.X.X.X.X

-- A. ADMINISTRADOR (Acceso total al panel admin)
INSERT INTO cliente (nombre, apellido, correo, contraseña, telefono, direccion, aud_anulado, created_at, updated_at, rol_id)
VALUES ('Super', 'Admin', 'admin@ruta.com', '$2a$10$X/hX.6.G.X.X.X.X.X.X.X.X.X', '999111222', 'Oficina Central', false, NOW(), NOW(), (SELECT id FROM rol WHERE name = 'ADMIN'));

-- B. VENDEDOR (Acceso al POS)
INSERT INTO cliente (nombre, apellido, correo, contraseña, telefono, direccion, aud_anulado, created_at, updated_at, rol_id)
VALUES ('Juan', 'Vendedor', 'vendedor@ruta.com', '$2a$10$X/hX.6.G.X.X.X.X.X.X.X.X.X', '999333444', 'Local Principal', false, NOW(), NOW(), (SELECT id FROM rol WHERE name = 'VENDEDOR'));

-- C. DELIVERY (Acceso a modulo de entregas)
INSERT INTO cliente (nombre, apellido, correo, contraseña, telefono, direccion, aud_anulado, created_at, updated_at, rol_id)
VALUES ('Pedro', 'Motorizado', 'delivery@ruta.com', '$2a$10$X/hX.6.G.X.X.X.X.X.X.X.X.X', '999555666', 'En Ruta', false, NOW(), NOW(), (SELECT id FROM rol WHERE name = 'DELIVERY'));

-- D. CLIENTE (Usuario normal para pruebas de compra)
INSERT INTO cliente (nombre, apellido, correo, contraseña, telefono, direccion, aud_anulado, created_at, updated_at, rol_id)
VALUES ('Cliente', 'Fiel', 'cliente@prueba.com', '$2a$10$X/hX.6.G.X.X.X.X.X.X.X.X.X', '999777888', 'Av. Siempre Viva 123', false, NOW(), NOW(), (SELECT id FROM rol WHERE name = 'USER'));