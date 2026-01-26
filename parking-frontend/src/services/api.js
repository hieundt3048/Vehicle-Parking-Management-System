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
  
  getAvailableSlots: (zoneId) => 
    api.get('/zones/available-slots', {
      params: zoneId ? { zoneId } : undefined,
    }),
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
  
  getShiftRevenue: (date) =>
    api.get('/reports/revenue/shifts', { params: { date } }),
  
  getOccupancyStats: () => 
    api.get('/reports/occupancy'),
};
// ========================
// Plate Recognition API (Upload ảnh biển số)
// ========================
const YOLO_SERVICE_URL = import.meta.env.VITE_YOLO_SERVICE_URL || 'http://localhost:5000';

export const plateAPI = {
  uploadPlateImage: async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    // Call Python YOLO service directly
    const response = await axios.post(`${YOLO_SERVICE_URL}/detect-plate`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 30000, // 30 seconds for image processing
    });
    
    // Transform response to match expected format
    return {
      data: {
        plateNumber: response.data.plate,
        rawResults: response.data.raw_results,
      }
    };
  },
};

export default api;

