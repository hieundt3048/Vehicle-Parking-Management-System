import axios from 'axios';

// Cấu hình base URL cho API
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

// Tạo instance Axios với cấu hình mặc định
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Tự động thêm token vào headers
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Basic ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Xử lý lỗi toàn cục
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Xử lý lỗi 401 (Unauthorized) - Token hết hạn hoặc không hợp lệ
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    
    // Xử lý lỗi 403 (Forbidden)
    if (error.response && error.response.status === 403) {
      console.error('Bạn không có quyền truy cập tài nguyên này.');
    }

    return Promise.reject(error);
  }
);

// ========================
// Auth API
// ========================
export const authAPI = {
  login: (username, password) => 
    api.post('/auth/login', { username, password }),
  
  register: (userData) => 
    api.post('/auth/register', userData),
  
  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  },
};

// ========================
// Zones API (Quản lý khu vực)
// ========================
export const zonesAPI = {
  getAll: () => 
    api.get('/zones'),
  
  getById: (id) => 
    api.get(`/zones/${id}`),
  
  create: (zoneData) => 
    api.post('/zones', zoneData),
  
  update: (id, zoneData) => 
    api.put(`/zones/${id}`, zoneData),
  
  delete: (id) => 
    api.delete(`/zones/${id}`),
  
<<<<<<< HEAD
=======
<<<<<<< HEAD
  getAvailableSlots: () => 
    api.get('/zones/available-slots'),
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
  getAvailableSlots: (zoneId) => 
    api.get('/zones/available-slots', {
      params: zoneId ? { zoneId } : undefined,
    }),
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
};

// ========================
// Tickets API (Quản lý vé xe)
// ========================
export const ticketsAPI = {
  getAll: () => 
    api.get('/tickets'),
  
  getById: (id) => 
    api.get(`/tickets/${id}`),
  
  create: (ticketData) => 
    api.post('/tickets', ticketData),
  
  checkOut: (id) => 
    api.post(`/tickets/${id}/checkout`),
  
  getByPlateNumber: (plateNumber) => 
    api.get(`/tickets/search?plateNumber=${plateNumber}`),
  
  getActiveTickets: () => 
    api.get('/tickets/active'),
};

// ========================
// Reports API (Báo cáo & Thống kê)
// ========================
export const reportsAPI = {
  getDailyRevenue: (date) => 
    api.get('/reports/revenue/daily', { params: { date } }),
  
  getMonthlyRevenue: (month, year) => 
    api.get('/reports/revenue/monthly', { params: { month, year } }),
  
<<<<<<< HEAD
  getShiftRevenue: (date) =>
    api.get('/reports/revenue/shifts', { params: { date } }),
  
=======
<<<<<<< HEAD
=======
  getShiftRevenue: (date) =>
    api.get('/reports/revenue/shifts', { params: { date } }),
  
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
  getOccupancyStats: () => 
    api.get('/reports/occupancy'),
};

<<<<<<< HEAD
=======
<<<<<<< HEAD
// ========================
// Monthly Tickets API (Vé tháng)
// ========================
export const monthlyTicketsAPI = {
  getAll: () => 
    api.get('/monthly-tickets'),
  
  getById: (id) => 
    api.get(`/monthly-tickets/${id}`),
  
  create: (ticketData) => 
    api.post('/monthly-tickets', ticketData),
  
  renew: (id) => 
    api.post(`/monthly-tickets/${id}/renew`),
  
  cancel: (id) => 
    api.delete(`/monthly-tickets/${id}`),
};

=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
export default api;

