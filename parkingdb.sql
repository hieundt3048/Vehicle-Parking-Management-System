-- ====================================================================
-- SCRIPT RESET DATABASE - PARKING MANAGEMENT SYSTEM
-- SQL Server Compatible
-- Database: parkingdb
-- ====================================================================

-- Use master to drop and recreate database
USE master;
GO

-- Drop database if exists
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'parkingdb')
BEGIN
    ALTER DATABASE parkingdb SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE parkingdb;
END
GO

-- Create database
CREATE DATABASE parkingdb;
GO

USE parkingdb;
GO

-- ====================================================================
-- CREATE TABLES
-- ====================================================================

-- Table: users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    role NVARCHAR(20) NOT NULL,
    active BIT NOT NULL DEFAULT 1
);
GO

CREATE INDEX idx_username ON users(username);
GO

-- Table: parking_zones
CREATE TABLE parking_zones (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    vehicle_type NVARCHAR(20) NOT NULL,
    total_slots INT NOT NULL
);
GO

CREATE INDEX idx_vehicle_type ON parking_zones(vehicle_type);
GO

-- Table: parking_slots
CREATE TABLE parking_slots (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    slot_number NVARCHAR(20) NOT NULL UNIQUE,
    status NVARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    zone_id BIGINT NOT NULL,
    CONSTRAINT FK_parking_slots_zone FOREIGN KEY (zone_id) REFERENCES parking_zones(id) ON DELETE CASCADE
);
GO

CREATE INDEX idx_status ON parking_slots(status);
CREATE INDEX idx_zone_status ON parking_slots(zone_id, status);
GO

-- Table: tickets
CREATE TABLE tickets (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    license_plate NVARCHAR(20) NOT NULL,
    vehicle_type NVARCHAR(20) NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME NULL,
    slot_id BIGINT NULL,
    total_amount FLOAT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT FK_tickets_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id) ON DELETE SET NULL
);
GO

CREATE INDEX idx_license_status ON tickets(license_plate, status);
CREATE INDEX idx_status_entry ON tickets(status, entry_time);
GO

-- ====================================================================
-- INSERT SAMPLE DATA
-- ====================================================================

-- Users (password: admin123)
-- BCrypt hash: $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
INSERT INTO users (username, password, full_name, role, active) VALUES
(N'admin', N'$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', N'Administrator', N'ADMIN', 1),
(N'nhanvien', N'$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', N'Nhân viên', N'EMPLOYEE', 1);
GO

-- Parking zones
SET IDENTITY_INSERT parking_zones ON;
INSERT INTO parking_zones (id, name, vehicle_type, total_slots) VALUES
(1, N'Khu A - Xe Máy', N'MOTORBIKE', 20),
(2, N'Khu B - Ô Tô', N'CAR', 12);
SET IDENTITY_INSERT parking_zones OFF;
GO

-- Parking slots for Zone A (Motorbike)
INSERT INTO parking_slots (slot_number, status, zone_id) VALUES
(N'A-01', N'AVAILABLE', 1),
(N'A-02', N'AVAILABLE', 1),
(N'A-03', N'AVAILABLE', 1),
(N'A-04', N'AVAILABLE', 1),
(N'A-05', N'AVAILABLE', 1),
(N'A-06', N'AVAILABLE', 1),
(N'A-07', N'AVAILABLE', 1),
(N'A-08', N'AVAILABLE', 1),
(N'A-09', N'AVAILABLE', 1),
(N'A-10', N'AVAILABLE', 1),
(N'A-11', N'AVAILABLE', 1),
(N'A-12', N'AVAILABLE', 1),
(N'A-13', N'AVAILABLE', 1),
(N'A-14', N'AVAILABLE', 1),
(N'A-15', N'AVAILABLE', 1),
(N'A-16', N'AVAILABLE', 1),
(N'A-17', N'AVAILABLE', 1),
(N'A-18', N'AVAILABLE', 1),
(N'A-19', N'AVAILABLE', 1),
(N'A-20', N'AVAILABLE', 1);
GO

-- Parking slots for Zone B (Car)
INSERT INTO parking_slots (slot_number, status, zone_id) VALUES
(N'B-01', N'AVAILABLE', 2),
(N'B-02', N'AVAILABLE', 2),
(N'B-03', N'AVAILABLE', 2),
(N'B-04', N'AVAILABLE', 2),
(N'B-05', N'AVAILABLE', 2),
(N'B-06', N'AVAILABLE', 2),
(N'B-07', N'AVAILABLE', 2),
(N'B-08', N'AVAILABLE', 2),
(N'B-09', N'AVAILABLE', 2),
(N'B-10', N'AVAILABLE', 2),
(N'B-11', N'AVAILABLE', 2),
(N'B-12', N'AVAILABLE', 2);
GO

-- ====================================================================
-- VERIFY DATA
-- ====================================================================
SELECT '=== USERS ===' AS Info;
SELECT id, username, full_name, role, active FROM users;

SELECT '=== PARKING ZONES ===' AS Info;
SELECT * FROM parking_zones;

SELECT '=== PARKING SLOTS ===' AS Info;
SELECT COUNT(*) AS TotalSlots, zone_id, status FROM parking_slots GROUP BY zone_id, status;

SELECT '=== SETUP COMPLETED ===' AS Info;
SELECT 
    (SELECT COUNT(*) FROM users) AS Users,
    (SELECT COUNT(*) FROM parking_zones) AS Zones,
    (SELECT COUNT(*) FROM parking_slots) AS Slots,
    (SELECT COUNT(*) FROM tickets) AS Tickets;
GO
