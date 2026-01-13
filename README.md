# Hệ Thống Quản Lý Bãi Xe (Vehicle Parking Management System)

## Mô tả
Hệ thống quản lý bãi xe đơn giản với Spring Boot và SQL Server - Khung sườn cơ bản cho sinh viên.

## Công nghệ sử dụng
- Java 21
- Spring Boot 4.0.1
- Spring Data JPA
- Spring Security (BCrypt)
- SQL Server
- Lombok
- Maven

## Cấu trúc dự án

```
com.parking.system
├── entity/              # Các Entity (User, ParkingZone, ParkingSlot, Ticket, MonthlyTicket)
├── repository/          # Repository interfaces
├── service/             # Business logic đơn giản
├── controller/          # REST API Controllers cơ bản
└── config/              # Configuration (Security)
```

## Cấu hình Database

1. Cài đặt SQL Server
2. Tạo database:
```sql
CREATE DATABASE ParkingDB;
```

3. Cập nhật thông tin kết nối trong `application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ParkingDB;encrypt=false
spring.datasource.username=sa
spring.datasource.password=YourPassword123
```

## Chạy ứng dụng

```bash
mvn clean install
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## API Endpoints

### 1. Authentication (`/api/auth`)

#### Đăng ký user mới
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "fullName": "Quản trị viên",
  "role": "ADMIN"
}
```

#### Lấy danh sách user
```
GET /api/auth/users
```

### 2. Quản lý Khu vực & Slot (`/api/zones`)

#### Tạo khu vực mới
```
POST /api/zones
{
  "name": "Khu A",
  "vehicleType": "MOTORBIKE",
  "totalSlots": 20
}
```

#### Lấy danh sách khu vực
```
GET /api/zones
```

#### Lấy slot trống
```
GET /api/zones/slots/available
```

#### Cập nhật trạng thái slot
```
PUT /api/zones/slots/{slotId}/status?status=OCCUPIED
```

### 3. Quản lý Vé (`/api/tickets`)

#### Tạo vé (xe vào)
```
POST /api/tickets
{
  "licensePlate": "30A-12345",
  "vehicleType": "MOTORBIKE",
  "slotId": 1
}
```

#### Xử lý xe ra
```
POST /api/tickets/{ticketId}/exit
```

#### Lấy tất cả vé
```
GET /api/tickets
```

### 4. Vé tháng (`/api/monthly-tickets`)

#### Đăng ký vé tháng
```
POST /api/monthly-tickets
{
  "cardId": "MT001",
  "licensePlate": "30A-99999",
  "vehicleType": "MOTORBIKE",
  "monthlyFee": 300000
}
```

#### Lấy tất cả vé tháng
```
GET /api/monthly-tickets
```

### 5. Báo cáo (`/api/reports`)

#### Báo cáo doanh thu tổng
```
GET /api/reports
```

## Authentication

Hệ thống sử dụng HTTP Basic Authentication với BCrypt để mã hóa password.

Ví dụ với cURL:
```bash
curl -u admin:admin123 http://localhost:8080/api/zones
```

## Phân quyền

- **ADMIN**: Có quyền truy cập tất cả API, bao gồm báo cáo
- **EMPLOYEE**: Có quyền quản lý vé, slot

## Tính năng chính (Đơn giản hóa)

### 1. Quản lý Slot & Khu vực
- Tạo khu vực đỗ xe
- Xem tất cả khu vực
- Xem slot trống
- Cập nhật trạng thái slot

### 2. Quản lý Vé
- Tạo vé khi xe vào
- Xử lý xe ra và tính phí tự động
- Xem tất cả vé

### 3. Bộ tính phí đơn giản
- Tính phí: Giá cơ bản × Số giờ
- Xe máy: 5,000 VNĐ/giờ
- Ô tô: 10,000 VNĐ/giờ

### 4. Vé tháng
- Đăng ký vé tháng
- Xem tất cả vé tháng

### 5. Báo cáo (Admin)
- Xem tổng doanh thu và số vé

## Lưu ý

- Database sẽ tự động tạo bảng khi chạy lần đầu (ddl-auto=update)
- Password được mã hóa bằng BCrypt
- Code được đơn giản hóa để dễ hiểu cho sinh viên
- Chỉ là khung sườn cơ bản, có thể mở rộng thêm

