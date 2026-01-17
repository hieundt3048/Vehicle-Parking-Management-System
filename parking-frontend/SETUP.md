# Hướng Dẫn Cài Đặt Parking Management Frontend

## Yêu Cầu Hệ Thống

- Node.js >= 16.0.0
- npm hoặc yarn
- Backend API đang chạy trên `http://localhost:8080`

## Cài Đặt Dependencies

### Bước 1: Cài đặt các gói cơ bản

```bash
cd parking-frontend
npm install
```

### Bước 2: Cài đặt Tailwind CSS

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

### Bước 3: Cài đặt Lucide React (Icons)

```bash
npm install lucide-react
```

### Bước 4: Cấu hình Tailwind CSS

Tạo/cập nhật file `tailwind.config.js`:

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

### Bước 5: Tạo file .env

Tạo file `.env` trong thư mục `parking-frontend/`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Chạy Ứng Dụng

### Development Mode

```bash
npm run dev
```

Ứng dụng sẽ chạy tại: `http://localhost:5173`

### Build Production

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

## Cấu Trúc Thư Mục

```
parking-frontend/
├── public/                 # Static assets
├── src/
│   ├── components/        # React components
│   │   ├── Login.jsx
│   │   ├── Dashboard.jsx
│   │   ├── ParkingMap.jsx
│   │   ├── CheckIn.jsx
│   │   └── CheckOut.jsx
│   ├── services/          # API services
│   │   └── api.js
│   ├── App.jsx            # Main App with routing
│   ├── main.jsx           # Entry point
│   └── index.css          # Global styles + Tailwind
├── .env                   # Environment variables (create this)
├── package.json
├── vite.config.js
└── tailwind.config.js     # Tailwind configuration
```

## Tính Năng

### Đăng Nhập
- Route: `/login`
- Xác thực người dùng qua API
- Lưu token vào localStorage

### Dashboard
- Route: `/dashboard`
- Hiển thị thống kê tổng quan
- Điều hướng đến các tính năng khác

### Sơ Đồ Bãi Xe
- Route: `/parking-map`
- Hiển thị trạng thái thời gian thực
- Phân loại theo khu vực và loại xe

### Check-In (Xe Vào)
- Route: `/check-in`
- Nhập biển số xe
- Gợi ý vị trí trống
- Tạo vé gửi xe

### Check-Out (Xe Ra)
- Route: `/check-out`
- Tìm kiếm vé theo biển số
- Tính phí tự động
- Thanh toán và mở cổng

## API Endpoints

Backend cần cung cấp các endpoints sau:

- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `GET /api/zones` - Lấy danh sách zones
- `GET /api/zones/available-slots` - Lấy vị trí trống
- `POST /api/tickets` - Tạo vé mới (check-in)
- `POST /api/tickets/:id/checkout` - Thanh toán (check-out)
- `GET /api/tickets/search?plateNumber=xxx` - Tìm vé theo biển số
- `GET /api/reports/revenue/daily` - Báo cáo doanh thu

## Troubleshooting

### Lỗi: "Cannot find module 'lucide-react'"
```bash
npm install lucide-react
```

### Lỗi: Tailwind classes không hoạt động
- Kiểm tra `tailwind.config.js` đã cấu hình đúng
- Kiểm tra `index.css` đã import Tailwind directives
- Chạy lại `npm run dev`

### Lỗi: API connection failed
- Kiểm tra backend đang chạy tại `http://localhost:8080`
- Kiểm tra file `.env` đã được tạo với `VITE_API_BASE_URL`
- Kiểm tra CORS đã được cấu hình trên backend

## Tài Khoản Test

Nếu backend có sẵn dữ liệu mẫu:
- Username: `admin`
- Password: `123456`

## Liên Hệ & Hỗ Trợ

Mọi vấn đề kỹ thuật vui lòng liên hệ team phát triển.

