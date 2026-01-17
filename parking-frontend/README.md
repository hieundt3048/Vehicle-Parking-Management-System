# 🚗 Parking Management System - Frontend

Hệ thống quản lý bãi đậu xe thông minh được xây dựng với **React + Vite + Tailwind CSS**.

## ✨ Tính Năng Chính

- 🔐 **Đăng nhập & Xác thực**: Bảo mật với JWT token
- 📊 **Dashboard**: Thống kê tổng quan về bãi xe
- 🗺️ **Sơ đồ bãi xe**: Xem trạng thái thời gian thực
- ➕ **Check-in (Xe vào)**: Tiếp nhận xe và cấp vé
- ➖ **Check-out (Xe ra)**: Tính phí và thanh toán
- 📱 **Responsive Design**: Hoạt động mượt mà trên mọi thiết bị

## 🚀 Bắt Đầu Nhanh

### 1. Cài Đặt Dependencies

```bash
cd parking-frontend
npm install
```

### 2. Tạo File .env

Tạo file `.env` trong thư mục `parking-frontend/`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 3. Chạy Development Server

```bash
npm run dev
```

Truy cập: **http://localhost:5173**

## 📁 Cấu Trúc Dự Án

```
parking-frontend/
├── src/
│   ├── components/          # React Components
│   │   ├── Login.jsx        # Trang đăng nhập
│   │   ├── Dashboard.jsx    # Bảng điều khiển
│   │   ├── ParkingMap.jsx   # Sơ đồ bãi xe
│   │   ├── CheckIn.jsx      # Tiếp nhận xe vào
│   │   └── CheckOut.jsx     # Thanh toán xe ra
│   ├── services/
│   │   └── api.js           # Axios API configuration
│   ├── App.jsx              # Router & Protected Routes
│   ├── main.jsx             # Entry point
│   └── index.css            # Global styles + Tailwind
├── public/                  # Static assets
├── .env                     # Environment variables (tạo file này)
├── tailwind.config.js       # Tailwind configuration
├── postcss.config.js        # PostCSS configuration
├── vite.config.js           # Vite configuration
└── package.json
```

## 🛠️ Tech Stack

- **Framework**: React 19
- **Build Tool**: Vite 7
- **Routing**: React Router DOM 7
- **HTTP Client**: Axios
- **UI Styling**: Tailwind CSS 3
- **Icons**: Lucide React
- **Language**: JavaScript (ES6+)

## 📋 API Endpoints

Frontend kết nối với các API endpoints sau:

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| `POST` | `/auth/login` | Đăng nhập |
| `POST` | `/auth/register` | Đăng ký tài khoản |
| `GET` | `/zones` | Lấy danh sách khu vực |
| `GET` | `/zones/available-slots` | Lấy vị trí trống |
| `POST` | `/tickets` | Tạo vé (check-in) |
| `POST` | `/tickets/:id/checkout` | Thanh toán (check-out) |
| `GET` | `/tickets/search?plateNumber=xxx` | Tìm vé |
| `GET` | `/reports/revenue/daily` | Báo cáo doanh thu |

## 🔒 Protected Routes

Các route yêu cầu xác thực:
- `/dashboard` - Trang chủ sau khi đăng nhập
- `/parking-map` - Sơ đồ bãi xe
- `/check-in` - Tiếp nhận xe
- `/check-out` - Thanh toán xe ra

Người dùng chưa đăng nhập sẽ tự động chuyển về `/login`.

## 🎨 UI/UX Features

- ✅ Modern glassmorphism design
- ✅ Smooth animations & transitions
- ✅ Real-time data updates
- ✅ Loading states & error handling
- ✅ Mobile-first responsive design
- ✅ Dark/Light theme support (trong tương lai)

## 📝 Scripts

```bash
npm run dev      # Chạy development server
npm run build    # Build production
npm run preview  # Preview production build
npm run lint     # Chạy ESLint
```

## 🐛 Troubleshooting

### Lỗi: Cannot find module 'lucide-react'
```bash
npm install lucide-react
```

### Lỗi: Tailwind classes không áp dụng
- Kiểm tra `tailwind.config.js` đã tồn tại
- Kiểm tra `index.css` có import Tailwind directives
- Xóa cache: `rm -rf node_modules/.vite`

### Lỗi: API connection failed
- Đảm bảo backend chạy tại `http://localhost:8080`
- Kiểm tra file `.env` có `VITE_API_BASE_URL`
- Kiểm tra CORS configuration trên backend

## 📖 Tài Liệu Chi Tiết

Xem file [SETUP.md](./SETUP.md) để biết thêm chi tiết về cài đặt và cấu hình.

## 👥 Tài Khoản Test

```
Username: admin
Password: 123456
```

## 📄 License

© 2024 Parking Management System. All rights reserved.

---

**Phát triển bởi**: Đội ngũ phát triển hệ thống bãi đậu xe  
**Phiên bản**: 1.0.0
