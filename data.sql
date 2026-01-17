-- 1. Reset dữ liệu cũ (Tùy chọn, cẩn thận khi dùng)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE parking_slot;
-- TRUNCATE TABLE parking_zone;
-- TRUNCATE TABLE users;
-- SET FOREIGN_KEY_CHECKS = 1;

-- 2. Tạo User (Admin & Employee)
-- Mật khẩu "123456" đã mã hóa: $2a$10$eaccAg.5wgwGGTeC.KYrSO.w3w.fPqR.j1y.g.t.1.1.1.1.1
INSERT INTO users (username, password, full_name, role, active) VALUES 
('admin', '$2a$10$eaccAg.5wgwGGTeC.KYrSO.w3w.fPqR.j1y.g.t.1.1.1.1.1', 'System Administrator', 'ADMIN', 1),
('nhanvien1', '$2a$10$eaccAg.5wgwGGTeC.KYrSO.w3w.fPqR.j1y.g.t.1.1.1.1.1', 'Nguyễn Văn A', 'EMPLOYEE', 1),
('nhanvien2', '$2a$10$eaccAg.5wgwGGTeC.KYrSO.w3w.fPqR.j1y.g.t.1.1.1.1.1', 'Trần Thị B', 'EMPLOYEE', 1);

-- 3. Tạo Khu vực đỗ xe (Parking Zones)
-- Zone A: Xe máy (giá 5000/h), Zone B: Ô tô (giá 15000/h)
-- Lưu ý: Kiểm tra lại tên cột trong database nếu code Java đặt khác (vd: unit_price hay price)
INSERT INTO parking_zone (name, unit_price, vehicle_type) VALUES 
('Khu A - Xe Máy', 5000, 'MOTORBIKE'),
('Khu B - Ô Tô', 15000, 'CAR');

-- 4. Tạo Vị trí đỗ (Parking Slots)
-- Giả sử ID của Khu A là 1, Khu B là 2 (do Auto Increment)

-- Slots cho Khu A (Xe máy)
INSERT INTO parking_slot (slot_number, is_available, zone_id) VALUES 
('A-01', 1, 1), ('A-02', 1, 1), ('A-03', 1, 1), ('A-04', 1, 1), ('A-05', 1, 1),
('A-06', 1, 1), ('A-07', 1, 1), ('A-08', 1, 1), ('A-09', 1, 1), ('A-10', 1, 1);

-- Slots cho Khu B (Ô tô)
INSERT INTO parking_slot (slot_number, is_available, zone_id) VALUES 
('B-01', 1, 2), ('B-02', 1, 2), ('B-03', 1, 2), ('B-04', 1, 2), ('B-05', 1, 2),
('B-06', 1, 2), ('B-07', 1, 2), ('B-08', 1, 2), ('B-09', 1, 2), ('B-10', 1, 2);